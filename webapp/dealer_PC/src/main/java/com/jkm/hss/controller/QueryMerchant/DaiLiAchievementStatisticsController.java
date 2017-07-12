package com.jkm.hss.controller.QueryMerchant;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.entity.AchievementStatisticsResponse;
import com.jkm.hss.bill.entity.QueryOrderRequest;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2017/6/14.
 */
@Slf4j
@Controller
@RequestMapping(value = "/daili/DaiLiAchievementStatistics")
public class DaiLiAchievementStatisticsController extends BaseController {
    @Autowired
    private OSSClient ossClient;

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/getDaiLiAchievement",method = RequestMethod.POST)
    public CommonResponse getDaiLiAchievement(@RequestBody QueryOrderRequest req) throws ParseException {
        final PageModel<AchievementStatisticsResponse> pageModel = new PageModel<AchievementStatisticsResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());

        Date begin =null;
        Date end =null;
        if (req.getStartTime1() !=null && req.getEndTime()!=null && req.getStartTime1()!="" && req.getEndTime()!=""){
            begin = DateFormatUtil.parse(req.getStartTime1()+ " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            end  = DateFormatUtil.parse(req.getEndTime() + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            String s = req.getStartTime1() + "~" + req.getEndTime();
            req.setCreateTime(s);
            req.setBegin(begin);
            req.setEnd(end);
        }

        long dealerId = super.getDealerId();
        req.setDealerId(dealerId);
        List<AchievementStatisticsResponse> list = this.orderService.getDaiLiAchievement(req);
        int count = this.orderService.getDaiLiAchievementCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        String downLoadExcel = downLoad(req);
        pageModel.setExt(downLoadExcel);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 导出全部
     * @return
     */
    private String downLoad(@RequestBody QueryOrderRequest req){
        final String fileZip = this.orderService.downloadDaiLiAchievement(req, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "Achievement.xls";
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url = null;
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
            return url.getHost() + url.getFile();
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }
}
