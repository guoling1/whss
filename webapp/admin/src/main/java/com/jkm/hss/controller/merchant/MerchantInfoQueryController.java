package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.service.MerchantInfoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Controller
@RequestMapping(value = "/admin/query")
public class MerchantInfoQueryController extends BaseController {

    @Autowired
    private MerchantInfoQueryService merchantInfoQueryService;



    @ResponseBody
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> getAll(@RequestBody MerchantInfoResponse merchantInfoResponse) {
        int pageNo = merchantInfoResponse.getPageNo();
        int pageSize = merchantInfoResponse.getPageSize();
        merchantInfoResponse.setPageNo((pageNo-1) * pageSize);
        List<MerchantInfoResponse> list1 = this.merchantInfoQueryService.getCount();
        List<MerchantInfoResponse> list = this.merchantInfoQueryService.getAll(merchantInfoResponse);

        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getDealerId()==0){
                    String proxyName = "金开门";
                    list.get(i).setProxyName(proxyName);
                }

            }
        }else {
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        PageModel<MerchantInfoResponse> pageModel = new PageModel<>(pageNo,pageSize);
        pageModel.setCount(list1.size());
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }


    @ResponseBody
    @RequestMapping(value = "/selectByName",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> selectByName(int pageNo,int pageSize,String merchantName) {
        PageModel<MerchantInfoResponse> pageModel = new PageModel<>(pageNo,pageSize);

        List<MerchantInfoResponse> list1 = this.merchantInfoQueryService.getCount();
        List<MerchantInfoResponse> list = this.merchantInfoQueryService.selectByName((pageNo-1)*pageSize,pageSize,merchantName);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getDealerId()==0){
                    String proxyName = "金开门";
                    list.get(i).setProxyName(proxyName);
                }

            }
        }else {
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        pageModel.setCount(list1.size());
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);

    }


}
