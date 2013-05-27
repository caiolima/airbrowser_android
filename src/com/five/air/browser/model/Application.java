package com.five.air.browser.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Application {

	private String name,description,icon,url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static Application createFromJSON(String serverIP,JSONObject json){
		
		Application app=new Application();
		try {
			String url="http://"+serverIP+"/"+json.getString("url");
			app.setName(json.getString("name"));
			app.setDescription(json.getString("description"));
			app.setIcon(url+"/icon.png");
			app.setUrl(url);
			
			return app;
		} catch (JSONException e) {
			return null;
		}
		
		
	}
	
}
