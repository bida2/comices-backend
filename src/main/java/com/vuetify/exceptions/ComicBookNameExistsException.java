package com.vuetify.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ComicBookNameExistsException extends DataIntegrityViolationException {

	public ComicBookNameExistsException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
