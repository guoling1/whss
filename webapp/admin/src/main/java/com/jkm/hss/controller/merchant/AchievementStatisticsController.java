package com.jkm.hss.controller.merchant;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.AchievementStatisticsResponse;
import com.jkm.hss.bill.entity.QueryOrderRequest;
import com.jkm.hss.bill.service.OrderService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by zhangbin on 2017/6/10.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/AchievementStatistics")
public class AchievementStatisticsController {
    @Autowired
    private OSSClient ossClient;

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/getAchievement",method = RequestMethod.POST)
    public CommonResponse getAchievement(@RequestBody QueryOrderRequest req) throws ParseException {
        final PageModel<AchievementStatisticsResponse> pageModel = new PageModel<AchievementStatisticsResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String checkedTime = sdf.format(date);
        req.setStartTime(checkedTime);
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<AchievementStatisticsResponse> list = this.orderService.getAchievement(req);
        int count = this.orderService.getAchievementCount(req);
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
        final String fileZip = this.orderService.downloadAchievement(req, ApplicationConsts.getApplicationConfig().ossBucke());

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
