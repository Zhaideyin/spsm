package com.ronxuntech.service.socket.fullchannelxml.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.ronxuntech.dao.DaoSupport;
import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;
import com.ronxuntech.service.socket.fullchannelxml.FullChannelXmlManager;
import com.ronxuntech.service.socket.socketport.SocketPortManager;

/** 
 * 说明： 全渠道配置文件
 * 创建人：Liuxh
 * 创建时间：2016-08-15
 * @version
 */
@Service("fullchannelxmlService")
public class FullChannelXmlService implements FullChannelXmlManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name="socketportService")
	private SocketPortManager socketportService;
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("FullChannelXmlMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public boolean delete(PageData pd)throws Exception{
		//删除前，先判断如果端口中如果rule中存在有和fullchannel中state相同的，则不能删除
		List<PageData> list =socketportService.findByState(pd);
		boolean flag ;
		if(list.size()==0){
			dao.delete("FullChannelXmlMapper.delete", pd);
			flag=true;
		}else{
			flag=false;
		}
		return flag;
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("FullChannelXmlMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("FullChannelXmlMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("FullChannelXmlMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("FullChannelXmlMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("FullChannelXmlMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

