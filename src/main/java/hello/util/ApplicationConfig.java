package hello.util;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public interface ApplicationConfig {
    
    public String get(String var);

    DynamoDB getDynamoDBClient();
    
}
