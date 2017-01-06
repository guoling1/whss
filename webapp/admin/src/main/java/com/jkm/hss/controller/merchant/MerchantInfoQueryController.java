package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.service.MerchantInfoQueryService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Controller
@RequestMapping(value = "/admin/query")
public class MerchantInfoQueryController extends BaseController {

    @Autowired
    private MerchantInfoQueryService merchantInfoQueryService;



    @ResponseBody
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> getAll(@RequestBody MerchantInfoRequest req) {
        final PageModel<MerchantInfoResponse> pageModel = new PageModel<MerchantInfoResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        long count = this.merchantInfoQueryService.getCount(req);
        List<MerchantInfoResponse> list = this.merchantInfoQueryService.getAll(req);
        if (list == null){
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }

        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }


}
