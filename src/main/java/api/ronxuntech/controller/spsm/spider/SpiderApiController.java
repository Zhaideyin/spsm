package api.ronxuntech.controller.spsm.spider;

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
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.util.PageData;

import api.ronxuntech.controller.BaseController;

/**
 * 说明：数据爬取 创建人：Liuxh 创建时间：2016-10-08
 *
 */
@RestController
@RequestMapping(value = "/api/spider")
public class SpiderApiController extends BaseController {

	@Resource(name = "spiderService")
	private SpiderManager spiderService;

	@RequestMapping(value = "list")
	public Map list(int page, int count) throws Exception {
		Page pg = new Page(page, count);
		pg.setPd(this.getPageData());

		List<PageData> varList = spiderService.list(pg); // 列出SPIDER列表,有分页
		return this.formatRtnMsg(varList, "success", String.valueOf(pg.getTotalPage()));
	}

	// 根据id获取单条信息
	@RequestMapping(value = "info")
	public Object info() throws Exception {
		PageData pd = this.getPageData();
		return spiderService.findById(pd);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
	}
}
