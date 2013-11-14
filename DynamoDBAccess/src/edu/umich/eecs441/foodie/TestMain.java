package edu.umich.eecs441.foodie;

import java.io.IOException;

public class TestMain {

	public static void main(String[] args) {
		
		try {
			DynamoDBOperation backendOperation = DynamoDBOperation.getInstance();
//			int result = backendOperation.clientCreate("aaabfffdsadfadad", "test");
			
			int result = backendOperation.clientLogin("ssx", "Su920509");
			
			System.out.print(result);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
