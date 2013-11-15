package edu.umich.eecs441.foodie;

import java.io.IOException;
import java.util.Vector;

public class TestMain {

	public static void main(String[] args) {
		
		try {
			DynamoDBOperation backendOperation = DynamoDBOperation.getInstance();
//			int result = backendOperation.clientCreate("aaabfffdsadfadad", "test");
			
			/*int result = backendOperation.clientLogin("ssx", "Su920509");
			
			System.out.print(result);*/
	/*		
			int result = backendOperation.checkMeal(1, "好吃的");
			System.out.println(result);
			*/
			for (int i = 0; i < 5; i++) {
				backendOperation.markMeal(0, "红烧排骨" + String.valueOf(i), "hongshaopaigu", "fdafas");
			}
			
			
//			backendOperation.cancelMeal(1, "fda");
			
			Vector<BackendMealEntry> resultList = backendOperation.getMeal(0);
			
			for (BackendMealEntry entry : resultList) {
				System.out.println(entry.toString());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
