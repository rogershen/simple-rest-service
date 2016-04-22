package hello.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import hello.auth.ImgurAuth;
import hello.model.GetImageResponse;
import hello.model.ImgurImage;

@Component
public class ImgurUtilImpl implements ImgurUtil {
    
    private static Logger LOG = LoggerFactory.getLogger(ImgurUtilImpl.class);
    
    @Autowired
    private ImgurAuth imgurAuth;
    
    private ImgurImage handleImageJSON(String imageJSON) {
        GetImageResponse getImageResp = new GetImageResponse();
        
	ObjectMapper mapper = new ObjectMapper();
	 mapper.setPropertyNamingStrategy(
		    PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	try {
	    mapper.readerForUpdating(getImageResp).readValue(imageJSON);
	} catch (JsonParseException|JsonMappingException e ) {
	    LOG.error("JSON parsing error: ", e);
	} catch (IOException e) {
	    LOG.error("IOException when parsing JSON: ", e);
	}
	return getImageResp.getData();
    }
    
    @Override
    public ImgurImage getImgurImageById(String imageId) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("https://api.imgur.com/3/image/" + imageId).openConnection();
            conn.setDoOutput(false);
            
            imgurAuth.authorizeConnection(conn);
            InputStream inputStream = conn.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "UTF-8");
            return handleImageJSON(writer.toString());
        } catch(Exception e) {
            LOG.error("Error trying to receive image JSON: ", e);
        }
	return null;
    }
}
