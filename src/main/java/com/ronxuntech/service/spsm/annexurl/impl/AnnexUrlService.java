package com.ronxuntech.service.spsm.annexurl.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.ronxuntech.dao.DaoSupport;
import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;

/** 
 * 说明： 附件地址
 * 创建人：Liuxh
 * 创建时间：2016-11-24
 * @version
 */
@Service("annexurlService")
public class AnnexUrlService implements AnnexUrlManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("AnnexUrlMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("AnnexUrlMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AnnexUrlMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AnnexUrlMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AnnexUrlMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AnnexUrlMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("AnnexUrlMapper.deleteAll", ArrayDATA_IDS);
	}

	/**
	 * 通过 targeturl 和status 来查询 annex
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listBySeedUrlIdAndStatus(PageData pd) throws Exception {
		
		return (List<PageData>)dao.findForList("AnnexUrlMapper.listBySeedUrlIdAndStatus", pd);
	}

	/**
	 * 批量插入
	 */
	@Override
	public void saveAll(PageData pd) throws Exception {
		dao.save("AnnexUrlMapper.saveAll", pd);
	}

	public List<PageData> findByUrlAndStatus(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("AnnexUrlMapper.findByUrlAndStatus", pd);
	}

	
}

