package com.ronxuntech.controller.socket.fullchannelxml;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ronxuntech.component.socket.util.Fileutil;
import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.socket.fullchannelxml.FullChannelXmlManager;
import com.ronxuntech.service.socket.socketport.SocketPortManager;
import com.ronxuntech.util.AppUtil;
import com.ronxuntech.util.Jurisdiction;
import com.ronxuntech.util.ObjectExcelView;
import com.ronxuntech.util.PageData;

/** 
 * 说明：全渠道配置文件
 * 创建人：Liuxh
 * 创建时间：2016-08-15
 */
@Controller
@RequestMapping(value="/fullchannelxml")
public class FullChannelXmlController extends BaseController {
	
	String menuUrl = "fullchannelxml/list.do"; //菜单地址(权限用)
	@Resource(name="fullchannelxmlService")
	private FullChannelXmlManager fullchannelxmlService;
	
	@Resource(name="socketportService")
	private SocketPortManager socketportService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增FullChannelXml");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FULLCHANNELXML_ID", this.get32UUID());	//主键
		//将输入的state值存入
		pd.put("STATE",pd.getString("NEW_STATE"));
		//获取当前的文件路径
		String filepath  = Fileutil.createFile(pd.getString("NEW_STATE"),pd.getString("CONTENT"));
		//content 存的是本地文件的路径
		pd.put("CONTENT",filepath);
		fullchannelxmlService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除FullChannelXml");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		
		//先通过id查询出fullchannel
		PageData pd1 =fullchannelxmlService.findById(pd);
		//取出state放入pd2.
		PageData pd2=new PageData();
//		System.out.println(pd1.get("STATE")+"****STATE****");
		pd2.put("RULE",pd1.get("STATE"));//将说明，（如8583放入pd2中。）
		//判断是否有数据，如果有，那么不能删除，
		boolean flag =fullchannelxmlService.delete(pd2);
		if(flag){
			fullchannelxmlService.delete(pd);
		}
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改FullChannelXml");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//通过文件路径，和修改类容。修改xml内容
		
		//先创建一个文件，将内容放进去。然后把文件替换掉
//		Fileutil.createFile(pd.getString("STATE"),pd.getString("CONTENT"));
		String path =Fileutil.updateFile(pd.getString("path"),pd.getString("CONTENT"),pd.getString("NEW_STATE"));
		//保存文件路径
		pd.put("CONTENT",path);
		
		System.out.println("修改之前的state:-*--"+pd.getString("STATE"));
		System.out.println("修改之后state:---"+pd.getString("NEW_STATE"));
		//如果传进来的state和new-state一样那么就不用去修改之前的，否则就修改之前的 
		if(pd.getString("STATE").equals(pd.getString("NEW_STATE"))){
			
		}else{
			PageData pd2 =new PageData();
			//取出修改之前的rule。然后查询
			pd2.put("RULE",pd.getString("STATE"));
			//查询所有关于这个rule的端口记录。然后修改之前的 规则为NEW_STATE
			List<PageData> list=socketportService.findByState(pd2);
			System.out.println("list.size:---"+list.size());
			for(int i=0;i<list.size();i++){
				PageData pd3=list.get(i);
				pd3.put("RULE",pd.getString("NEW_STATE"));
				socketportService.edit(pd3);
			}
		}
		
		//如果修改了xml说明 ,那么修改端口那边的规则
		pd.put("STATE", pd.getString("NEW_STATE"));
		fullchannelxmlService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表FullChannelXml");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = fullchannelxmlService.list(page);	//列出FullChannelXml列表
		mv.setViewName("socket/fullchannelxml/fullchannelxml_list");
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
		mv.setViewName("socket/fullchannelxml/fullchannelxml_edit");
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
		pd = fullchannelxmlService.findById(pd);	//根据ID读取
		String path =pd.getString("CONTENT");
		String content =Fileutil.readFile(path);
		mv.setViewName("socket/fullchannelxml/fullchannelxml_edit");
		mv.addObject("msg", "edit");
		mv.addObject("content",content);
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除FullChannelXml");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			fullchannelxmlService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出FullChannelXml到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("xml内容");	//1
		titles.add("创建时间");	//2
		titles.add("说明");	//3
		dataMap.put("titles", titles);
		List<PageData> varOList = fullchannelxmlService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CONTENT"));	//1
			vpd.put("var2", varOList.get(i).getString("CRATETIME"));	//2
			vpd.put("var3", varOList.get(i).getString("STATE"));	//3
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
