package com.ronxuntech.service.spsm.spider;

import java.util.List;
import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;

/** 
 * 说明： 数据爬取接口
 * 创建人：Liuxh
 * 创建时间：2016-10-08
 * @version
 */
public interface SpiderManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**
	 * 通过annexurl查询
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public List<PageData> findByAnnexUrlAndPageUrl(PageData pd) throws Exception;
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

