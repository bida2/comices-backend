package com.vuetify.singletons;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

public class SearchTags {
	// static variable single_instance of type Singleton 
    private static SearchTags single_instance = null; 
  
    // Contains the search term as the key
    // and the amount of times it was looked up as the value
    @NotEmpty
    private Map<String, Integer> tags = new HashMap<String, Integer>();
    
  
    // private constructor restricted to this class itself 
    private SearchTags() 
    {} 
    
    private SearchTags(Map<String,Integer> tags) {
    	this.tags = tags;
    }
  
    // static method to create instance of Singleton class 
    public static SearchTags getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new SearchTags(); 
  
        return single_instance; 
    }
    
    public static SearchTags getInstance(Map<String, Integer> tags) {
    	if (single_instance == null) 
            single_instance = new SearchTags(tags); 
  
        return single_instance; 
    }

	public Map<String, Integer> getTags() {
		return tags;
	}

	public void setTags(Map<String, Integer> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "SearchTags [tags=" + tags + "]";
	} 
}
