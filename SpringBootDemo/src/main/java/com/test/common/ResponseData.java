package com.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseData {
	@JsonProperty("ErrorCode")
	private int errorcode;

	@JsonProperty("ErrorData")
	private ResponseErrorData data;

	public ResponseData() {
	}

	public ResponseData(int errorcode, ResponseErrorData data) {
		this.errorcode = errorcode;
		this.data = data;
	}

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public ResponseErrorData getData() {
		return data;
	}

	public void setData(ResponseErrorData data) {
		this.data = data;
	}

}
