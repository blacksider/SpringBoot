package com.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseErrorData {
	@JsonProperty("ErrorMsg")
	private String errorMsg;

	public ResponseErrorData() {
	}

	public ResponseErrorData(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
