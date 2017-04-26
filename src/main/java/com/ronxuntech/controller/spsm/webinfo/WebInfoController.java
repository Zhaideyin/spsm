package com.ronxuntech.controller.spsm.webinfo;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.ReadXML;
import com.ronxuntech.service.spsm.databasetype.DataBaseTypeManager;
import com.ronxuntech.service.spsm.listtype.ListTypeManager;
import com.ronxuntech.service.spsm.navbartype.NavBarTypeManager;
import com.ronxuntech.service.spsm.sublisttype.SubListTypeManager;
import net.sf.json.JSONSerializer;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.spsm.webinfo.WebInfoManager;
import com.ronxuntech.util.AppUtil;
import com.ronxuntech.util.Jurisdiction;
import com.ronxuntech.util.ObjectExcelView;
import com.ronxuntech.util.PageData;


/** 
 * 说明：网站抓取信息配置
 * 创建人：Liuxh
 * 创建时间：2016-12-26
 */
@Controller
@RequestMapping(value="/webinfo")
public class WebInfoController extends BaseController {
	
	String menuUrl = "webinfo/list.do"; //菜单地址(权限用)
	@Resource(name="webinfoService")
	private WebInfoManager webinfoService;
	@Resource(name="databasetypeService")
	private DataBaseTypeManager databasetypeService;

	@Resource(name="navbartypeService")
	private NavBarTypeManager navbartypeService;

	@Resource(name="listtypeService")
	private ListTypeManager listtypeService;

	@Resource(name="sublisttypeService")
	private SubListTypeManager sublisttypeService;
	private SimpleDateFormat sdf = new SimpleDateFormat("YYYY:MM:DD hh:mm:ss");
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增WebInfo");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(pd.getString("URLTAG").equals("")){
			pd.put("URLTAG","//body");
		}
		pd.put("WEBINFO_ID", this.get32UUID());	//主键
		pd.put("CREATETIME",sdf.format(new Date()));
		webinfoService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除WebInfo");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		webinfoService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改WebInfo");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		webinfoService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表WebInfo");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("seedUrl");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("seedUrl", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = webinfoService.list(page);	//列出WebInfo列表

		/*List<PageData> sublisttype = sublisttypeService.listAll(new PageData());
		pd.put("sublisttype", sublisttype);
		List<PageData> listtype = listtypeService.listAll(new PageData());
		pd.put("listtype", listtype);
		List<PageData> navbarTypeList = navbartypeService.listAll(new PageData());
		pd.put("navbarTypeList", navbarTypeList);
		List<PageData> databaseTypeList = databasetypeService.listAll(new PageData());
		pd.put("databaseTypeList",databaseTypeList);*/

		mv.setViewName("spsm/webinfo/webinfo_list");
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
		mv.setViewName("spsm/webinfo/webinfo_edit");
		mv.addObject("msg", "save");

		//查询所有的数据类型
		List<PageData> datatypeList =databasetypeService.listAll(pd);
		pd.put("datatypeList", datatypeList);
//		ReadXML rxml=new ReadXML();

		List<PageData> listmap = webinfoService.listAll(new PageData());
//				rxml.ResolveXml();
		mv.addObject("urlList",listmap);
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 下拉框数据获取
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/chooseNextSelect")
	public void chooseNextSelect(HttpServletResponse response) throws Exception{
		List<PageData> list =new 	ArrayList<>();
		PageData pd = this.getPageData();
		response.setCharacterEncoding("utf-8");
		PrintWriter out =response.getWriter();

		//判断下是第几个， 则执行不同的service.
		String flag =pd.get("flag").toString();
		if(flag.equals("1")){
			list =navbartypeService.listFindByDatabaseID(pd);
		}else if(flag.equals("2")){
			list = listtypeService.listFindByNavbarID(pd);
		}else if(flag.equals("3")){
			list=sublisttypeService.listFindByListID(pd);
		}
		out.print(JSONSerializer.toJSON(list));
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
		pd = webinfoService.findById(pd);	//根据ID读取
		//查询所有的数据类型
		List<PageData> datatypeList =databasetypeService.listAll(pd);
		pd.put("datatypeList",datatypeList);

		PageData tempPd = new PageData();
		tempPd.put("DATABASETYPE_ID",pd.getString("DATABASETYPEID"));
		List<PageData> navbartypeList =navbartypeService.listFindByDatabaseID(tempPd);
		pd.put("navbartypeList",navbartypeList);

		tempPd.put("NAVBARTYPE_ID",pd.getString("NAVBARTYPEID"));
		List<PageData> listypeList =listtypeService.listFindByNavbarID(tempPd);
		pd.put("listypeList",listypeList);

		tempPd.put("LISTTYPE_ID",pd.getString("LISTTYPEID"));
		List<PageData> sublistypeList =sublisttypeService.listFindByListID(tempPd);
		pd.put("sublistypeList",sublistypeList);

		mv.setViewName("spsm/webinfo/webinfo_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除WebInfo");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			webinfoService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出WebInfo到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("种子地址");	//1
		titles.add("url过滤正则表达式");	//2
		titles.add("标题标签");	//3
		titles.add("内容标签");	//4
		titles.add("是否含图片");	//5
		titles.add("图片过滤正则表达式");	//6
		titles.add("图片标签");	//7
		titles.add("是否含有文件");	//8
		titles.add("文件过滤正则表达式");	//9
		titles.add("文件标签");	//10
		titles.add("网站总页数");	//11
		titles.add("网站分页是ajax分页设置");	//12
		titles.add("网站分页是get的");	//13
		titles.add("网站分页类型");	//14
		titles.add("网页编码格式");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = webinfoService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("SEED"));	//1
			vpd.put("var2", varOList.get(i).getString("URLREX"));	//2
			vpd.put("var3", varOList.get(i).getString("TITLETAG"));	//3
			vpd.put("var4", varOList.get(i).getString("CONTENTTAG"));	//4
			vpd.put("var5", varOList.get(i).getString("HASIMG"));	//5
			vpd.put("var6", varOList.get(i).getString("IMGREGEX"));	//6
			vpd.put("var7", varOList.get(i).getString("IMGTAG"));	//7
			vpd.put("var8", varOList.get(i).getString("HASDOC"));	//8
			vpd.put("var9", varOList.get(i).getString("DOCREGEX"));	//9
			vpd.put("var10", varOList.get(i).getString("DOCTAG"));	//10
			vpd.put("var11", varOList.get(i).getString("TOTALPAGE"));	//11
			vpd.put("var12", varOList.get(i).getString("PAGEAJAXTAG"));	//12
			vpd.put("var13", varOList.get(i).getString("PAGEGETTAG"));	//13
			vpd.put("var14", varOList.get(i).getString("PAGEMETHOD"));	//14
			vpd.put("var15", varOList.get(i).getString("PAGEENCODING"));	//15
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
