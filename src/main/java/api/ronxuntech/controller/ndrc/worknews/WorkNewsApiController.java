package api.ronxuntech.controller.ndrc.worknews;

import api.ronxuntech.controller.BaseController;
import com.ronxuntech.entity.Page;
import com.ronxuntech.service.ndrc.worknews.WorkNewsManager;
import com.ronxuntech.util.PageData;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/13.
 */

@RestController
@RequestMapping(value = "/api/worknews")
public class WorkNewsApiController extends BaseController {
    @Resource(name = "worknewsService")
    private WorkNewsManager worknewsService;

    @RequestMapping(value="list")
    public Map list(int page,int count) throws Exception {
        Page pg = new Page(page,count);
        pg.setPd(this.getPageData());

        List<PageData> varList = worknewsService.list(pg);	//列出worknews列表
        return this.formatRtnMsg(varList, "success",String.valueOf(pg.getTotalPage()));
    }

    @RequestMapping(value = "info")
    public Object info() throws Exception {
        PageData pd = this.getPageData();
        return worknewsService.findById(pd);
    }


    @InitBinder
    public void initBinder(WebDataBinder binder){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
    }
}
