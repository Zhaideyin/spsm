package com.ronxuntech.service.spsm.targeturl.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.ronxuntech.dao.DaoSupport;
import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;

/** 
 * 说明： 目标地址
 * 创建人：Liuxh
 * 创建时间：2016-11-24
 * @version
 */
@Service("targeturlService")
public class TargetUrlService implements TargetUrlManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("TargetUrlMapper.save", pd);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void saveAll(PageData pd)throws Exception{
		dao.save("TargetUrlMapper.saveAll", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("TargetUrlMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("TargetUrlMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("TargetUrlMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("TargetUrlMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("TargetUrlMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("TargetUrlMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**
	 * 通过seedurl 和状态来查询目标url
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listBySeedUrlIdAndStatus(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("TargetUrlMapper.listBySeedUrlIdAndStatus", pd);
	}
	/**
	 * 通过url 查询
	 * 
	 */
	@Override
	public List<PageData> findByUrl(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("TargetUrlMapper.findByUrl", pd);
	}
}

