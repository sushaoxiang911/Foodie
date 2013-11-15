package edu.umich.eecs441.foodie;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;


/**
 * This class is for the connection between Foodie with aws DynamoDB. 
 * It provides client create, client log-in, add mark, cancel mark mathod.
 * @author Shaoxiang Su
 *
 */

// use singleton to do that since this will be used in multiple classes
public class DynamoDBOperation {
	
	static private DynamoDBOperation instance = null;
	
	static private AmazonDynamoDBClient client;
	
	static private String USERINFOTABLE = "USER_INFOR";
	static private String MARKRECORDTABLE = "MARK_RECORD";
	
	static private String MAX = "!@#$%^&*()";
	
	
	
	private static void createClient() throws IOException {
	        
	        AWSCredentials credentials = new PropertiesCredentials(
	                DynamoDBOperation.class.getResourceAsStream("AwsCredentials.properties"));

	        client = new AmazonDynamoDBClient(credentials);       
	}
	 
	private DynamoDBOperation() throws IOException{
		createClient();
	}
	 
	 /**
	  * This method returns the instance of the class. 
	  * If the instance does not exist, create one and return it.
	  * @return DynamoDBOperation instance
	  * @throws IOException
	  */
	public static DynamoDBOperation getInstance() throws IOException {
		if (instance == null) {
			instance = new DynamoDBOperation();
		}
		 
		return instance;
	}
	 
	 /**
	  * Create a new client with specific information. The function will check if the user has already existed.
	  * Assign an ID to every client
	  * @param username String, the new client's username
	  * @param password String, the new client's password
	  * @return 0 - user existed, try another one<br>
	  * 		1 - success
	  */
	public int clientCreate (String username, String password) {
		 
		// check if the username has already existed
		HashMap <String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("USER_NAME", new AttributeValue().withS(username));
		 
		GetItemRequest getItemRequest = new GetItemRequest()
			.withTableName(USERINFOTABLE)
			.withKey(key);
		 
		GetItemResult result = client.getItem(getItemRequest);
		if (result.getItem() != null) {
			// the username exists
			return 0;
		}
		key.clear();
		 
		 // save one extra item MAX into the table to keep track of the max id
		key.put("USER_NAME", new AttributeValue().withS(MAX));
		 
		getItemRequest = new GetItemRequest()
			.withTableName(USERINFOTABLE)
			.withKey(key);
		 
		result = client.getItem(getItemRequest);
		int maxId = Integer.valueOf(result.getItem().get("ID").getN());
		key.clear();
		 
		// update the max by 1
		Map <String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("USER_NAME", new AttributeValue().withS(MAX));
		item.put("ID", new AttributeValue().withN(String.valueOf(maxId + 1)));
		 
		PutItemRequest itemRequest = new PutItemRequest().withTableName(USERINFOTABLE).withItem(item);
		client.putItem(itemRequest);
		item.clear();
		 
		// create the new client, update id by 1
		item.put("USER_NAME", new AttributeValue().withS(username));
		item.put("USER_PASSWORD", new AttributeValue().withS(password));
		item.put("ID", new AttributeValue().withN(String.valueOf(maxId + 1)));
		 
		itemRequest = new PutItemRequest().withTableName(USERINFOTABLE).withItem(item);
		client.putItem(itemRequest);
		item.clear();
		return 1;
	}
	 
	 /**
	  * clientLogin goes into the database check the username and password
	  * @param username String, the username entered by client
	  * @param password String, the password entered by client
	  * @return -1 - the user does not existed <br>
	  * 		-2 - the username does not match password <br>
	  * 		else - the user id
	  */
	 public int clientLogin (String username, String password) {
		 
		HashMap <String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("USER_NAME", new AttributeValue().withS(username));
		 
		GetItemRequest getItemRequest = new GetItemRequest()
			.withTableName(USERINFOTABLE)
			.withKey(key);
		 
		GetItemResult result = client.getItem(getItemRequest);
		if (result.getItem() == null) {
			// the username does not exist
			return -1;
		}
		 
		 
		if (!result.getItem().get("USER_PASSWORD").getS().equals(password)) {
			// username, password do not match
			return -2;
		}
		key.clear();
		 
		 // else return user id
		return Integer.valueOf(result.getItem().get("ID").getN());
	}
	 
