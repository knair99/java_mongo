package com.nimma.jersey.iot;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nimma.jersey.server.iotdatum.IOTDatum;

public class RegisterIOT {

	public static void main(String[] args){
		
		RegisterIOTWithServer(2, 2, "5129094874");
		
	}
	
	public static void RegisterIOTWithServer(int id, int device_id, String phone){
		IOTDatum idata = new IOTDatum();
		
		idata.setdeviceid(device_id);
		idata.setid(id);
		idata.setphonenumber(phone);
		
		String json_iot = idata.toString();
		
		//Push this to server
		//Setup client
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/com.nimma.jersey.server/rest");
		
		//Handle response
		System.out.println("JSON from client : " + json_iot );
		Response r2 = target.path("/register").request(MediaType.APPLICATION_JSON_TYPE).post(
				Entity.entity(json_iot, MediaType.APPLICATION_JSON_TYPE));

		//Output
		System.out.println("Server response:");
		System.out.println(r2.readEntity(String.class));
		System.out.println("---------------------------");
		
	}
	
	
}
