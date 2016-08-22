package com.ronxuntech.controller.socket.socketport;

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

import com.ronxuntech.component.socket.SocketServers;
import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.socket.fullchannelxml.impl.FullChannelXmlService;
import com.ronxuntech.service.socket.socketport.SocketPortManager;
import com.ronxuntech.util.AppUtil;
import com.ronxuntech.util.Jurisdiction;
import com.ronxuntech.util.ObjectExcelView;
import com.ronxuntech.util.PageData;

/** 
 * 说明：socket端口
 * 创建人：Liuxh
 * 创建时间：2016-08-10
 */
@Controller
@RequestMapping(value="/socketport")
public class SocketPortController extends BaseController {
	
	String menuUrl = "socketport/list.do"; //菜单地址(权限用)
	@Resource(name="socketportService")
	private SocketPortManager socketportService;
	
	@Resource(name="fullchannelxmlService")
	private FullChannelXmlService fullchannelxmlService;
	
	private SocketServers server =SocketServers.getInstance();
	
	private String  path;
	/**
	 * 开启端口
	 * gostart
	 */
	@RequestMapping(value="/start")
	public void start(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"开启SocketPort");
		PageData pd = new PageData();
		pd = this.getPageData();
		
		final int port=Integer.parseInt(socketportService.findById(pd).getString("PORT"));
		PageData pd1= socketportService.findById(pd);
	
		//查找对应的协议获取到相应的地址信息,如8583
		
		String rule=pd1.get("RULE").toString();
		System.out.println(rule+"------------规则---------------------");
		PageData pd2=new PageData();
		pd2.put("STATE",rule);
		List<PageData> list = fullchannelxmlService.listAll(pd2);
		for(int i=0;i<list.size();i++){
			if(list.get(i).get("STATE").equals(rule)){
				path=list.get(i).getString("CONTENT");
				System.out.println(path+"-----------filepath--------------------------");
				Thread t=new Thread(){
					@Override
					public void run(){
						try {
							server.start(port,path);
							System.out.println("sever...run()-----------------");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
				System.out.println("thread...start()--------------");
				//修改状态.
				pd1.put("STATE", "开启");
				socketportService.edit(pd1);
				break;
			}
		}
		out.write("success");
		out.close();
	}
	/*
	 * 关闭端口
	 */
	@RequestMapping(value="/stop")
	public void stop(PrintWriter out) throws Exception{
		
		server.stop();
		//修改数据库状态
		PageData pd = new PageData();
		pd = this.getPageData();
		
		PageData pd1= socketportService.findById(pd);
		//修改状态.
		pd1.put("STATE", "关闭");
		socketportService.edit(pd1);
		out.write("success");
		out.close();
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增SocketPort");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
	
//		//通过id查询出对应的xml.
//		PageData pd2=new PageData();
//		pd2.put("FULLCHANNELXML_ID", id);
//		pd2 =fullchannelxmlService.findById(pd2);
		
		//先查询
		List<PageData> list=  socketportService.listAll(pd);
//		System.out.println(list.get(0)+"*********************************");
		int temp=0;  //统计是否存在
		for(int i=0;i<list.size();i++){
			if(list.get(i).get("PORT").equals(pd.get("PORT"))){
				temp++;
			}else{
				continue;
			}
		}
		if(temp==0){//如果不存在则添加到数据库，
			//设置默认值 状态为关闭，创建时间为当前时间
			pd.put("STATE", "关闭");
			pd.put("CREATETIME", new Date());
			
			pd.put("SOCKETPORT_ID", this.get32UUID());	//主键
			socketportService.save(pd);
		}
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
		logBefore(logger, Jurisdiction.getUsername()+"删除SocketPort");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		socketportService.delete(pd);
		
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改SocketPort");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		socketportService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表SocketPort");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = socketportService.list(page);	//列出SocketPort列表
		mv.setViewName("socket/socketport/socketport_list");
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
		
		//查询rule,
		List<PageData> list= fullchannelxmlService.listAll(pd);
		/*for(int i=0;i<list.size();i++){
			System.out.println("-------------------------");
			System.out.println(list.get(i).getString("STATE"));
		}
		pd.put("rule", list);*/
		pd = this.getPageData();
		mv.setViewName("socket/socketport/socketport_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		mv.addObject("rules",list);
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
		//查询rule,
		List<PageData> list= fullchannelxmlService.listAll(pd);
		pd = socketportService.findById(pd);	//根据ID读取
		mv.setViewName("socket/socketport/socketport_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		mv.addObject("rules",list);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除SocketPort");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			socketportService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出SocketPort到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("端口");	//1
		titles.add("状态");	//2
		titles.add("创建时间");	//3
		titles.add("用途");  //4
		titles.add("规则");  //5
		dataMap.put("titles", titles);
		List<PageData> varOList = socketportService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PORT"));	//1
			vpd.put("var2", varOList.get(i).getString("STATE"));	//2
			vpd.put("var3", varOList.get(i).getString("CREATTIME"));	//3
			vpd.put("var", varOList.get(i).getString("AFFCT")); //4
			vpd.put("var",varOList.get(i).getString("RULE"));  //5
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
