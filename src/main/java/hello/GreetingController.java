package hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import hello.model.ImgurImage;
import hello.util.ApplicationConfig;
import hello.util.ImgurUtil;

@RestController
public class GreetingController {

    private static Logger LOG = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private ApplicationConfig appConfig;

    @Autowired
    private ImgurUtil imgurUtil;

    @RequestMapping("/rest/image")
    public ImgurImage greeting(@RequestParam(value = "id", defaultValue = "H0ofcXJ") String id) {
	ImgurImage imgurImage = imgurUtil.getImgurImageById(id);
	return imgurImage;
    }

    @RequestMapping("/rest/code")
    public String code(@RequestParam(value = "code", defaultValue ="something") String code) {
	try {
	    Table table = appConfig.getDynamoDBClient().getTable("TestTable");
	    LOG.info("Adding a new item...");
	    PutItemOutcome outcome = table.putItem(new Item().withPrimaryKey("key", "testKey2", "title", "testTitle")
		    .withJSON("info", "{\"plot\" : \"Something happens.\"}"));

	    LOG.info("PutItem succeeded:\n" + outcome.getPutItemResult());

	} catch (Exception e) {
	    LOG.error("Unable to add item.");
	    LOG.error(e.getMessage());
	}
	LOG.info("Received authentication code: " + code);
	return "Hi";
    }
}