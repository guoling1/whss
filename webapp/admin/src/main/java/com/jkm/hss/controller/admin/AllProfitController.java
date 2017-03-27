package com.jkm.hss.controller.admin;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.helper.request.CompanyPrifitRequest;
import com.jkm.hss.merchant.helper.response.CompanyProfitResponse;
import com.jkm.hss.merchant.service.AllProfitService;
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
 * Created by yuxiang on 2016-12-08.
 */
@RequestMapping(value = "/admin/allProfit")
@Controller
@Slf4j
public class AllProfitController extends BaseController {

    @Autowired
    private AllProfitService allProfitService;

    @Autowired
    private OSSClient ossClient;

    /**
     * 公司分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/companyProfit", method = RequestMethod.POST)
    public CommonResponse getCompanyProfit(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<CompanyProfitResponse> list = allProfitService.selectCompanyProfit(req);
        List<CompanyProfitResponse> count = allProfitService.selectCompanyProfitCount(req);
        pageModel.setCount(count.size());
        pageModel.setRecords(list);
        String downLoadExcel = downLoad(req);
        pageModel.setExt(downLoadExcel);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);

    }

    /**
     * 公司分润统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/companyAmount", method = RequestMethod.POST)
    public CommonResponse companyAmount(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        String result = allProfitService.companyAmount(req);
//        BigDecimal x = new BigDecimal("0.0");
        if(result==null||("").equals(result)){
            String res="0";
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", res);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", result);

    }

    /**
     * 导出公司分润
     * @return
     */
    private String downLoad(@RequestBody CompanyPrifitRequest req) throws ParseException {
        final String fileZip = this.allProfitService.downloadExcel(req, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "companyProfit.xls";
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

    /**
     * 公司分润详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/companyProfitDetail", method = RequestMethod.POST)
    public CommonResponse getCompanyProfitDeatail(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        List<CompanyProfitResponse> list = allProfitService.selectCompanyProfitDetails(req);
        int count = allProfitService.selectCompanyProfitDetailsCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询完成", pageModel);
    }

    /**
     * 公司分润详情统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ProfitDetailAmount", method = RequestMethod.POST)
    public CommonResponse ProfitDetailAmount(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        CompanyProfitResponse result = allProfitService.ProfitDetailAmount(req);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", result);
    }

    /**
     * 一级代理商分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstProfit", method = RequestMethod.POST)
    public CommonResponse getFirstProfit(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<CompanyProfitResponse> list = allProfitService.selectOneProfit(req);
        List<CompanyProfitResponse> count = allProfitService.selectOneProfitCount(req);
        pageModel.setCount(count.size());
        pageModel.setRecords(list);
        String downLoadExcel = downLoadOneDealer(req);
        pageModel.setExt(downLoadExcel);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);

    }

    /**
     * 一级代理商分润统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstAmount", method = RequestMethod.POST)
    public CommonResponse getFirstAmount(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        String result = allProfitService.getFirstAmount(req);
        if(result==null||("").equals(result)){
            String res="0";
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", res);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", result);

    }

    /**
     * 导出一级代理分润
     * @return
     */
    private String downLoadOneDealer(@RequestBody CompanyPrifitRequest req) throws ParseException {
        final String fileZip = this.allProfitService.downloadExcelOneDealer(req, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "companyProfit.xls";
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

    /**
     * 一级代理分润详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstDealerDetail", method = RequestMethod.POST)
    public CommonResponse getFirstDealerDeatail(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());

        List<CompanyProfitResponse> list = allProfitService.selectOneProfitDetails(req);

        int count = allProfitService.selectOneProfitDetailsCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 一级代理分润详情统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstDetailAmount", method = RequestMethod.POST)
    public CommonResponse firstDetailAmount(@RequestBody final CompanyPrifitRequest req) throws ParseException {

        CompanyProfitResponse result = allProfitService.firstDetailAmount(req);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", result);
    }

    /**
     * 二级代理商分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondProfit", method = RequestMethod.POST)
    public CommonResponse getSecondProfit(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<CompanyProfitResponse> list = allProfitService.selectTwoProfit(req);
        pageModel.setCount(list.size());
        pageModel.setRecords(list);
        String downLoadExcel = downLoadTwoDealer(req);
        pageModel.setExt(downLoadExcel);


        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 二级代理商分润统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondAmount", method = RequestMethod.POST)
    public CommonResponse secondAmount(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        String result = allProfitService.secondAmount(req);
        if(result==null||("").equals(result)){
            String res="0";
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", res);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", result);
    }

    /**
     * 导出二级代理分润
     * @return
     */
    private String downLoadTwoDealer(@RequestBody CompanyPrifitRequest req) throws ParseException {
        final String fileZip = this.allProfitService.downloadExcelTwoDealer(req, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "companyProfit.xls";
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

    /**
     * 二级代理分润详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ secondDealerDetail", method = RequestMethod.POST)
    public CommonResponse getSecondDealerDeatail(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        List<CompanyProfitResponse> list = allProfitService.selectTwoProfitDetails(req);
        int count = allProfitService.selectTwoProfitDetailsCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 二级代理分润详情统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondDetailAmount", method = RequestMethod.POST)
    public CommonResponse secondDetailAmount(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        CompanyProfitResponse result = allProfitService.secondDetailAmount(req);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", result);
    }
}