	// TODO: in foodie project, create a singleton to save the user status.
	
	/**
	 * This method insert a new data entry into MARK_RECORD table with specific userId, mealName, mealTranslation, picUrl<br>
	 * The new record is guaranteed not existing
	 * @param userId int, when client successfully login, the id would be held by client
	 * @param mealName String, the name that the client search
	 * @param mealTranslation String, the translation that the client obtains
	 * @param picUrl String, the URL of the meal picture
	 */
	public void markMeal (int userId, String mealName, String mealTranslation, String picUrl) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		item.put("USER_ID", new AttributeValue().withN(String.valueOf(userId)));
		item.put("MEAL_NAME", new AttributeValue().withS(mealName));
		item.put("MEAL_TRANSLATION", new AttributeValue().withS(mealTranslation));
		item.put("PIC_URL", new AttributeValue().withS(picUrl));
		
		PutItemRequest putItemRequest = new PutItemRequest()
			.withTableName(MARKRECORDTABLE)
			.withItem(item);
		
		client.putItem(putItemRequest);
	}
	/**
	 * This method checks if the record for a specific user exists or not
	 * @param userId int, when client successfully login, the id would be held by client 
	 * @param mealName String, the name that the client search
	 * @return 	1 - exists <br>
	 * 			0 - not exist
	 */
	public int checkMeal (int userId, String mealName) {
		// check if the (userId, mealName) has existed
		Condition userIdCondition = new Condition()
			.withComparisonOperator(ComparisonOperator.EQ)
			.withAttributeValueList(new AttributeValue().withN(String.valueOf(userId)));
		Condition mealNameCondition = new Condition()
			.withComparisonOperator(ComparisonOperator.EQ)
			.withAttributeValueList(new AttributeValue().withS(mealName));
		
		Map<String, Condition> keyConditions = new HashMap <String, Condition>();
		keyConditions.put("USER_ID", userIdCondition);
		keyConditions.put("MEAL_NAME", mealNameCondition);
		
		QueryRequest queryRequest = new QueryRequest()
			.withTableName(MARKRECORDTABLE)
			.withKeyConditions(keyConditions);
		
		QueryResult result = client.query(queryRequest);
		
		if (result.getItems().size() != 0) {
			// already exists
			return 1;
		} else {
			return 0;
		}
	}
	
	// cancel mark
	/**
	 * This function cancel a previous marking
	 * @param userId int, when client successfully login, the id would be held by client 
	 * @param mealName String, the name that the client search
	 */
	public void cancelMeal(int userId, String mealName) {
		Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("USER_ID", new AttributeValue().withN(String.valueOf(userId)));
		key.put("MEAL_NAME", new AttributeValue().withS(mealName));
		
		DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
			.withTableName(MARKRECORDTABLE)
			.withKey(key);
		DeleteItemResult deleteItemResult = client.deleteItem(deleteItemRequest);
		
		System.out.println(deleteItemResult.toString());
	}
	// get all record
	/**
	 * This function returns all the records of a specific client
	 * @param userId
	 * @return Vector of BackendMealEntry
	 */ 
	public Vector<BackendMealEntry> getMeal (int userId) {
		Vector<BackendMealEntry> result = new Vector<BackendMealEntry>();
		
		Condition userIdCondition = new Condition()
			.withComparisonOperator(ComparisonOperator.EQ)
			.withAttributeValueList(new AttributeValue().withN(String.valueOf(userId)));
		
		Map<String, Condition> keyConditions = new HashMap <String, Condition>();
		keyConditions.put("USER_ID", userIdCondition);
		
		QueryRequest queryRequest = new QueryRequest()
		.withTableName(MARKRECORDTABLE)
		.withKeyConditions(keyConditions);
		
		QueryResult queryResult = client.query(queryRequest);
		
		for (Map<String, AttributeValue> item : queryResult.getItems()) {
		    result.add(new BackendMealEntry(item));
		}
		return result;
	}
	
	
}
