package com.ronxuntech.util;

import java.util.HashMap;
import java.util.Map;

public class ApiUtil {
	
	public static final ApiUtil single=new ApiUtil();
	
	private ApiUtil() {

	}
	public static ApiUtil newInstance(){
		return single;
	}
	
	public Map<String,Object> formatErrMsg(int errcode,String errmsg) {
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("errcode", errcode);
		data.put("errmsg", errmsg);
		return data;
	}
}
