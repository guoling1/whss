package com.jkm.hss.controller.admin;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.CompanyProfitDetail;
import com.jkm.hss.dealer.entity.DailyProfitDetail;
import com.jkm.hss.dealer.entity.ShallProfitDetail;
import com.jkm.hss.dealer.service.CompanyProfitDetailService;
import com.jkm.hss.dealer.service.DailyProfitDetailService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.helper.DealerProfitQueryParam;
import com.jkm.hss.helper.PageQueryParam;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
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
import java.util.Map;

/**
 * Created by yuxiang on 2016-12-08.
 */
@RequestMapping(value = "/admin/profit")
@Controller
@Slf4j
public class ProfitController extends BaseController {

    @Autowired
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private DailyProfitDetailService dailyProfitDetailService;
    @Autowired
    private CompanyProfitDetailService companyProfitDetailService;
    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * 公司分润
     * @param pageQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/companyProfit")
    public CommonResponse getCompanyProfit(@RequestBody final DealerProfitQueryParam pageQueryParam){
        try{
            PageModel<DailyProfitDetail> pageModel = this.dailyProfitDetailService.selectCompanyByParam(pageQueryParam.getBeginProfitDate(),
                    pageQueryParam.getDealerName(), pageQueryParam.getEndProfitDate(), pageQueryParam.getPageNo(), pageQueryParam.getPageSize());
            return CommonResponse.objectResponse(1, "success", pageModel);
        }catch (final Throwable throwable){
            log.error("查询一级代理商分润异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "查询一级代理商分润异常");
    }

    /**
     * 公司分润详情
     * @param pageQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/companyProfit/detail")
    public CommonResponse getCompanyProfitDeatail(@RequestBody final DealerProfitQueryParam pageQueryParam){

            try{
                final DailyProfitDetail dailyProfitDetail = this.dailyProfitDetailService.selectById(pageQueryParam.getDailyProfitId());

                final List<CompanyProfitDetail> companyList = this.companyProfitDetailService.selectByProfitDate(dailyProfitDetail.getStatisticsDate());
                final List<Long> merchantIdList = this.companyProfitDetailService.getMerchantIdByProfitDate(dailyProfitDetail.getStatisticsDate());
                final List<MerchantInfo> merchantInfoList = this.merchantInfoService.batchGetMerchantInfo(merchantIdList);
                final Map<Long, MerchantInfo> map = Maps.uniqueIndex(merchantInfoList, new Function<MerchantInfo, Long>() {
                    @Override
                    public Long apply(MerchantInfo input) {
                        return input.getId();
                    }
                });
                final List<ShallProfitDetail> dealerList = this.shallProfitDetailService.selectCompanyByProfitDate(dailyProfitDetail.getStatisticsDate());
                final List<Long> merchantIdListOther = this.shallProfitDetailService.getMerchantIdByProfitDate(dailyProfitDetail.getStatisticsDate());
                final List<MerchantInfo> merchantInfoOtherList = this.merchantInfoService.batchGetMerchantInfo(merchantIdList);
                final Map<Long, MerchantInfo> mapOther = Maps.uniqueIndex(merchantInfoOtherList, new Function<MerchantInfo, Long>() {
                    @Override
                    public Long apply(MerchantInfo input) {
                        return input.getId();
                    }
                });

            }catch (final Throwable throwable){
                log.error("查询公司分润详情异常:" + throwable.getMessage());
            }
            return CommonResponse.simpleResponse(-1, "查询公司分润明细异常");
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
            PageModel<DailyProfitDetail> pageModel = this.dailyProfitDetailService.selectFirstByParam(pageQueryParam.getBeginProfitDate(),
                    pageQueryParam.getDealerName(), pageQueryParam.getEndProfitDate(), pageQueryParam.getPageNo(), pageQueryParam.getPageSize());
            return CommonResponse.objectResponse(1, "success", pageModel);
        }catch (final Throwable throwable){
            log.error("查询一级代理商分润异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "查询一级代理商分润异常");

    }

    /**
     * 一级代理分润详情
     * @param pageQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstDealer/detail")
    public CommonResponse getFirstDealerDeatail(@RequestBody final DealerProfitQueryParam pageQueryParam){

        try{
            final List<ShallProfitDetail> list = this.shallProfitDetailService.getFirstDealerDeatail(pageQueryParam.getDailyProfitId());
            return CommonResponse.objectResponse(1, "success", list);
        }catch (final Throwable throwable){
            log.error("查询一级代理商分润详情异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "查询一代分润明细异常");
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
            PageModel<DailyProfitDetail> pageModel = this.dailyProfitDetailService.selectSecondByParam(pageQueryParam.getBeginProfitDate(),
                    pageQueryParam.getDealerName(), pageQueryParam.getEndProfitDate(), pageQueryParam.getPageNo(), pageQueryParam.getPageSize());
            return CommonResponse.objectResponse(1, "success", pageModel);
        }catch (final Throwable throwable){
            log.error("查询二级代理商分润异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "查询二级代理商分润异常");
    }

    /**
     * 二级代理分润详情
     * @param pageQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondDealer/detail")
    public CommonResponse getSecondDealerDeatail(@RequestBody final DealerProfitQueryParam pageQueryParam){

        try{
            final List<ShallProfitDetail> list = this.shallProfitDetailService.getSecondDealerDeatail(pageQueryParam.getDailyProfitId());
            return CommonResponse.objectResponse(1, "success", list);
        }catch (final Throwable throwable){
            log.error("查询二级代理商分润详情异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "查询二代分润明细异常");
    }
}
