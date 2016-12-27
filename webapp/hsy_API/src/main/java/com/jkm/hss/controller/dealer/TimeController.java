package com.jkm.hss.controller.dealer;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.service.DailyProfitDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yuxiang on 2016-12-09.
 */
@RequestMapping("/time")
@Controller
@Slf4j
public class TimeController extends BaseController {

    @Autowired
    private DailyProfitDetailService dailyProfitDetailService;
    /**
     * 定时任务
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dailyProfit", method = RequestMethod.GET)
    public CommonResponse dailyProfit() {
        try{
            this.dailyProfitDetailService.dailyProfitCount();
            return CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable throwable){
            log.error("每日分润定时任务运行异常,异常信息:" + throwable.getMessage());
        }
        return  CommonResponse.simpleResponse(-1, "fail");
    }
}
