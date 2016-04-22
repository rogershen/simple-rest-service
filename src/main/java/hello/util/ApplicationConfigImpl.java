package hello.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@Component
public class ApplicationConfigImpl implements ApplicationConfig {

    private static Logger LOG = LoggerFactory.getLogger(ApplicationConfigImpl.class);
    
    private static Path localFilePath;
    
    private static Map<String, String> configMap;
    
    private DynamoDB ddb;
    
    private boolean isDesktop() {
        return "ROGERSHEN-PC".equals(System.getenv().get("USERDOMAIN_ROAMINGPROFILE"));
    }
    
    @PostConstruct
    private void init() {
	LOG.info("Initializing Application Config");
	localFilePath = FileSystems.getDefault().getPath(".env");
	configMap = new HashMap<>();
	/* Mock getting environment variables by using .env file */
	if(isDesktop()) {
	    List<String> configEntries = new ArrayList<>();
	    try {
		Files.lines(localFilePath).forEach(line -> configEntries.add((line)));
	    } catch (IOException e) {
		LOG.error("Unable to load config from local file: " + e);
	    }
	    for(String configEntry : configEntries) {
		String[] splitConfigEntry = configEntry.split("=");
		if(splitConfigEntry.length == 2) {
		    configMap.put(splitConfigEntry[0], splitConfigEntry[1]);
		} else {
		  LOG.info("Invalid config entry: " + configEntry);  
		}
	    }
	    try {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(configMap.get(MyConstants.AWS_ACCESS_KEY_ID), configMap.get(MyConstants.AWS_SECRET_ACCESS_KEY));
		AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(awsCreds).withRegion(Regions.US_WEST_2);
		ddb = new DynamoDB(dynamoDBClient);
	    } catch (Exception e) {
		LOG.error("Exception when trying to create ddb client on desktop: ", e);
	    }
	} else {
	    try {
		EnvironmentVariableCredentialsProvider awsCreds = new EnvironmentVariableCredentialsProvider();
		AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(awsCreds).withRegion(Regions.US_WEST_2);
		ddb = new DynamoDB(dynamoDBClient);
	    } catch (Exception e) {
		LOG.error("Exception when trying to create ddb client on heroku: ", e);
	    }
	}
	LOG.info("Finished initializing Application Config");
    }
    
    @Override
    public String get(String var) {
	if(isDesktop()) {
	    return configMap.get(var);
	} else {
	    return System.getenv(var);
	}
        
    }
    
    @Override
    public DynamoDB getDynamoDBClient() {
	return ddb;
    }
}
