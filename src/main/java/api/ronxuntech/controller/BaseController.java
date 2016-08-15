package api.ronxuntech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.UuidUtil;


public class BaseController {

	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	//获取token中的数据
	public Object getTokenData(){
		return getRequest().getAttribute("tokenData");
	}
	/** PageData对象
	 * @return
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	/**得到分页列表的信息
	 * @return
	 */
	public Page getPage(){
		return new Page();
	}
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	public Map<String,Object> formatSuccessMsg(Object object) {
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("data", object);
		if(null!=getRequest().getAttribute("token")) 
			data.put("token", getRequest().getAttribute("token"));
		return data;
	}

	public Map<String,Object> formatErrMsg(int errcode,String errmsg) {
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("errcode", errcode);
		data.put("errmsg", errmsg);
		return data;
	}

	protected Map formatRtnMsg(Object obj, String message) {
		return formatRtnMsg(obj, message, null);
	}

	protected Map formatRtnMsg(Object obj, String message, String totalPage) {
		try {
			Map rtnmap = new HashMap();

			if (obj instanceof Map || obj instanceof List || obj instanceof String) {
				rtnmap.put("data", obj);
			}
			rtnmap.put("rtncode", 1);
			if (totalPage != null) {
				rtnmap.put("totalPage", totalPage);
			}
			rtnmap.put("message", message);
			return rtnmap;
		} catch (Throwable e) {
			e.printStackTrace();
			return formatErrMsg(-1, "error");
		}

	}
}
