package com.ronxuntech.controller.file;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.information.pictures.PicturesManager;
import com.ronxuntech.util.AppUtil;
import com.ronxuntech.util.Const;
import com.ronxuntech.util.DateUtil;
import com.ronxuntech.util.DelAllFile;
import com.ronxuntech.util.FileUpload;
import com.ronxuntech.util.GetWeb;
import com.ronxuntech.util.Jurisdiction;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.PathUtil;
import com.ronxuntech.util.Tools;
import com.ronxuntech.util.Watermark;

/** 
 * 类名称：图片管理
 * 创建人：FH Q313596790
 * 创建时间：2015-03-21
 */
@Controller
@RequestMapping(value="/imgfile")
public class FileController extends BaseController {
	
	String menuUrl = "imgfile/save.do"; //菜单地址(权限用)
	@Resource(name="picturesService")
	private PicturesManager picturesService;
	
	
	
	/**新增
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Object save(
			@RequestParam(required=false) MultipartFile file
			) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"新增图片");
		Map<String,String> map = new HashMap<String,String>();
		String  ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		if(Jurisdiction.buttonJurisdiction(menuUrl, "add")){
			if (null != file && !file.isEmpty()) {
				String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + ffile;		//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			}else{
				System.out.println("上传失败");
			}
			pd.put("PICTURES_ID", this.get32UUID());			//主键
			pd.put("TITLE", "图片");								//标题
			pd.put("NAME", fileName);							//文件名
			pd.put("PATH", ffile + "/" + fileName);				//路径
			pd.put("CREATETIME", Tools.date2Str(new Date()));	//创建时间
			pd.put("MASTER_ID", "1");							//附属与
			pd.put("BZ", "图片上传");						//备注
			Watermark.setWatemark(PathUtil.getClasspath() + Const.FILEPATHIMG + ffile + "/" + fileName);//加水印
			picturesService.save(pd);
		}
		map.put("result", "ok");
		map.put("id", pd.getString("PICTURES_ID"));
		map.put("path", pd.getString("PATH"));
		return AppUtil.returnObject(pd, map);
	}
	

}
