package com.ronxuntech.controller.spsm.spider;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.ronxuntech.component.AjaxCrawler;
import com.ronxuntech.component.WebInfo;
import com.ronxuntech.component.spsm.BaseCrawler;
import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.type.TypeManager;
import com.ronxuntech.util.AppUtil;
import com.ronxuntech.util.Jurisdiction;
import com.ronxuntech.util.ObjectExcelView;
import com.ronxuntech.util.PageData;

import us.codecraft.webmagic.Spider;

/** 
 * 说明：数据爬取
 * 创建人：Liuxh
 * 创建时间：2016-10-08
 */
@Controller
@RequestMapping(value="/spider")
public class SpiderController extends BaseController {
	
	String menuUrl = "spider/list.do"; //菜单地址(权限用)
	@Resource(name="spiderService")
	private SpiderManager spiderService;
	
	@Resource(name="typeService")
	private TypeManager typeService;
	
	private BaseCrawler crawler=BaseCrawler.getInstance();
	private AjaxCrawler ajaxCrawler=AjaxCrawler.getInstance();
	/**
	 * 跳转到爬取页面，取出所有数据类型
	 */
	@RequestMapping(value="/gostart")
	public ModelAndView goStart() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"开启爬取");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		//查询所有的数据类型
		List<PageData> list =typeService.listAll(pd);
		mv.addObject("list", list);
//		mv.addObject("msg","success");
		mv.setViewName("spsm/spider/spider_start");
		return mv;
	}
	//开启爬取
	@RequestMapping(value = "/start")
	public void start(PrintWriter out,@RequestParam(value="seedUrl") String seedUrl,@RequestParam(value="typeName") String typeId) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "开启爬取");
		PageData pd = new PageData();
		//调用开启方法，传递种子和数据库的类型
//		crawler.start(seedUrl,typeId);
		WebInfo web=WebInfo.init(seedUrl, typeId);
		if(web.getPageMethod().equals("get")){
			crawler.start(web);
		}else if(web.getPageMethod().equals("ajax")){
			ajaxCrawler.start(web);
		}
		out.write("success");
		out.close();
	}
	
	//停止
		@RequestMapping(value = "/stop")
		public void stop(PrintWriter out) throws Exception {
//			if(crawler.getS().getStatus()!=Spider.Status.Stopped){
//				crawler.getS().stop();
//				Thread t=new Thread();
//				t.sleep(10000);
//				System.out.println("stop: *******************   after "+crawler.getS().getStatus());
//			}
			
//			crawler.down();
			out.write("success");
			out.close();
		}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增SPIDER");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SPIDER_ID", this.get32UUID());	//主键
		spiderService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
	
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除SPIDER");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		spiderService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改SPIDER");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		spiderService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表SPIDER");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = spiderService.list(page);	//列出SPIDER列表
		mv.setViewName("spsm/spider/spider_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("spsm/spider/spider_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = spiderService.findById(pd);	//根据ID读取
		mv.setViewName("spsm/spider/spider_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除SPIDER");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			spiderService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出SPIDER到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("标题");	//1
		titles.add("作者");	//2
		titles.add("摘要");	//3
		titles.add("内容");	//4
		titles.add("类型");	//5
		titles.add("发布时间");	//6
		titles.add("爬取时间");	//7
		titles.add("关键字");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = spiderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TITLE"));	//1
			vpd.put("var2", varOList.get(i).getString("AUTHOR"));	//2
			vpd.put("var3", varOList.get(i).getString("ABSTRACT"));	//3
			vpd.put("var4", varOList.get(i).getString("CONTENT"));	//4
			vpd.put("var5", varOList.get(i).get("TYPE").toString());	//5
			vpd.put("var6", varOList.get(i).getString("PUBLISH_TIME"));	//6
			vpd.put("var7", varOList.get(i).getString("CREATE_TIME"));	//7
			vpd.put("var8", varOList.get(i).getString("KEYWORDS"));	//8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
