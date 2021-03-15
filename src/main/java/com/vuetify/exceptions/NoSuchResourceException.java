package com.vuetify.exceptions;

public class NoSuchResourceException extends Exception{
	
	public String resourceName;
	
	public NoSuchResourceException(String resourceName) {
		this.resourceName = resourceName;
	}
}
