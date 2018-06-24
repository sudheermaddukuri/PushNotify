package com.ca.notify.domain;

/**
 * 
 * App error object
 *
 */
public class Response {
	private Boolean errNErr;
	private String message;
	public Boolean getErrNErr() {
		return errNErr;
	}
	public void setErrNErr(Boolean errNErr) {
		this.errNErr = errNErr;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
