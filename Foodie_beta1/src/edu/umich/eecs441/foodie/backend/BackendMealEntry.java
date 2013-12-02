package edu.umich.eecs441.foodie.backend;

import java.util.Map;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

/**
 * This class is a simple javaBean used for track MARKRECORDTABLE
 * @author Shaoxiang Su
 *
 */
public class BackendMealEntry {
	
	private int clientId;
	private String mealName;
	private String mealTranslation;
	private String picUrl;
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getMealName() {
		return mealName;
	}
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}
	public String getMealTranslation() {
		return mealTranslation;
	}
	public void setMealTranslation(String mealTranslation) {
		this.mealTranslation = mealTranslation;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public BackendMealEntry(int clientId, String mealName,
			String mealTranslation, String picUrl) {
		super();
		this.clientId = clientId;
		this.mealName = mealName;
		this.mealTranslation = mealTranslation;
		this.picUrl = picUrl;
	}
	
	public BackendMealEntry(Map<String, AttributeValue> item) {
		clientId = Integer.valueOf(item.get("USER_ID").getN());
		mealName = item.get("MEAL_NAME").getS();
		mealTranslation = item.get("MEAL_TRANSLATION").getS();
		picUrl = item.get("PIC_URL").getS();
	}
	@Override
	public String toString() {
		return "BackendMealEntry [clientId=" + clientId + ", mealName="
				+ mealName + ", mealTranslation=" + mealTranslation
				+ ", picUrl=" + picUrl + "]";
	}
	
	
}
