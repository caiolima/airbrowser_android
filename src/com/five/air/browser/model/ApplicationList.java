package com.five.air.browser.model;

import java.util.Vector;

public class ApplicationList {

	private Vector<Application> applications=new Vector<Application>();

	public Vector<Application> getApplications() {
		return applications;
	}

	public void setApplications(Vector<Application> applications) {
		this.applications = applications;
	}
	
	public void add(Application app){
		applications.add(app);
	}
	
	public Application get(int pos){
		return applications.get(pos);
	}
	
	public int size(){
		return applications.size();
	}
	
}
