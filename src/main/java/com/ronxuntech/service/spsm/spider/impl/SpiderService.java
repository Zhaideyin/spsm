package com.ronxuntech.service.spsm.spider.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ronxuntech.dao.DaoSupport;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.util.PageData;

/** 
 * 说明： 数据爬取
 * 创建人：Liuxh
 * 创建时间：2016-10-08
 * @version
 */
@Service("spiderService")
public class SpiderService implements SpiderManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("SpiderMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("SpiderMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("SpiderMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SpiderMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SpiderMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SpiderMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("SpiderMapper.deleteAll", ArrayDATA_IDS);
	}

	/**
	 * 通过下载链接去查询下载后的名称及地址。
	 */
	public List<PageData> findByAnnexUrlAndPageUrl(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("SpiderMapper.findByAnnexUrlAndPageUrl", pd);
	}
	
}

