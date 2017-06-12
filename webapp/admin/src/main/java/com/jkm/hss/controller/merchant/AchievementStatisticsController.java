package com.jkm.hss.controller.merchant;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.bill.entity.QueryOrderRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by zhangbin on 2017/6/10.
 */
@Controller
@RequestMapping(value = "'/admin/AchievementStatistics")
public class AchievementStatisticsController {

    @ResponseBody
    @RequestMapping(value = "/getAchievement",method = RequestMethod.POST)
    public CommonResponse getAchievement(@RequestBody QueryOrderRequest req){

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", null);
    }
}
