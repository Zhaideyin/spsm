package com.ronxuntech.controller.base;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.ronxuntech.entity.Page;
import com.ronxuntech.util.Logger;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.UuidUtil;

/**
 * @author FH Q313596790
 * 修改时间：2015、12、11
 */
public class BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	
	/** new PageData对象
	 * @return
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**得到ModelAndView
	 * @return
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**得到分页列表的信息
	 * @return
	 */
	public Page getPage(){
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	
	protected Map formatRtnMsg(Object obj, String message) {
		try {
			Map rtnmap = new HashMap();

			if(obj instanceof Map || obj instanceof List || obj instanceof String) {
				rtnmap.put("data", obj);
			} 
			rtnmap.put("rtncode", 0);
			rtnmap.put("message", "success");
			return rtnmap;
		} catch (Throwable e) {
			e.printStackTrace();
			return formatErrMsg(-1, "error");
		}
	}

	public Map formatErrMsg(int errcode, String message) {
		Map rtnmap = new HashMap();
		rtnmap.put("rtncode", errcode);
		rtnmap.put("message", message);
		return rtnmap;
	}
	
}
