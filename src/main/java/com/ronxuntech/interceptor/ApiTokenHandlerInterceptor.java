package com.ronxuntech.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ronxuntech.util.Const;
import com.ronxuntech.util.JwtsUtil;


public class ApiTokenHandlerInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		if(path.matches(Const.NO_APITOKEN_PATH)){
			return true;
		}else{
			String token=request.getParameter("accessToken");
			Map<String,Object> data=JwtsUtil.newInstance().validateToken(Const.STORE_KEY,Const.KEY_STRING,token);
			if(null==data){
				response.setStatus(HttpStatus.SC_FORBIDDEN);
				return false;
			}

			long now=System.currentTimeMillis();
			long expiresAt=(long) data.get("expiresAt");
			Map<String,Object> newToken=new HashMap<String,Object>();
			//如果还有1分钟过期则更新token
			if(expiresAt-now<=1000*60){
				newToken=JwtsUtil.newInstance().generateToken(Const.STORE_KEY, data, Const.KEY_STRING);
			}else{
				newToken.put("accessToken", token);
				newToken.put("expiresAt", expiresAt);
			}
			request.setAttribute("token", newToken);
			request.setAttribute("tokenData", data);
			return true;
		}
	}
	
}
