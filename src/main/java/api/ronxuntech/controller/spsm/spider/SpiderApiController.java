package api.ronxuntech.controller.spsm.spider;

import api.ronxuntech.controller.BaseController;
import com.ronxuntech.component.spsm.lucene.LuceneUtil;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.spsm.databasetype.DataBaseTypeManager;
import com.ronxuntech.service.spsm.listtype.ListTypeManager;
import com.ronxuntech.service.spsm.navbartype.NavBarTypeManager;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.sublisttype.SubListTypeManager;
import com.ronxuntech.util.PageData;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 说明：数据爬取 创建人：Liuxh 创建时间：2016-10-08
 *
 */
@RestController
@RequestMapping(value = "/api/spider")
public class SpiderApiController extends BaseController {

	@Resource(name = "spiderService")
	private SpiderManager spiderService;
	@Resource
	private DataBaseTypeManager dataBaseTypeService;
	@Resource
	private NavBarTypeManager navBarTypeService;
	@Resource
	private ListTypeManager listTypeService;
	@Resource
	private SubListTypeManager subListTypeManager;
	/**
	 * 查询全部
	 * @param page
	 * @param count
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "list")
	public Map list(int page, int count) throws Exception {
		Page pg = new Page(page, count);
		pg.setPd(this.getPageData());

		List<PageData> varList = spiderService.list(pg); // 列出SPIDER列表,有分页
		return this.formatRtnMsg(varList, "success", String.valueOf(pg.getTotalPage()));
	}
	
	/**
	 * 全文检索查询
	 * @param page
	 * @param count
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "fullTextSearch")
	public Map fullTextSearch(int page, int count,String keyword) throws Exception {
		Page pg = new Page(page, count);
		pg.setTotalResult(100);
		List<String> fieldNameList = new ArrayList<>();
		fieldNameList.add("TITLE");
		fieldNameList.add("CONTENT");
		fieldNameList.add("SPIDER_ID");
		List<PageData> varList = LuceneUtil.search(keyword, fieldNameList, pg); // 列出SPIDER列表,有分页
		return this.formatRtnMsg(varList, "success", String.valueOf(pg.getTotalPage()));
	}
	
	/**
	 * 通过 datatypeName 查询 得到 dataypeId 然后查询出该分类的数据， 有分页。
	 * @param page
	 * @param count
	 * @param datatypeName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="listPageByDataType")
	public Map listPageByDataType(int page, int count,String datatypeName)throws Exception{
		Page pg = new Page(page, count);
		PageData pd = this.getPageData();
		pd.put("datatypeName", datatypeName);

		PageData pd1 = null;
		String datatype ="";
		pd1 = dataBaseTypeService.findByName(pd);
		if (null == pd1){
			pd1 = navBarTypeService.findByName(pd);
			if(null == pd1){
				pd1 =  listTypeService.findByName(pd);
				if(null == pd1){
					pd1 = subListTypeManager.findByName(pd);
					datatype = pd1.getString("SUBLISTTYPE_ID");
				}else{
					datatype = pd1.getString("LISTTYPE_ID"	);
				}
			}else{
				datatype = pd1.getString("NAVBARTYPE_ID");
			}
		}else{
			datatype = pd1.getString("DATABASETYPE_ID");
		}
		//通过pd1查数据库中的对应的type
		PageData pd2 = new PageData();
		pd2.put("datatype",datatype);
		pg.setPd(pd2);
		
		List<PageData> varList  = spiderService.listPageByDataType(pg);
		return this.formatRtnMsg(varList, "success", String.valueOf(pg.getTotalPage()));
	}

	/**
	 * 列表 没有分页
	 * @param databaseTypeId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="listByDataType")
	public Map listByDataType(String databaseTypeId)throws Exception{
		
		PageData pd = this.getPageData();
		pd.put("datatype", databaseTypeId);
		List<PageData> varList  = spiderService.listByDataType(pd);
		return this.formatRtnMsg(varList, "success");
	}

	// 根据id获取单条信息
	@RequestMapping(value = "info")
	public Object info(String id) throws Exception {
		PageData pd = this.getPageData();
		pd.put("SPIDER_ID", id);
		return spiderService.findById(pd);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
	}
}
