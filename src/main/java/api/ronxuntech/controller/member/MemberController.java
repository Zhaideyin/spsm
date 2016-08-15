package api.ronxuntech.controller.member;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ronxuntech.aop.Before;
import com.ronxuntech.service.system.appuser.AppuserManager;
import com.ronxuntech.util.Const;
import com.ronxuntech.util.JwtsUtil;
import com.ronxuntech.util.MD5;
import com.ronxuntech.util.PageData;

import api.ronxuntech.controller.BaseController;
import api.ronxuntech.validator.MemberLoginValidator;

@RestController
@RequestMapping("/api/member")
public class MemberController extends BaseController{
	@Resource(name="appuserService")
	private AppuserManager appuserService;
	
	@RequestMapping(method=RequestMethod.POST,value="/store")
	public Object store(){
		PageData pd = new PageData();
		pd = this.getPageData();
		String userId= this.get32UUID();
		pd.put("USER_ID", this.get32UUID());	//ID
		pd.put("RIGHTS", "");					
		pd.put("LAST_LOGIN", "");				//最后登录时间
		pd.put("IP", "");						//IP
		pd.put("PASSWORD", MD5.md5(pd.getString("PASSWORD")));
		try {
			if(null == appuserService.findByUsername(pd)
					&&null==appuserService.findByEmail(pd)
					&&null==appuserService.findByPhone(pd)
			){
				appuserService.saveU(pd);
				//获取token
				Map<String,Object> tokenMap=JwtsUtil.newInstance().generateToken(Const.STORE_KEY,userId,Const.KEY_STRING);
				return this.formatSuccessMsg(tokenMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.formatErrMsg(0, "注册失败");
	}
	@Before({MemberLoginValidator.class})
	@RequestMapping(method=RequestMethod.GET,value="/login")
	public Object login(){
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd.put("PASSWORD", MD5.md5(pd.getString("PASSWORD")));
			pd=appuserService.findByLogin(pd);
			if(null!=pd){
				//登录成功，获取token
				String userId=(String)pd.get("USER_ID");
				Map<String,Object> tokenMap=JwtsUtil.newInstance().generateToken(Const.STORE_KEY,userId,Const.KEY_STRING);
				return this.formatSuccessMsg(tokenMap);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.formatErrMsg(0, "登录失败");
	}
}