package edu.umich.eecs441.foodie.database;

import edu.umich.eecs441.foodie.BackendMealEntry;
import edu.umich.eecs441.foodie.DynamoDBOperation;


/**
 * This class is to make connections with DynamoDB back-end
 * @author Shaoxiang Su
 *
 */
public class MealEntry {
	
	private String recMealName;
	private String picMealName;
	private String picUrl;
	private String mealTranslation;
	
	
	public MealEntry(BackendMealEntry entry) {
		recMealName = entry.getMealName();
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