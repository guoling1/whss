package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/query")
public class MerchantInfoQueryController extends BaseController {

    @Autowired
    private MerchantInfoQueryService merchantInfoQueryService;



    @ResponseBody
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> getAll(@RequestBody MerchantInfoRequest req) throws ParseException {
        final PageModel<MerchantInfoResponse> pageModel = new PageModel<MerchantInfoResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        if(req.getEndTime1()!=null&&!"".equals(req.getEndTime1())){
            Date dt = sdf.parse(req.getEndTime1());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime1(sdf.format(rightNow.getTime()));
        }
        if(req.getEndTime2()!=null&&!"".equals(req.getEndTime2())){
            Date dt = sdf.parse(req.getEndTime2());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime2(sdf.format(rightNow.getTime()));
        }
        long count = this.merchantInfoQueryService.getCount(req);
        List<MerchantInfoResponse> list = this.merchantInfoQueryService.getAll(req);
        if (list == null){
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }else {
            for (int i=0;i<list.size();i++){
                list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getMobile()));
            }
        }

        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }


}
