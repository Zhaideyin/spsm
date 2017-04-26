package api.ronxuntech.validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ronxuntech.aop.Validator;
import com.ronxuntech.util.ApiUtil;

public class MemberLoginValidator extends Validator{

	Map<String,Object> data=new HashMap<String,Object>();

	protected boolean validate(HttpServletRequest request) {
		String name=request.getParameter("USERNAME");
		String password=request.getParameter("PASSWORD");
		if(null==name||"".equalsIgnoreCase(name)){
			data=ApiUtil.newInstance().formatErrMsg(4001, "用户名不能为空");
			return false;
		}
		if(null==password||"".equalsIgnoreCase(password)){
			data=ApiUtil.newInstance().formatErrMsg(4002, "用户密码不能为空");
			return false;
		}
		return true;
	}

	protected Map<String,Object> handleError() {
		
		return data;
	}
}
