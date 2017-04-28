package com.jkm.hss.controller.DealerController;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.helper.response.DealerReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.jkm.hss.dealer.helper.response.HomeReportResponse;

/**
 * Created by wayne on 17/4/27.
 */
@Slf4j
@RestController
@RequestMapping(value = "/daili/report")
public class ReportController extends BaseController {

    @ResponseBody
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public CommonResponse home(){
        HomeReportResponse response=new HomeReportResponse();
        response.setDealerReporthss(new DealerReport());
        response.setDealerReporthsy(new DealerReport());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", response);
    }
}
