package edu.umich.eecs441.foodie.database;

import java.io.IOException;

import edu.umich.eecs441.foodie.backend.BackendMealEntry;
import edu.umich.eecs441.foodie.backend.DynamoDBOperation;



/**
 * This class is to make connections with DynamoDB back-end
 * @author Shaoxiang Su
 *
 */
public class MealEntry {
	
	private static String TAG = "MealEntry.";
	private String recMealName;
	private String picMealName;
	private String picUrl;
	private String mealTranslation;
	
	/**
	 * This function deletes the current meal entry
	 * @throws IOException
	 */
	public void deleteMeal() throws IOException {
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.OFFLINE){
		}
		DynamoDBOperation.getInstance().cancelMeal(FoodieClient.getInstance().getUserId(), picMealName);
	}
	
	// TODO: Actually, it depends on how to interpret the object. I can think of the entry object just exists in bookmark table, or it exists when we find a result(need further change)
	// mark, create a backend object, call backend funciton
	public void addMeal() throws IOException {
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.OFFLINE){
		}
		DynamoDBOperation.getInstance().markMeal(new BackendMealEntry(FoodieClient.getInstance().getUserId(), picMealName, mealTranslation, picUrl));
	}
	
	
	public boolean existedMeal() throws IOException {
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.OFFLINE){
		}
		int result = DynamoDBOperation.getInstance().checkMeal(FoodieClient.getInstance().getUserId(), picMealName);
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public MealEntry(BackendMealEntry entry) {
		picMealName = entry.getMealName();
		mealTranslation = entry.getMealTranslation();
		picUrl = entry.getPicUrl();
	}
	
	public MealEntry(String recMealName, String picMealName, String picUrl,
			String mealTranslation) {
		super();
		this.recMealName = recMealName;
		this.picMealName = picMealName;
		this.picUrl = picUrl;
		this.mealTranslation = mealTranslation;
	}

	public String getRecMealName() {
		return recMealName;
	}
	public void setRecMealName(String recMealName) {
		this.recMealName = recMealName;
	}
	public String getPicMealName() {
		return picMealName;
	}
	public void setPicMealName(String picMealName) {
		this.picMealName = picMealName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getMealTranslation() {
		return mealTranslation;
	}
	public void setMealTranslation(String mealTranslation) {
		this.mealTranslation = mealTranslation;
	}
	
	
	
}
