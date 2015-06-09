package com.ariisens.nearsens.map;

import org.json.JSONArray;

public class MyOffersJoinWithPlace {
	
	private JSONArray responseJsonObject;
	private String titolo;
	private String id;
	
	public MyOffersJoinWithPlace() {
	
	}

	public JSONArray getResponseJsonObject() {
		return responseJsonObject;
	}

	public void setResponseJsonObject(JSONArray responseJsonObject) {
		this.responseJsonObject = responseJsonObject;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
