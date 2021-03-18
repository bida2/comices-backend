package com.vuetify.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidation {
	
	// Format should be like Batman #32
	public static Boolean isComicNameValid(String name) {
		Pattern pattern = Pattern.compile("^[A-Za-z ]+[#]{1,1}[0-9]{1,}$");
	    Matcher matcher = pattern.matcher(name);
	    return matcher.find();
	}
	
	// Format should contain only English letters - no special symbols or numbers allowed!
	public static Boolean containsOnlyLetters(String field) {
			Pattern pattern = Pattern.compile("^[A-Za-z ]+$");
		    Matcher matcher = pattern.matcher(field);
		    return matcher.find();
	}
	
		// Format should contain only integers or float numbers!
		public static Boolean containsOnlyIntegerOrFloat(Number number) {
				Pattern pattern = Pattern.compile("^(?:\\d{1,3}(?:\\.\\d{1,3})?|1)$");
			    Matcher matcher = pattern.matcher(number.toString());
			    return matcher.find();
		}
		
		// Format must be a valid URL adress
		public static Boolean isValidUrl(String url) {
				Pattern pattern = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
			    Matcher matcher = pattern.matcher(url);
			    return matcher.find();
		}
		
		
		// Format must accept a integer number between 1 and 5
		public static Boolean isValidReviewScore(int score) {
			Pattern pattern = Pattern.compile("^[1-5]$");
		    Matcher matcher = pattern.matcher(String.valueOf(score));
		    return matcher.find();
		}
		
		// Format must accept a valid e-mail adress only (ex: myacc123@gmail.com )
		public static Boolean isValidEmail(String email) {
			Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
		    Matcher matcher = pattern.matcher(email);
		    return matcher.find();
		}
		
		// Format of field must not be empty or null
		public static Boolean isNotEmptyOrNull(Object fieldValue) {
			if (!fieldValue.toString().equals("null") && !fieldValue.toString().equals("undefined") && !fieldValue.toString().isEmpty() && fieldValue != null) 
				return true;
			else return false;
		}
		
		// Format of field must contain only letters (English alphabet) and numbers with digits 0-9
		public static Boolean containsOnlyLettersAndNumbers(String fieldValue) {
			Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
		    Matcher matcher = pattern.matcher(fieldValue);
		    return matcher.find();
		}
	

	
	
}
