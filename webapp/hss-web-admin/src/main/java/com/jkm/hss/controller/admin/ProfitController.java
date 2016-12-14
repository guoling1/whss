package com.jkm.hss.controller.admin;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.DailyProfitDetail;
import com.jkm.hss.dealer.service.DailyProfitDetailService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.helper.DealerProfitQueryParam;
import com.jkm.hss.helper.PageQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2016-12-08.
 */
@RequestMapping(value = "/profit")
@Controller
@Slf4j
public class ProfitController extends BaseController {

    @Autowired
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private DailyProfitDetailService dailyProfitDetailService;

    /**
     * 公司分润
     * @param pageQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/companyProfit")
    public CommonResponse getCompanyProfit(final PageQueryParam pageQueryParam){

        return null;
    }

    /**
     * 一级代理商分润
     * @param pageQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstProfit")
    public CommonResponse getFirstProfit(@RequestBody final DealerProfitQueryParam pageQueryParam){
        try{
            PageModel<DailyProfitDetail> pageModel = this.dailyProfitDetailService.selectFirstByParam(pageQueryParam.getDealerId(),
                    pageQueryParam.getDealerName(), pageQueryParam.getProfitDate(), pageQueryParam.getPageNO(), pageQueryParam.getPageSize());
            return CommonResponse.objectResponse(1,"success",pageModel);
        }catch (final Throwable throwable){
            log.error("查询一级代理商分润异常");
        }
        return CommonResponse.simpleResponse(-1, "查询一级代理商分润异常");

    }

    /**
     * 二级代理商分润
     * @param pageQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondProfit")
    public CommonResponse getSecondProfit(@RequestBody final DealerProfitQueryParam pageQueryParam){

        try{
            PageModel<DailyProfitDetail> pageModel = this.dailyProfitDetailService.selectSecondByParam(pageQueryParam.getDealerId(),
                    pageQueryParam.getDealerName(), pageQueryParam.getProfitDate(), pageQueryParam.getPageNO(), pageQueryParam.getPageSize());
            return CommonResponse.objectResponse(1,"success",pageModel);
        }catch (final Throwable throwable){
            log.error("查询二级代理商分润异常");
        }
           return CommonResponse.simpleResponse(-1, "查询二级代理商分润异常");
    }

    /**
     *  下载一级代理商分润数据
     *
     * @param pageQueryParam
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/first/excelExport", method = RequestMethod.GET)
    public void firstExportExcel(@RequestBody final DealerProfitQueryParam pageQueryParam, final HttpServletResponse response)
            throws Exception {
        PageModel<DailyProfitDetail> pageModel = this.dailyProfitDetailService.selectFirstByParam(pageQueryParam.getDealerId(),
                pageQueryParam.getDealerName(), pageQueryParam.getProfitDate(), pageQueryParam.getPageNO(), pageQueryParam.getPageSize());

        final List<List<String>> list = new ArrayList<>();
        for (final DailyProfitDetail input : pageModel.getRecords()) {
            final List<String> obj = new ArrayList<>();
            obj.add(String.valueOf(input.getTotalMoney()));
            obj.add(input.getStatisticsDate());
            obj.add(input.getDealerName());
            obj.add(String.valueOf(input.getSecondDealerId()));
            obj.add(String.valueOf(input.getCollectMoney()));
            obj.add(String.valueOf(input.getWithdrawMoney()));
            list.add(obj);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename="+ DateFormatUtil.format(new Date(),DateFormatUtil.yyyy_MM_dd)+".xls");
        final OutputStream outputStream = response.getOutputStream();

        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<String> head = new ArrayList<>();
        head.add("一级代理分润");
        head.add("分润日期");
        head.add("代理商名称");
        head.add("代理商编号");
        head.add("收单交易分润金额");
        head.add("提现分润金额");

        list.add(0, head);
        excelSheetVO.setName("代理商分润信息");
        excelSheetVO.setDatas(list);
        final List<ExcelSheetVO> excelSheetVOs = new ArrayList<>();
        excelSheetVOs.add(excelSheetVO);
        ExcelUtil.exportExcel(excelSheetVOs , outputStream);

        outputStream.flush();
        outputStream.close();
    }

    /**
     *  下载二级代理商分润数据
     *
     * @param pageQueryParam
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/second/excelExport", method = RequestMethod.GET)
    public void secondExportExcel(@RequestBody final DealerProfitQueryParam pageQueryParam, final HttpServletResponse response)
            throws Exception {
        PageModel<DailyProfitDetail> pageModel = this.dailyProfitDetailService.selectSecondByParam(pageQueryParam.getDealerId(),
                pageQueryParam.getDealerName(), pageQueryParam.getProfitDate(), pageQueryParam.getPageNO(), pageQueryParam.getPageSize());

        final List<List<String>> list = new ArrayList<>();
        for (final DailyProfitDetail input : pageModel.getRecords()) {
            final List<String> obj = new ArrayList<>();
            obj.add(input.getFirstDealerName());
            obj.add(String.valueOf(input.getTotalMoney()));
            obj.add(input.getStatisticsDate());
            obj.add(input.getDealerName());
            obj.add(String.valueOf(input.getSecondDealerId()));
            obj.add(String.valueOf(input.getCollectMoney()));
            obj.add(String.valueOf(input.getWithdrawMoney()));
            list.add(obj);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename="+ DateFormatUtil.format(new Date(),DateFormatUtil.yyyy_MM_dd)+".xls");
        final OutputStream outputStream = response.getOutputStream();

        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<String> head = new ArrayList<>();
        head.add("所属上级");
        head.add("二级代理分润");
        head.add("分润日期");
        head.add("代理商名称");
        head.add("代理商编号");
        head.add("收单交易分润金额");
        head.add("提现分润金额");

        list.add(0, head);
        excelSheetVO.setName("代理商分润信息");
        excelSheetVO.setDatas(list);
        final List<ExcelSheetVO> excelSheetVOs = new ArrayList<>();
        excelSheetVOs.add(excelSheetVO);
        ExcelUtil.exportExcel(excelSheetVOs , outputStream);

        outputStream.flush();
        outputStream.close();
    }
}
