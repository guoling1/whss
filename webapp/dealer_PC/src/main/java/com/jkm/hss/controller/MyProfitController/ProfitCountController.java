package com.jkm.hss.controller.MyProfitController;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.ProfitCountRequest;
import com.jkm.hss.account.entity.ProfitCountRespons;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangbin on 2017/5/22.
 * 分润统计
 */
@Controller
@RequestMapping(value = "/daili/profitCount")
public class ProfitCountController {

    @Autowired
    private SplitAccountRecordService splitAccountRecordService;

    @ResponseBody
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public CommonResponse getCount(@RequestBody ProfitCountRequest request){
        final PageModel<ProfitCountRespons> pageModel = new PageModel<ProfitCountRespons>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
//        List<ProfitCountRespons> list = this.splitAccountRecordService.getProfit(request);
//        int count = this.splitAccountRecordService.getProfitCount(request);
//        pageModel.setCount(count);
//        pageModel.setRecords(list);
        return null;

    }

}
