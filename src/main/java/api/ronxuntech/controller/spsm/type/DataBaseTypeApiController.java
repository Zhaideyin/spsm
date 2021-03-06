package api.ronxuntech.controller.spsm.type;

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
import com.ronxuntech.service.spsm.databasetype.DataBaseTypeManager;
import com.ronxuntech.util.PageData;

import api.ronxuntech.controller.BaseController;

/** 
 * 说明：数据类型
 * 创建人：Liuxh
 * 创建时间：2016-10-08
 *
 */
@RestController
@RequestMapping(value = "/api/databasetype")
public class DataBaseTypeApiController extends BaseController {

	@Resource(name="databasetypeService")
	private DataBaseTypeManager databasetypeService;
	
	@RequestMapping(value="list")
	public Map list(int page,int count) throws Exception {
		Page pg = new Page(page,count);
		pg.setPd(this.getPageData());

		List<PageData> varList = databasetypeService.list(pg);	//列出Type列表,有分页
			return this.formatRtnMsg(varList, "success",String.valueOf(pg.getTotalPage()));
	}

	//根据id获取单条信息
    @RequestMapping(value = "info")
    public Object info() throws Exception {
		PageData pd = this.getPageData();
		return databasetypeService.findById(pd);
    }
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
