package com.ronxuntech.component.spsm.util.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.drools.compiler.lang.dsl.DSLMapParser.mapping_file_return;

import com.sun.jna.Function.PostCallRead;

import okhttp3.Response;

public class Test {
	
	public static void main(String[] args) {
//		String url="http://www.seedchina.com.cn/defaultInfoList.aspx?Id=5&isSubType=yes";
//		Map<String,String> params =new HashMap<>();
//		params.put("__EVENTTARGET","GridView1$ctl31$LinkButtonNextPage");  
//		params.put("__EVENTARGUMENT","");  
//		params.put("__VIEWSTATE","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");  
//		params.put("__EVENTVALIDATION","/wEWBQKfpPTHAQKvm7PxBQKw4u2SCAK/6trBDgLc5vbbDxpVjpXRkBiFfkc/bqQRF2z2vm/psez4ztRNCFePb63/");
		Map<String,String> params =new HashMap<>();
	/*	String url="http://182.140.197.104:8180/logincheck";
		
		
		params.put("username", "510100:a");
		params.put("password","21218cca77804d2ba1922c33e0151105");*/
		
		String url="http://www.cgris.net/Gis%E4%BD%9C%E7%89%A9%E5%88%86%E5%B8%83.asp";
		
		
		
		String temp="数据库_作物中文名";
		String databaseName="";
		try {
			databaseName = new String(temp.getBytes( "gb2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		
	 	String temp1 = "满江红    ";
	 	
	 	String dataValue="";
		try {
			dataValue = new String("%C2%FA%BD%AD%BA%EC++++".getBytes( "gb2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  

	 	String submit ="B1";
	 	
	 	String submitValue="";
	 	try {
	 		submitValue = new String("%C8%B7%C8%CF".getBytes( "gb2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		
	 	
	 	params.put(databaseName, dataValue);
		params.put("B1", submitValue);
		
		HttpListener httpListener =new HttpListener() {
			
			@Override
			public void onSuccess(Response response) {
				try {
					System.out.println(response.body().string());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub
				
			}
		};
		HttpClient client =HttpClient.getInstance();
		client.post(url, params, httpListener);
	}
	

}