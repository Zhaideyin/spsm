package api.ronxuntech.controller.spsm.webinfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ronxuntech.entity.Page;
import com.ronxuntech.service.spsm.webinfo.WebInfoManager;
import com.ronxuntech.util.PageData;

import api.ronxuntech.controller.BaseController;

/** 
 * 说明：网站抓取信息配置
 * 创建人：Liuxh
 * 创建时间：2016-12-26
 *
 */
@RestController
@RequestMapping(value = "/api/webinfo")
public class WebInfoApiController extends BaseController {

	@Resource(name="webinfoService")
	private WebInfoManager webinfoService;
	
	@RequestMapping(value="list")
	public Map list(int page,int count) throws Exception {
		Page pg = new Page(page,count);
		pg.setPd(this.getPageData());

		List<PageData> varList = webinfoService.list(pg);	//列出WEBINFO列表,有分页
			return this.formatRtnMsg(varList, "success",String.valueOf(pg.getTotalPage()));
	}

	//根据id获取单条信息
    @RequestMapping(value = "info")
    public Object info() throws Exception {
		PageData pd = this.getPageData();
		return webinfoService.findById(pd);
    }
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
