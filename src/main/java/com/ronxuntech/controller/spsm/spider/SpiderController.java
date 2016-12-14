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
import com.ronxuntech.component.spsm.util.ImgOrDocPipeline;
import com.ronxuntech.component.spsm.util.ReadXML;
import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.databasetype.DataBaseTypeManager;
import com.ronxuntech.service.spsm.listtype.ListTypeManager;
import com.ronxuntech.service.spsm.navbartype.NavBarTypeManager;
import com.ronxuntech.service.spsm.seedurl.SeedUrlManager;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.sublisttype.SubListTypeManager;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.util.AppUtil;
import com.ronxuntech.util.Jurisdiction;
import com.ronxuntech.util.ObjectExcelView;
import com.ronxuntech.util.PageData;

import net.sf.json.JSONSerializer;
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
	
	@Resource(name="databasetypeService")
	private DataBaseTypeManager  databasetypeService;
	
	@Resource(name="navbartypeService")
	private NavBarTypeManager  navbartypeService;

	@Resource(name="listtypeService")
	private ListTypeManager  listtypeService;
	
	@Resource(name="sublisttypeService")
	private SubListTypeManager sublisttypeService;
	
	@Resource(name="seedurlService")
	private SeedUrlManager seedurlService;
	
	@Resource(name="targeturlService")
	private TargetUrlManager targeturlService;
	
	@Resource(name="annexurlService")
	private AnnexUrlManager annexurlService;
	
	
	
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
		mv.setViewName("spsm/spider/spider_start");
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
		//先查询传递来的种子是否已经爬取过。如果爬取过，则跳过。
		PageData seedpd =new PageData();
		seedpd.put("SEEDURL_ID", System.currentTimeMillis());
		seedpd.put("SEEDURL", seedUrl);
		seedpd.put("STATUS", "0");
		PageData seedpd1= seedurlService.findByUrl(seedpd);
		//初始化要爬取网站的信息
		WebInfo web=WebInfo.init(seedUrl, dataType.toString());
		//如果查询到数据库中存在了该种子，那么就证明该网站已经爬取过， 但是状态是 0，那么还有网站或者图未下载完成。
		if(seedpd1!=null && seedpd1.size()!=0){
				if(seedpd1.get("STATUS").equals("0")){
					//重下目标网址
					reDownloadTargetUrl(seedUrl, dataType, seedpd1);
					//重下未下的附件（只是之前没有下载）
					PageData seedPd2=new PageData();
					seedPd2.put("SEEDURLID", seedpd1.getString("SEEDURL_ID"));
					seedPd2.put("STATUS", "0");
					List<PageData> redownAnnexUrlList= annexurlService.listBySeedUrlIdAndStatus(seedPd2);
					if(redownAnnexUrlList.size()>0){
						reDownloadAnnexUrl(seedUrl, seedpd1);
					}
					//接着爬取没有爬完的
					collar(web);
//					
					checkDone(seedpd, out);
				}
		}else{
			//将目前的这个种子添加到数据库
			seedurlService.save(seedpd);
			collar(web);
			
			checkDone(seedpd, out);
		}
		
		out.close();
	}
	/**
	 * 检查是否下载完成
	 * @param seedpd1
	 * @param out
	 * @throws Exception
	 */
	public void checkDone(PageData seedpd1,PrintWriter out) throws Exception{
		PageData targetPd= new PageData();
		targetPd.put("SEEDURLID", seedpd1.get("SEEDURL_ID").toString());
		targetPd.put("STATUS", "0");
		List<PageData>  targetList = targeturlService.listBySeedUrlIdAndStatus(targetPd);
		
		PageData annexPd= new PageData();
		annexPd.put("STATUS", "0");
		annexPd.put("SEEDURLID",seedpd1.get("SEEDURL_ID").toString());
		List<PageData>  annexList =annexurlService.listBySeedUrlIdAndStatus(annexPd);
	
		if(targetList.size()>0 || annexList.size()>0){
			out.write("some thing not done");
		}else{
			out.write("success");
		}
	}
	
	/**
	 * 开始抓取
	 * @param pd
	 * @param seedUrl
	 * @param dataType
	 * @throws Exception
	 */
	public void collar(WebInfo web) throws Exception{
		BaseCrawler crawler=BaseCrawler.getInstance();
		AjaxCrawler ajaxCrawler=AjaxCrawler.getInstance();
		if(web.getPageMethod().equals("get")){
			crawler.start(web);
		}else if(web.getPageMethod().equals("ajax")){
			ajaxCrawler.start(web);
		}
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
		titles.add("内容");	//2
		titles.add("类型");	//3
		titles.add("爬取时间");	//4
		titles.add("网页地址");	//5
		titles.add("附件链接");	//6
		titles.add("附件地址");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = spiderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TITLE"));	//1
			vpd.put("var2", varOList.get(i).getString("CONTENT"));	//2
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
			
			
			vpd.put("var3", type);	//3
			vpd.put("var4", varOList.get(i).getString("CREATE_TIME"));	//4
			vpd.put("var5", varOList.get(i).getString("TARGETURLID"));	//5
			vpd.put("var6", varOList.get(i).getString("ANNEXURLS"));	//6
			vpd.put("var7", varOList.get(i).getString("FILENAME"));	//7
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
	
	/**
	 * 目标网址没有下载完成，则接着下载。同样会下载文档和图片
	 * @param targetUrlList
	 * @param redownTargetUrlList
	 * @param web
	 * @throws Exception   List<String> targetUrlList,List<PageData> redownTargetUrlList ,WebInfo web
	 */
	public void reDownloadTargetUrl(String seedUrl,StringBuffer dataType,PageData pd1) throws Exception{
		BaseCrawler crawler=BaseCrawler.getInstance();
		PageData pd2=new PageData();
		pd2.put("SEEDURLID", pd1.getString("SEEDURL_ID"));
		pd2.put("STATUS", "0");
		List<PageData> redownTargetUrlList=targeturlService.listBySeedUrlIdAndStatus(pd2);
		List<String> targetUrlList=new ArrayList<>();
		if(redownTargetUrlList.size()>0){
			for(int i=0;i<redownTargetUrlList.size();i++){
				targetUrlList.add(redownTargetUrlList.get(i).getString("TARGETURL"));
			}
			//初始化webinfo
			WebInfo web=WebInfo.init(seedUrl, dataType.toString());
			//重新爬取
			crawler.setTargetUrlListAndWebInfo(targetUrlList,web);
			if(web.isHasDoc() || web.isHasImg()){
				Spider.create(crawler).addUrl(redownTargetUrlList.get(0).getString("TARGETURL")).thread(4).run();
			}else{
				Spider.create(crawler).addUrl(redownTargetUrlList.get(0).getString("TARGETURL")).thread(10).run();
			}
		}
		
	}
	
	/**
	 * 附件重新下载
	 * @param seedUrl
	 * @param pd1
	 * @throws Exception
	 */
	public void reDownloadAnnexUrl(String seedUrl,PageData pd1) throws Exception{
		PageData pd3 =new PageData();
		pd3.put("SEEDURLID", pd1.getString("SEEDURL_ID"));
		pd3.put("STATUS", "0");
		
		//得到需要下载的链接
		List<PageData> redownAnnexUrlList= annexurlService.listBySeedUrlIdAndStatus(pd3);
		List<String> annexUrlList=new ArrayList<>(); //重下的地址链接
		List<String> annexNameList= new ArrayList<>(); //下载的文件名称
		for(int i=0;i<redownAnnexUrlList.size();i++){
			String annexurl= redownAnnexUrlList.get(i).getString("ANNEXURL");
			//得到下载的url链接 ，并加入list中
			annexUrlList.add(annexurl);
			//通过url得到下载后的名称。
			PageData pd4 =new PageData();
			pd4.put("ANNEXURL", annexurl.trim());
			pd4.put("TARGETURLID",redownAnnexUrlList.get(i).getString("TARGETURLID"));
			pd4.put("STATUS", "0");
			List<PageData> AnnexUrlAndPageUrlList =spiderService.findByAnnexUrlAndPageUrl(pd4);
//			System.out.println("AnnexUrlAndPageUrlList.size(): "+ AnnexUrlAndPageUrlList.toString());
			for(int j =0;j<AnnexUrlAndPageUrlList.size();j++){
				String fileName =AnnexUrlAndPageUrlList.get(j).getString("FILENAME");
				String annexUrls =AnnexUrlAndPageUrlList.get(j).getString("ANNEXURLS");
//				System.out.println("fileName:"+fileName);
//				System.out.println("annexUrls "+ annexUrls );
				// 将得到的下载地址，通过‘，’分离。得到一个数组
				if(annexUrls!=null && fileName!=null){
					String [] urls =annexUrls.split(",");
					String [] fileNameList =fileName.split(",");
//					System.out.println("annexUrls:"+annexUrls.toString());
					
					//取出一个网页中多个附件中， 未下载的，
					for(int n=0;n<urls.length;n++){
						if(urls[n].equals(annexurl)){
							annexNameList.add(fileNameList[n]);
//							System.out.println("fileNameList() "+fileNameList[n]);
						}
					}
				}
				
			}
		}
//		//下载未下载的文件
//		System.out.println("annexNameList.size() :"+annexNameList.size());
//		for(int m=0;m<annexNameList.size();m++){
//			System.out.println("annexNameList(): "+annexNameList.get(m));
//		}
//		System.out.println("fileNameList.size() : "+annexUrlList.size()  );
//		for(int s=0;s<annexUrlList.size();s++)
//		{
//			System.out.println("annexUrlList :"+annexUrlList.get(s));
//		}
		
		ImgOrDocPipeline imgOrDoc =new ImgOrDocPipeline(seedUrl,annexNameList, annexUrlList);
		imgOrDoc.reDownloadAnnex();
	}
}
