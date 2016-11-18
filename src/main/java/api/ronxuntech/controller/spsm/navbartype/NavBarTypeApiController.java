package api.ronxuntech.controller.spsm.navbartype;

import api.ronxuntech.controller.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ronxuntech.service.spsm.navbartype.NavBarTypeManager;

/** 
 * 说明：导航栏
 * 创建人：Liuxh
 * 创建时间：2016-11-11
 *
 */
@RestController
@RequestMapping(value = "/api/navbartype")
public class NavBarTypeApiController extends BaseController {

	@Resource(name="navbartypeService")
	private NavBarTypeManager navbartypeService;
	
	@RequestMapping(value="list")
	public Map list(int page,int count) throws Exception {
		Page pg = new Page(page,count);
		pg.setPd(this.getPageData());

		List<PageData> varList = navbartypeService.list(pg);	//列出NavBarType列表,有分页
			return this.formatRtnMsg(varList, "success",String.valueOf(pg.getTotalPage()));
	}

	//根据id获取单条信息
    @RequestMapping(value = "info")
    public Object info() throws Exception {
		PageData pd = this.getPageData();
		return navbartypeService.findById(pd);
    }
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
