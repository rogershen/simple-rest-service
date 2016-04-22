package hello.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;

import hello.auth.ImgurAuth;
import hello.auth.ImgurAuthImpl;

public class Tester {
    
    private static Path localFilePath = FileSystems.getDefault().getPath(".env");

    private static final String UPLOAD_URL = "https://api.imgur.com/3/image/nV8JXSX";
    
    private static final String AUTH_URL = "https://api.imgur.com/oauth2/authorize?";
    
    private static ImgurAuth imgurAuth;
    
    private static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    //public static void main(String[] args) throws MalformedURLException, IOException {
        //HttpURLConnection conn = null;
        //InputStream responseIn = null;

        //conn = (HttpURLConnection) new URL(AUTH_URL).openConnection();
        //conn.setDoOutput(false);
	//imgurAuth = new ImgurAuthImpl();
        //imgurAuth.requestNewAccessToken();
        
        /*
        imgurAuth = new ImgurAuthImpl();
        imgurAuth.authorizeConnection(conn);
        InputStream inputStream = conn.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        String theString = writer.toString();
        System.out.println(theString);*/
        /*
        copy(responseIn, out);
        out.flush();
        out.close();
        */
    //}
}
