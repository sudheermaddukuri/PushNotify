package com.jpmc.poc.notify.exception;

public class PushNotificationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9059737684403188767L;

	private String errCode;
	private String errMsg;
	
	public PushNotificationException(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


}
