package com.ronxuntech.controller.spsm.achievement;

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
import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.util.AppUtil;
import com.ronxuntech.util.ObjectExcelView;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.Jurisdiction;
import com.ronxuntech.util.Tools;
import com.ronxuntech.service.spsm.achievement.AchievementManager;

/** 
 * 说明：库查询
 * 创建人：Liuxh
 * 创建时间：2017-01-16
 */
@Controller
@RequestMapping(value="/achievement")
public class AchievementController extends BaseController {
	
	String menuUrl = "achievement/list.do"; //菜单地址(权限用)
	@Resource(name="achievementService")
	private AchievementManager achievementService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Achievement");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ACHIEVEMENT_ID", this.get32UUID());	//主键
		achievementService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Achievement");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		achievementService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Achievement");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		achievementService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Achievement");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = achievementService.list(page);	//列出Achievement列表
		mv.setViewName("spsm/achievement/achievement_list");
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
		mv.setViewName("spsm/achievement/achievement_edit");
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
		pd = achievementService.findById(pd);	//根据ID读取
		mv.setViewName("spsm/achievement/achievement_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Achievement");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			achievementService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Achievement到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("标题");	//1
		titles.add("完成单位");	//2
		titles.add("完成人");	//3
		titles.add("奖励类型");	//4
		titles.add("奖励等级");	//5
		titles.add("获奖时间");	//6
		titles.add("获奖级别");	//7
		titles.add("描述");	//8
		titles.add("联系单位名称");	//9
		titles.add("联系单位地址");	//10
		titles.add("联系单位邮编");	//11
		titles.add("联系单位电话");	//12
		titles.add("联系人");	//13
		titles.add("奖励来源");	//14
		titles.add("奖励发布单位");	//15
		titles.add("获奖时间");	//16
		titles.add("关键字");	//17
		titles.add("认证时间");	//18
		titles.add("认证单位");	//19
		titles.add("注册标志");	//20
		titles.add("公共机构");	//21
		titles.add("发布时间");	//22
		titles.add("创建时间");	//23
		titles.add("点击量");	//24
		dataMap.put("titles", titles);
		List<PageData> varOList = achievementService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TITLE"));	//1
			vpd.put("var2", varOList.get(i).getString("COMPLETE_ORG"));	//2
			vpd.put("var3", varOList.get(i).getString("COMPLETE_PER"));	//3
			vpd.put("var4", varOList.get(i).getString("ENCOURAGEMENT_CLASS"));	//4
			vpd.put("var5", varOList.get(i).getString("ENCOURAGEMENT_GRADE"));	//5
			vpd.put("var6", varOList.get(i).getString("ENCOURAGEMENT_DATE"));	//6
			vpd.put("var7", varOList.get(i).getString("ENCOURAGEMENT_LEVEL"));	//7
			vpd.put("var8", varOList.get(i).getString("DESCRIPTION"));	//8
			vpd.put("var9", varOList.get(i).getString("ORGANIZATION_NAME"));	//9
			vpd.put("var10", varOList.get(i).getString("ORGANIZATION_ADDRESS"));	//10
			vpd.put("var11", varOList.get(i).getString("ORGANIZATION_POSTCODE"));	//11
			vpd.put("var12", varOList.get(i).getString("ORGANIZATION_PHONE"));	//12
			vpd.put("var13", varOList.get(i).getString("ORGANIZATION_LINKMAN"));	//13
			vpd.put("var14", varOList.get(i).getString("SOURCE"));	//14
			vpd.put("var15", varOList.get(i).getString("DATA_PUBLIC_NAME"));	//15
			vpd.put("var16", varOList.get(i).getString("DATA_PUBLIC_DATE"));	//16
			vpd.put("var17", varOList.get(i).getString("KEYWORDS"));	//17
			vpd.put("var18", varOList.get(i).getString("IDENTIFY_DATE"));	//18
			vpd.put("var19", varOList.get(i).getString("IDENTIFY_ORG"));	//19
			vpd.put("var20", varOList.get(i).getString("REGISTRATION_MARK"));	//20
			vpd.put("var21", varOList.get(i).getString("PUBLIC_ORGANIZATION"));	//21
			vpd.put("var22", varOList.get(i).getString("PUBLIC_DATE"));	//22
			vpd.put("var23", varOList.get(i).getString("CREATE_TIME"));	//23
			vpd.put("var24", varOList.get(i).get("HIT").toString());	//24
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
