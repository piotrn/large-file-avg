package com.example.largefileavg.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.largefileavg.model.RestException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<RestException> handleException(Exception e) {
		HttpStatus httpStatus;
		if (e instanceof ServletRequestBindingException) {
			httpStatus = (HttpStatus) ((ServletRequestBindingException)e).getStatusCode();
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(new RestException(e.getLocalizedMessage()), httpStatus);
	}
}
