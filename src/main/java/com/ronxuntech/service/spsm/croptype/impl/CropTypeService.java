package com.ronxuntech.service.spsm.croptype.impl;

import java.util.List;
import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ronxuntech.dao.DaoSupport;
import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;
import com.ronxuntech.service.spsm.croptype.CropTypeManager;

/** 
 * 说明： 作物类别
 * 创建人：Liuxh
 * 创建时间：2017-02-16
 * @version
 */

@Service("croptypeService")
public class CropTypeService implements CropTypeManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("CropTypeMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CropTypeMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CropTypeMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CropTypeMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CropTypeMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CropTypeMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CropTypeMapper.deleteAll", ArrayDATA_IDS);
	}

	/**
	 * 通过cropName获取数据
	 */
	@Override
	public PageData findByCropName(PageData pd) throws Exception {
		return (PageData) dao.findForObject("CropTypeMapper.findByCropName", pd);
	}

	@Override
	public List<PageData> findByCropTypeName(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("CropTypeMapper.findByCropTypeName", pd);
	}

	/**
	 *查询作物与作物类别的关系
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<PageData> findCropAndCropTypeRaletion(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("CropTypeMapper.findCropAndCropTypeRaletion", pd);
	}
	
}

