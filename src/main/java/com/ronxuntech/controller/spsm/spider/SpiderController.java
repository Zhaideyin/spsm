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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ronxuntech.component.spsm.AjaxCrawler;
import com.ronxuntech.component.spsm.BaseCrawler;
import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.ReadXML;
import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.spsm.databasetype.DataBaseTypeManager;
import com.ronxuntech.service.spsm.listtype.ListTypeManager;
import com.ronxuntech.service.spsm.navbartype.NavBarTypeManager;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.sublisttype.SubListTypeManager;
import com.ronxuntech.util.AppUtil;
import com.ronxuntech.util.Jurisdiction;
import com.ronxuntech.util.ObjectExcelView;
import com.ronxuntech.util.PageData;

import net.sf.json.JSONSerializer;

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
	
	@Resource(name="databasetypeService")
	private DataBaseTypeManager  databasetypeService;
	@Resource(name="navbartypeService")
	private NavBarTypeManager  navbartypeService;

	@Resource(name="listtypeService")
	private ListTypeManager  listtypeService;
	@Resource(name="sublisttypeService")
	private SubListTypeManager sublisttypeService;
	
	private BaseCrawler crawler=BaseCrawler.getInstance();
	private AjaxCrawler ajaxCrawler=AjaxCrawler.getInstance();
	
	@Autowired
	private  HttpServletRequest request;
	/**
	 * 跳转到爬取页面，取出所有数据类型
	 */
	@RequestMapping(value="/gostart")
	public ModelAndView goStart() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"开启爬取");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		//查询所有的数据类型
		List<PageData> list =databasetypeService.listAll(pd);
		mv.addObject("list", list);
		ReadXML rxml=new ReadXML();
		
		List<HashMap> listmap =rxml.ResolveXml();
		mv.addObject("urlList",listmap);
//		mv.addObject("msg","success");
		mv.setViewName("spsm/spider/spider_start");
		return mv;
	}
	
	@RequestMapping(value="/chooseNextSelect")
	public void chooseNextSelect(HttpServletResponse response) throws Exception{
		List<PageData> list =new 	ArrayList<>();
		PageData pd = this.getPageData();
		response.setCharacterEncoding("utf-8");
		PrintWriter out =response.getWriter();
		
		//判断下是第几个， 则执行不同的service.
		String flag =pd.get("flag").toString();
		System.out.println("flag:"+flag);
		if(flag.equals("1")){
			list =navbartypeService.listFindByDatabaseID(pd);
			
		}else if(flag.equals("2")){
			list = listtypeService.listFindByNavbarID(pd);
		}else if(flag.equals("3")){
			list=sublisttypeService.listFindByListID(pd);
		}
		out.print(JSONSerializer.toJSON(list));
	}
	
	//开启爬取
	@RequestMapping(value = "/start")
	public void start(PrintWriter out,@RequestParam(value="seedUrl") String seedUrl) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "开启爬取");
		//将前台传递来的typeID 非空的组合在一起。
		StringBuffer dataType=new StringBuffer();
		String SUBLISTTYPE_ID=request.getParameter("SUBLISTTYPE_ID");
		String LISTTYPE_ID=request.getParameter("LISTTYPE_ID");
		String NAVBARTYPE_ID=request.getParameter("NAVBARTYPE_ID");
		String DATABASETYPE_ID= request.getParameter("DATABASETYPE_ID");
		if(SUBLISTTYPE_ID!="" && null!=SUBLISTTYPE_ID){
			dataType.append(SUBLISTTYPE_ID+",");
		}
		if(LISTTYPE_ID!="" && null!=LISTTYPE_ID){
			dataType.append(LISTTYPE_ID+",") ;
		}
		if(NAVBARTYPE_ID!=""  && null!=NAVBARTYPE_ID){
			dataType.append(NAVBARTYPE_ID+",");
		}
		if(DATABASETYPE_ID!="" && null!=DATABASETYPE_ID){
			dataType.append(DATABASETYPE_ID+",");
		}
		//调用开启方法，传递种子和数据库的类型
