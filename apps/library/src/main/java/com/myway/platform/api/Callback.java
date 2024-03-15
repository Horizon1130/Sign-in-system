package com.myway.platform.api;

public interface Callback {

	public void success(ReturnResult result);

	public void error(String errorMsg);

}
