package com.vuetify.utils;

import java.util.Arrays;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTDecoder {
	
	// Possible values for group are -> "admins" and "users"
 	public boolean decodeJwt(String token, String group) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			String[] groups = jwt.getClaim("groups").asArray(String.class);
			if(Arrays.stream(groups).anyMatch(group::equals)) {
				return true;
			} else {
				return false;
			}
			
		} catch(JWTDecodeException exception) {
			// Signifies token is invalid or missing
			return false;
		}
	}
	
	
}
