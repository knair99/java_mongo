package com.nimma.jersey.server;


import org.json.JSONArray;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.nimma.jersey.server.clientdatum.ClientDatum;

import java.util.ArrayList;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;


/*
JSON for sales schema
{
   "id" : 1,
   "item_name" : "Every Man Jack",
   "dept_name" : "Personal Care",
   "discount" : "50% off"
}

*/


public class MongoServerSide {

	
	//C- Method to create static Data
	public void CreateServerData(int id, String dept_name, String discount, String item_name){
		
		//First create valid json per schema
		ClientDatum cd = new ClientDatum();
		cd.setid(id);
		cd.setdeptname(dept_name);
		cd.setitemname(item_name);
		cd.setdiscount(discount);
		
		String json_data = cd.toString();
		System.out.println(json_data);
		Boolean valid = isMyJSONValid(json_data);
		System.out.println(valid);
		
		//Save to db
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		
		MongoDatabase db = mongoClient.getDatabase( "server_sales" );
		
		Document doc = Document.parse(json_data);
		db.getCollection("sales_collections").insertOne(doc);
		
		mongoClient.close();
		
		
	}
	
	//R- Method to get client Data for publishing sales info to server
	public ArrayList<String> FindByDeptId(int dept_id){
		
		ArrayList<String> json_items = new ArrayList<String>();
		
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		
		MongoDatabase db = mongoClient.getDatabase( "server_sales" );
		
		FindIterable<Document> collection = db.getCollection("sales_collections").find(new Document("id", dept_id));
		
		//Just like client side, iterate through each document
		collection.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        String record = document.toJson();
		        
		        JSONObject jsonObj;
				try {
					jsonObj = new JSONObject(record);
					jsonObj.remove("_id");
			        record = jsonObj.toString();
			        System.out.println(record);
			        
			        json_items.add(record);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		    }
		});
		
		mongoClient.close();
		
		return json_items;
		
	}
	
	//U-Method to update client Data with discount or item updates
	public void UpdateServerData(String item_name, String new_discount){
		
	}
	
	//D- Method to remove an item from collections
	public void DeleteServerData(String item_name){
		
	}
	

	public Boolean isMyJSONValid(String test){
		try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        // edited, to include @Arthur's comment
	        // e.g. in case JSONArray is valid as well...
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
	
}

