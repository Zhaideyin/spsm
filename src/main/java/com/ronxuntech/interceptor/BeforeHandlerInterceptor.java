package com.ronxuntech.interceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ronxuntech.aop.Before;
import com.ronxuntech.aop.Interceptor;
import com.ronxuntech.aop.Validator;

import net.sf.json.JSONObject;

/**
 * 验证过滤器，注解方式
 * @author train
 *
 */
public class BeforeHandlerInterceptor extends HandlerInterceptorAdapter {
    
    @SuppressWarnings("unchecked")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
        	Before before = ((HandlerMethod) handler).getMethodAnnotation(Before.class);
        	
           if(before == null || before.value()==null)
                return true;
            else{
            	Class<? extends Interceptor>[] clazzList=before.value();
            	boolean hasError=false;
            	Map<String,Object> errMsg=new HashMap<String,Object>();
            	System.out.println(clazzList.length);
            	for(int k=0;k<clazzList.length;k++){
            		Class<?> clazz=clazzList[k];
            		Class<?> superClazz=clazz.getSuperclass();

                	if(superClazz==Validator.class){
                		Object invoke = clazz.getConstructor(new Class[]{}).newInstance(new Object[]{});
                    	Method validateMethod = clazz.getDeclaredMethod("validate",HttpServletRequest.class);
                    	validateMethod.setAccessible(true); 
                    	boolean b=(boolean) validateMethod.invoke(invoke,request);
						//如果validate返回为false，则执行HandleError方法
                    	if(!b){
                    		Method errMethod = clazz.getDeclaredMethod("handleError");
                    		errMethod.setAccessible(true); 
    						errMsg=(Map<String, Object>) errMethod.invoke(invoke);
    						hasError=true;
                    		break;
                    	}
                	}
            	}
            	if(hasError){
            		JSONObject jsonObject = JSONObject.fromObject(errMsg);
            		response.setHeader("content-type", "application/json;charset=UTF-8");
            		PrintWriter out=response.getWriter();
            		out.print(jsonObject);
            		return false;
            	}
            	
            	return true;
            }
        }
        else
            return true;   
     }
}
