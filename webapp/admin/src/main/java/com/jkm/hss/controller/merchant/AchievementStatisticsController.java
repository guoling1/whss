package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.AchievementStatisticsResponse;
import com.jkm.hss.bill.entity.QueryOrderRequest;
import com.jkm.hss.bill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by zhangbin on 2017/6/10.
 */
@Controller
@RequestMapping(value = "'/admin/AchievementStatistics")
public class AchievementStatisticsController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/getAchievement",method = RequestMethod.POST)
    public CommonResponse getAchievement(@RequestBody QueryOrderRequest req){
        final PageModel<AchievementStatisticsResponse> pageModel = new PageModel<AchievementStatisticsResponse>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String checkedTime = sdf.format(date);
        req.setStartTime(checkedTime);
        List<AchievementStatisticsResponse> list = this.orderService.getAchievement(req);

        int count = this.orderService.getAchievementCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", list);
    }
}
