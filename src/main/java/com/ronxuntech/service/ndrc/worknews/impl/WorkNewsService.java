package com.ronxuntech.service.ndrc.worknews.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.ronxuntech.entity.system.Role;
import com.ronxuntech.util.*;
import org.springframework.stereotype.Service;
import com.ronxuntech.dao.DaoSupport;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.ndrc.worknews.WorkNewsManager;

/** 
 * 说明： 工作动态
 * 创建人：Liuxh
 * 创建时间：2016-07-13
 * @version
 */
@Service("worknewsService")
public class WorkNewsService implements WorkNewsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		String contentPath = pd.getString("CONTENTPATH");
		String content = pd.getString("CONTENT");
		contentPath = FileUtil.saveContentToFile(contentPath, content);
		pd.put("CONTENT", contentPath);
		dao.save("WorkNewsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("WorkNewsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		String contentPath = pd.getString("CONTENTPATH");
		String content = pd.getString("CONTENT");
		contentPath = FileUtil.saveContentToFile(contentPath, content);
		pd.put("CONTENT",contentPath);
		dao.update("WorkNewsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		List<PageData> pageDatas = (List<PageData>) dao.findForList("WorkNewsMapper.datalistPage", page);
		for (PageData pageData :pageDatas){
			//文件路径
			String contentpath = pageData.getString("CONTENT");

			String fileuploadpath = PathUtil.getClasspath() + Const.FILEPATHFILE;

			String r = FileUtil.readFileToString(fileuploadpath + contentpath);

			String result = HtmlUtil.filterHtml(r);

			int end = 100;

			if (result.length() < end) {
				end = result.length();
			}

			pageData.put("CONTENT", result.substring(0, end));

			//文件存放的路径
			pageData.put("CONTENTPATH",contentpath);
		}

		return pageDatas;
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("WorkNewsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		PageData pageData = (PageData) dao.findForObject("WorkNewsMapper.findById", pd);
		//	文件路径
		String contentpath = pageData.getString("CONTENT");

		String fileuploadpath = PathUtil.getClasspath() + Const.FILEPATHFILE;
		File file =new File(fileuploadpath + contentpath);
		//如果文件存在
		if (file.exists()){
			String r = FileUtil.readFileToString(fileuploadpath + contentpath);
			pageData.put("CONTENT",r);
			//文件存放的路径
			pageData.put("CONTENTPATH",contentpath);

		}
		return pageData;
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("WorkNewsMapper.deleteAll", ArrayDATA_IDS);
	}

	

	
}

