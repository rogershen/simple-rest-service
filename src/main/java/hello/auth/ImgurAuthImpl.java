package hello.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import hello.model.GetAccessTokenResponse;
import hello.model.GetImageResponse;
import hello.model.ImgurImage;
import hello.util.ApplicationConfig;
import hello.util.MyConstants;

@Component
public class ImgurAuthImpl implements ImgurAuth {
    
    private static Logger LOG = LoggerFactory.getLogger(ImgurAuthImpl.class);
    
    private String accessToken = null;
    
    @Autowired
    private ApplicationConfig appConfig;
    
    private String clientId = "90e7486c8a5cfad";
    
    @Override
    public void authorizeConnection(HttpURLConnection conn){
        if (!StringUtils.isEmpty(accessToken)) {
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }
        else {
            conn.setRequestProperty("Authorization", "Client-ID " + appConfig.get(MyConstants.IMGUR_CLIENT_ID));
        }
    }
    
    @Override
    public void authorizeClient() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("https://api.imgur.com/oauth2/authorize?client_id="+clientId+"&response_type=authorization_code").openConnection();
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.flush();
            out.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                handleAccessTokenResponse(in);
                in.close();
            }
            else {
                LOG.info("2responseCode=" + conn.getResponseCode());
                InputStream errorStream = conn.getErrorStream();
                StringBuilder sb = new StringBuilder();
                Scanner scanner = new Scanner(errorStream);
                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }
                LOG.info("2error response: " + sb.toString());
                errorStream.close();
            }
        } catch (Exception e) {
            LOG.error("Unable to request new access token: ", e);
        }
    }
    
    @Override
    public void requestNewAccessToken() {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("https://api.imgur.com/oauth2/token").openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Client-ID " + clientId);

            ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("client_id", clientId));
            nvps.add(new BasicNameValuePair("client_secret", "c7c68a676cb6f3e107ec050afeb1d38159684926"));
            nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps);

            OutputStream out = conn.getOutputStream();
            entity.writeTo(out);
            out.flush();
            out.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                handleAccessTokenResponse(in);
                in.close();
            }
            else {
                LOG.info("responseCode=" + conn.getResponseCode());
                InputStream errorStream = conn.getErrorStream();
                StringBuilder sb = new StringBuilder();
                Scanner scanner = new Scanner(errorStream);
                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }
                LOG.info("error response: " + sb.toString());
                errorStream.close();
            }
        } catch (Exception e) {
            LOG.error("Unable to request new access token: ", e);
        }
    }
    
    @Override
    public void sendAuthenticationCode(String code) {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("https://api.imgur.com/oauth2/token").openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Client-ID " + clientId);

            ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("client_id", clientId));
            nvps.add(new BasicNameValuePair("client_secret", "c7c68a676cb6f3e107ec050afeb1d38159684926"));
            nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
            nvps.add(new BasicNameValuePair("code", code));
            
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps);

            OutputStream out = conn.getOutputStream();
            entity.writeTo(out);
            out.flush();
            out.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                handleAccessTokenResponse(in);
                in.close();
            }
            else {
                LOG.info("1responseCode=" + conn.getResponseCode());
                InputStream errorStream = conn.getErrorStream();
                StringBuilder sb = new StringBuilder();
                Scanner scanner = new Scanner(errorStream);
                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }
                LOG.info("1error response: " + sb.toString());
                errorStream.close();
            }
        } catch (Exception e) {
            LOG.error("Unable to request new access token: ", e);
        }
    }
    
    public void handleAccessTokenResponse(InputStream in) {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext()) {
            sb.append(scanner.next());
        }
        scanner.close();
        
        GetAccessTokenResponse getAccessTokenResponse = new GetAccessTokenResponse();
        
	ObjectMapper mapper = new ObjectMapper();
	 mapper.setPropertyNamingStrategy(
		    PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	try {
	    mapper.readerForUpdating(getAccessTokenResponse).readValue(sb.toString());
	} catch (JsonParseException|JsonMappingException e ) {
	    LOG.error("JSON parsing error: ", e);
	} catch (IOException e) {
	    LOG.error("IOException when parsing JSON: ", e);
	}
    }
}
