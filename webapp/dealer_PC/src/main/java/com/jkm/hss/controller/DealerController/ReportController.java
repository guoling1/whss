package com.jkm.hss.controller.DealerController;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.helper.response.DealerReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.jkm.hss.dealer.helper.response.HomeReportResponse;
import com.jkm.hss.dealer.service.ReportService;
import com.jkm.base.common.util.DateFormatUtil;

import java.util.Date;
import java.util.Calendar;


/**
 * Created by wayne on 17/4/27.
 */
@Slf4j
@RestController
@RequestMapping(value = "/daili/report")
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;

    @ResponseBody
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public CommonResponse home() {
        long m_ndealerid=0;
        long m_naccountid=0;
        final Optional<Dealer> dealerOptional = getDealer();
        if (dealerOptional.isPresent()) {
            m_ndealerid= dealerOptional.get().getId();
            m_naccountid=dealerOptional.get().getAccountId();
        }
        Date nowD = new Date();
        //TODO计算后一天日期， 此功能后期结构优化调整
        Calendar cl = Calendar.getInstance();
        cl.setTime(nowD);
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day+1);
        String startTime = DateFormatUtil.format(nowD, "yyyy-MM-dd");
        String endTime = DateFormatUtil.format(cl.getTime(), "yyyy-MM-dd");
        HomeReportResponse response = reportService.getHomeReport(m_ndealerid,m_naccountid, startTime, endTime);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", response);
    }
}
