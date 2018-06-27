package com.jpmc.poc.notify.domain;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * Notify domain object.
 *
 */
@Component
public class Notify implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5445830199767055035L;
	
	private String arg;
	private String id;
	private String timestamp;
	private Category category;
	
	
	public Notify(){
		
	}
	
	public String getArg(){
		return arg;
	}
	
	public void setArg(String arg){
		this.arg = arg;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return getId() + ":" + getArg() + ":" + getTimestamp();
	}
	
	public enum Category{
		user("user"),
		group("group");
		private String name;
		
		private Category(String s){
			name = s;
		}
	}
	

}
