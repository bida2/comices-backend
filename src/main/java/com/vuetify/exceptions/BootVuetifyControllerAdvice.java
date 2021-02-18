package com.vuetify.exceptions;

import java.nio.file.InvalidPathException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class BootVuetifyControllerAdvice {
	 @ExceptionHandler(NumberFormatException.class)
     @ResponseStatus(HttpStatus.BAD_REQUEST)
     @ResponseBody
     public ResponseEntity<String> handleNumberFormatException(NumberFormatException e) {
         // the details which field binding went wrong are in the 
         // exception object. 
         return new ResponseEntity<String>("Something went wrong! Check your data's format and try again!", HttpStatus.BAD_REQUEST);
     }
	 
	 @ExceptionHandler(ComicBookNameExistsException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public ResponseEntity<String> handleComicBookNameExistsException(ComicBookNameExistsException e) {
		 return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	 }
	 
	 @ExceptionHandler(NullPointerException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
		 return new ResponseEntity<String>("Needed data was not found! Check your entered information and try again!", HttpStatus.BAD_REQUEST);
	 }
	 
	 @ExceptionHandler(MissingServletRequestParameterException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public ResponseEntity<String> handleMissingServletParameterException(MissingServletRequestParameterException e) {
		 return new ResponseEntity<String>("Something went wrong with submitting the data - check your input and try again!", HttpStatus.BAD_REQUEST);
	 }
	 
	 @ExceptionHandler(NotLoggedInException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public ResponseEntity<String> handleNotLoggedInException(NotLoggedInException e) {
		 return new ResponseEntity<String>("You need to login in order to do this action!", HttpStatus.FORBIDDEN);
	 }
	 
	 @ExceptionHandler(MethodArgumentTypeMismatchException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		 return new ResponseEntity<String>("Format of your entered data is incorrect! Check each field's data and try again!", HttpStatus.BAD_REQUEST);
	 }
	 
	 @ExceptionHandler(StorageException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public ResponseEntity<String> handleStorageException(StorageException e) {
		 return new ResponseEntity<String>("Something went wrong while uploading the file! Check your file and try again!", HttpStatus.BAD_REQUEST);
	 }
	 
	 @ExceptionHandler(InvalidPathException.class)
	 @ResponseStatus(HttpStatus.NOT_FOUND)
	 @ResponseBody
	 public ResponseEntity<String> handleInvalidPathException(InvalidPathException e) {
		 return new ResponseEntity<String>("Cannot find file with that name!", HttpStatus.NOT_FOUND);
	 }
	 
	 @ExceptionHandler(IllegalArgumentException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		 return new ResponseEntity<String>("Expected data of a different type!", HttpStatus.BAD_REQUEST);
	 }
}
