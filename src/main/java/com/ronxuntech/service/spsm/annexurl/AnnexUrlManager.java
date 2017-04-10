package com.ronxuntech.service.spsm.annexurl;

import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;

import java.util.List;

/** 
 * 说明： 附件地址接口
 * 创建人：Liuxh
 * 创建时间：2016-11-24
 * @version
 */
public interface AnnexUrlManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**
	 * 批量插入
	 * @param pd
	 * @throws Exception
	 */
	public void saveAll(PageData pd)throws Exception;
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
	
	/**
	 * 通过targeturl 和状态来查询附件
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listBySeedUrlIdAndStatus(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过url 查询 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findByUrlAndStatus(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

