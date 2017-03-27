package com.jkm.hss.controller.QueryMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.QueryMerchantRequest;
import com.jkm.hss.dealer.entity.QueryMerchantResponse;
import com.jkm.hss.dealer.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhangbin on 2017/3/24.
 */
@Controller
@RequestMapping(value = "/queryMerchant")
public class QueryMerchantController extends BaseController {

    @Autowired
    private DealerService dealerService;

    @ResponseBody
    @RequestMapping(value = "/dealerMerchantList", method = RequestMethod.POST)
    public CommonResponse dealerMerchantList(@RequestBody QueryMerchantRequest req) {
        final PageModel<QueryMerchantResponse> pageModel = new PageModel<QueryMerchantResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        long dealerId = super.getDealerId();
        int level = super.getDealer().get().getLevel();
        String proxyName = super.getDealer().get().getProxyName();
        req.setDealerId(dealerId);
        List<QueryMerchantResponse> list = dealerService.dealerMerchantList(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (level==1){
                    list.get(i).setProxyName(proxyName);
                }
            }
        }
        int count = dealerService.dealerMerchantCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

}
