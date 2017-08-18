package com.wikipedi.wikipedigo.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by E460 on 16/08/2017.
 */

public class BaseResponse<T> {

	@SerializedName("status")
	@Expose
	String status;

	@SerializedName("message")
	@Expose
	String message;

	@SerializedName("data")
	@Expose
	T value;

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public T getValue() {
		return value;
	}
}
