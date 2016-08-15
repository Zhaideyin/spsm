package com.ronxuntech.aop;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public abstract class Validator implements Interceptor{
	
	protected abstract boolean validate(HttpServletRequest request);

	protected abstract Map<String,Object> handleError();
}
