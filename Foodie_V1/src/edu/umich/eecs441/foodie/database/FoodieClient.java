package edu.umich.eecs441.foodie.database;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import android.util.Log;
import edu.umich.eecs441.foodie.BackendMealEntry;
import edu.umich.eecs441.foodie.DynamoDBOperation;

/**
 * This class is a singleton client class, which includes user_id and status of the client
 * @author Shaoxiang Su
 *
 */
public class FoodieClient {

	private static String TAG = "FoodieClient.";
	
	private int userId;
	private int clientStatus;
		
	public static int ONLINE = 0;
	public static int OFFLINE = 1;
	
	private static FoodieClient instance = null;
	private FoodieClient(){
		userId = -1;
		clientStatus = OFFLINE;
	}
	public static FoodieClient getInstance(){
		if (instance == null){
			instance = new FoodieClient();
		}
		return instance;
	}
	
	/**
	 * This function returns the current status of the client
	 * @return 	FoodieClient.ONLINE - client is online<br>
	 * 			FoodieClient.OFFLINE - client is offline
	 */
	public int getClientStatus(){
		return clientStatus;
	}
	
	
	public int getUserId() {
		return userId;
	}
	
	/**
	 * This function is login check
	 * @param username
	 * @param password
	 * @return 	-1 - the user does not existed <br>
				-2 - the username does not match password <br>
				else - the user id 
	 * @throws IOException
	 */
	public int clientLogin(String username, String password) throws IOException{
		// get the instance
		DynamoDBOperation operation = DynamoDBOperation.getInstance();
		int result = operation.clientLogin(username, password);
		if (result != -1 || result != -2){
			userId = result;
			clientStatus = ONLINE;
		}
		return result;
	}
	
	/**
	 * This function implements a client logout
	 */
	public void clientLogout(){
		userId = -1;
		clientStatus = OFFLINE;
	}
	/**
	 * This function obtains all the marking meal from the database
	 * @return List, all the meal entry
	 * @throws IOException
	 */
	public List<MealEntry> getMarkingData () throws IOException {
		
		List<MealEntry> result = new Vector<MealEntry>();
		if (clientStatus == OFFLINE) {
			Log.i(TAG + "getMarkingData()", "the client is offline");
			return null;
		}
		Vector<BackendMealEntry> temp = DynamoDBOperation.getInstance().getMeal(userId);
		for (int i = 0; i < temp.size(); i++) {
			result.add(new MealEntry(temp.get(i)));
		}
		return result;
	}
	
}