//		crawler.start(seedUrl,typeId);
		WebInfo web=WebInfo.init(seedUrl, dataType.toString());
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
		//获取前台传递的参数值
		String SUBLISTTYPE_ID = pd.getString("SUBLISTTYPE_ID");
		String LISTTYPE_ID = pd.getString("LISTTYPE_ID");          //关键词检索条件
		String NAVBARTYPE_ID = pd.getString("NAVBARTYPE_ID");
		String DATABASETYPE_ID = pd.getString("DATABASETYPE_ID");
		String keywords =pd.getString("keywords");
		
		//将不为空的id查询到他们的ｎａｍｅ．然后放入ｐｄ　在前台页面显示。、用于查询保存之前选择的类型
		if(null != SUBLISTTYPE_ID && !"".equals(SUBLISTTYPE_ID)){
			PageData pd1=new PageData();
			pd1 = sublisttypeService.findById(pd);
			pd.put("SUBLISTTYPENAME", pd1.getString("SUBLISTTYPENAME"));
			//用于回显四级下拉框
			pd1.put("LISTTYPE_ID", LISTTYPE_ID);
			List<PageData> sublisttype =sublisttypeService.listFindByListID(pd1);
			pd.put("sublisttype", sublisttype);
		}
		if(null != LISTTYPE_ID && !"".equals(LISTTYPE_ID)){
			PageData pd2=new PageData();
			pd2 = listtypeService.findById(pd);
			pd.put("LISTTYPENAME", pd2.getString("LISTTYPENAME"));
			//用于回显三级下拉框
			pd2.put("NAVBARTYPE_ID", NAVBARTYPE_ID);
			List<PageData> listtype =listtypeService.listFindByNavbarID(pd2);
			pd.put("listtype", listtype);
			
		}
		if(null != NAVBARTYPE_ID && !"".equals(NAVBARTYPE_ID)){
			PageData pd3=new PageData();
			pd3 = navbartypeService.findById(pd);
			pd.put("NAVBARTYPENAME", pd3.getString("NAVBARTYPENAME"));
			//用于回显二级下拉框
			pd3.put("DATABASETYPE_ID", DATABASETYPE_ID);
			List<PageData> navbartype =navbartypeService.listFindByDatabaseID(pd3);
			pd.put("navbartype", navbartype);
		}
		 if(null != DATABASETYPE_ID && !"".equals(DATABASETYPE_ID)){
			PageData pd4=new PageData();
			pd4 = databasetypeService.findById(pd);
			pd.put("DATABASETYPEENAME", pd4.getString("DATABASETYPENAME"));
		 }
		
		//从后往前判断， 看哪个类型不为空则设置关键字为该类型，然后查询该类型
		if(null != SUBLISTTYPE_ID && !"".equals(SUBLISTTYPE_ID)){
			pd.put("datatype", SUBLISTTYPE_ID.trim());
			//通过id 查询出菜单的名称，并保留在页面， 否在点击下一页，将清空数据
			pd.put("SUBLISTTYPE_ID", SUBLISTTYPE_ID);
		}else if(null != LISTTYPE_ID && !"".equals(LISTTYPE_ID)){
			pd.put("datatype", LISTTYPE_ID.trim());
			pd.put("LISTTYPE_ID", LISTTYPE_ID);
		}else if(null != NAVBARTYPE_ID && !"".equals(NAVBARTYPE_ID)){
			pd.put("datatype", NAVBARTYPE_ID.trim());
			pd.put("NAVBARTYPE_ID", NAVBARTYPE_ID);
		}else if(null != DATABASETYPE_ID && !"".equals(DATABASETYPE_ID)){
			pd.put("datatype", DATABASETYPE_ID.trim());
			pd.put("DATABASETYPE_ID", DATABASETYPE_ID);
		}
		
		//关键字
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = spiderService.list(page);	//列出SPIDER列表
		List<PageData> databasetype =databasetypeService.listAll(pd);
		pd.put("databasetype", databasetype);
//		mv.addObject("list", list);
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
		dataMap.put("titles", titles);
		List<PageData> varOList = spiderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TITLE"));	//1
			vpd.put("var2", varOList.get(i).getString("AUTHOR"));	//2
			vpd.put("var3", varOList.get(i).getString("ABSTRACT"));	//3
			vpd.put("var4", varOList.get(i).getString("CONTENT"));	//4
			String type;
			String SUBLISTTYPE_ID=varOList.get(i).get("SUBLISTTYPE_ID").toString();
			String LISTTYPE_ID=varOList.get(i).get("LISTTYPE_ID").toString();
			String NAVBARTYPE_ID=varOList.get(i).get("NAVBARTYPE_ID").toString();
			String DATABASETYPE_ID=varOList.get(i).get("DATABASETYPE_ID").toString();
			
			if(SUBLISTTYPE_ID!=null && !(LISTTYPE_ID.isEmpty())){
				type=LISTTYPE_ID;
			}else if(!(LISTTYPE_ID.isEmpty()) && LISTTYPE_ID!=null){
				type=LISTTYPE_ID;
			}else if(!(NAVBARTYPE_ID.isEmpty()) && NAVBARTYPE_ID!=null){
				type=NAVBARTYPE_ID;
			}else{
				type=DATABASETYPE_ID;
			}
			
			
			vpd.put("var5", type);	//5
			vpd.put("var6", varOList.get(i).getString("PUBLISH_TIME"));	//6
			vpd.put("var7", varOList.get(i).getString("CREATE_TIME"));	//7
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
