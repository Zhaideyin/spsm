package com.ronxuntech.service.spsm.croptype;

import java.util.List;
import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;

/** 
 * 说明： 作物类别接口
 * 创建人：Liuxh
 * 创建时间：2017-02-16
 * @version
 */
public interface CropTypeManager{

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
	
	/**通过CropName获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCropName(PageData pd)throws Exception;

	/**
	 * 通过作物类型查询
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findByCropTypeName(PageData pd) throws  Exception;
	/**
	 * 查询作物与作物类别的关系
	 * @param pd
	 * @return
	 * @throws Exception
	 */

	public List<PageData> findCropAndCropTypeRaletion(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

