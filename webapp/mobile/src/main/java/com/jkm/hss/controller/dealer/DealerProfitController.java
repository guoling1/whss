package com.jkm.hss.controller.dealer;

import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.helper.response.DealerProfitToMerchantResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.DailyProfitDetail;
import com.jkm.hss.dealer.service.DailyProfitDetailService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.merchant.service.OrderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yuxiang on 2016-11-28.
 */
@Slf4j
@Controller
@RequestMapping(value = "/dealer/profit")
public class DealerProfitController extends BaseController {

    @Autowired
    private DailyProfitDetailService dailyProfitDetailService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private OrderRecordService orderRecordService;
    /**
     * 我的分润详情(TO商户)
     *
     * @return
     */
    @RequestMapping(value = "toMerchant", method = RequestMethod.GET)
    public String toMerchant(final Model model) {

        try{
            final List<DailyProfitDetail> list = this.dailyProfitDetailService.toMerchant(this.getDealerId());
            if (CollectionUtils.isEmpty(list)){
                model.addAttribute("data", Collections.EMPTY_LIST);
                return "/dealer/aginDetailSec";
            }else{
                final Map<String, DealerProfitToMerchantResponse> map = new HashMap<>();
                for (DailyProfitDetail detail : list){
                    final DealerProfitToMerchantResponse response = map.get(detail.getStatisticsDate());
                    if (response == null){
                        final DealerProfitToMerchantResponse newResponse = new DealerProfitToMerchantResponse();
                        newResponse.setCreateTime(detail.getCreateTime());
                        newResponse.setDayCollectTotalMoney(detail.getCollectMoney());
                        newResponse.setDaywithdrawTotalMoney(detail.getWithdrawMoney());
                        newResponse.setDayTotalMoney(detail.getCollectMoney().add(detail.getWithdrawMoney()));
                        final ArrayList<DailyProfitDetail> dailyProfitDetails = Lists.newArrayList();
                        dailyProfitDetails.add(detail);
                        newResponse.setList(dailyProfitDetails);
                        newResponse.setProfitDate(detail.getStatisticsDate());
                        map.put(detail.getStatisticsDate(), newResponse);
                     }else{
                        final BigDecimal dayCollectTotalMoney = response.getDayCollectTotalMoney();
                        response.setDayCollectTotalMoney(dayCollectTotalMoney.add(detail.getCollectMoney()));
                        final BigDecimal daywithdrawTotalMoney = response.getDaywithdrawTotalMoney();
                        response.setDaywithdrawTotalMoney(daywithdrawTotalMoney.add(detail.getWithdrawMoney()));
                        response.setDayTotalMoney(dayCollectTotalMoney.add(daywithdrawTotalMoney));
                        final List<DailyProfitDetail> dailyProfitDetails = response.getList();
                        dailyProfitDetails.add(detail);
                        response.setList(dailyProfitDetails);
                        map.put(detail.getStatisticsDate(), response);
                     }
             }
                final List<DealerProfitToMerchantResponse> sortList = new ArrayList<>();
                for (Map.Entry<String , DealerProfitToMerchantResponse> entry : map.entrySet()){
                    sortList.add(entry.getValue());
                }
                Collections.sort(sortList, new Comparator<DealerProfitToMerchantResponse>() {
                    @Override
                    public int compare(DealerProfitToMerchantResponse o1, DealerProfitToMerchantResponse o2) {
                        return new Long(o1.getCreateTime().getTime()).compareTo(new Long(o2.getCreateTime().getTime()));
                    }
                });
                model.addAttribute("data", sortList);
                return "/dealer/aginDetailSec";
            }
        }catch(final Throwable throwable){
            log.error("获取分润详情异常,异常信息:" + throwable.getMessage());
        }
            return "/dealer/500";
    }

    /**
     * 我的分润详情(一级TO 二级)
     *
     * @return
     */
    @RequestMapping(value = "toDealer", method = RequestMethod.GET)
    public String toDealer(final Model model) {
        try{
//            final List<DailyProfitDetail> list = this.dailyProfitDetailService.toDealer(4);
            final List<DailyProfitDetail> list = this.dailyProfitDetailService.toDealer(this.getDealerId());
            if (CollectionUtils.isEmpty(list)){
                model.addAttribute("data", Collections.EMPTY_LIST);
                return "/dealer/aginDetail";
                //return  CommonResponse.objectResponse(1, "success", Collections.EMPTY_LIST);
            }else{
                final Map<String, DealerProfitToMerchantResponse> map = new HashMap<>();
                for (DailyProfitDetail detail : list){
                    final DealerProfitToMerchantResponse response = map.get(detail.getStatisticsDate());
                    if (response == null){
                        final DealerProfitToMerchantResponse newResponse = new DealerProfitToMerchantResponse();
                        newResponse.setCreateTime(detail.getCreateTime());
                        newResponse.setDayCollectTotalMoney(detail.getCollectMoney());
                        newResponse.setDaywithdrawTotalMoney(detail.getWithdrawMoney());
                        newResponse.setDayTotalMoney(detail.getCollectMoney().add(detail.getWithdrawMoney()));
                        final ArrayList<DailyProfitDetail> dailyProfitDetails = Lists.newArrayList();
                        dailyProfitDetails.add(detail);
                        newResponse.setList(dailyProfitDetails);
                        newResponse.setProfitDate(detail.getStatisticsDate());
                        map.put(detail.getStatisticsDate(), newResponse);
                    }else{
                        final BigDecimal dayCollectTotalMoney = response.getDayCollectTotalMoney();
                        response.setDayCollectTotalMoney(dayCollectTotalMoney.add(detail.getCollectMoney()));
                        final BigDecimal daywithdrawTotalMoney = response.getDaywithdrawTotalMoney();
                        response.setDaywithdrawTotalMoney(daywithdrawTotalMoney.add(detail.getWithdrawMoney()));
                        response.setDayTotalMoney(dayCollectTotalMoney.add(daywithdrawTotalMoney));
                        final List<DailyProfitDetail> dailyProfitDetails = response.getList();
                        dailyProfitDetails.add(detail);
                        response.setList(dailyProfitDetails);
                        map.put(detail.getStatisticsDate(), response);
                    }
                }
                final List<DealerProfitToMerchantResponse> sortList = new ArrayList<>();
                for (Map.Entry<String , DealerProfitToMerchantResponse> entry : map.entrySet()){
                    sortList.add(entry.getValue());
                }
                Collections.sort(sortList, new Comparator<DealerProfitToMerchantResponse>() {
                    @Override
                    public int compare(DealerProfitToMerchantResponse o1, DealerProfitToMerchantResponse o2) {
                        return new Long(o1.getCreateTime().getTime()).compareTo(new Long(o2.getCreateTime().getTime()));
                    }
                });
                model.addAttribute("data", sortList);
                return "/dealer/aginDetail";
            }
        }catch(final Throwable throwable){
            log.error("获取分润详情异常,异常信息:" + throwable.getMessage());
        }
       return "/dealer/500";
    }


    /**
     * 定时任务
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "timeRun", method = RequestMethod.GET)
    public CommonResponse timeRun() {
        try{
            this.dailyProfitDetailService.dailyProfitCount();
            return CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable throwable){
            log.error("每日分润定时任务运行异常,异常信息:" + throwable.getMessage());
        }
        return  CommonResponse.simpleResponse(-1, "fail");
    }
    /**
     * 测试
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public CommonResponse test() {
        System.out.println(new BigDecimal(0.2000).compareTo(new BigDecimal(0.2000)));
        System.out.println(new BigDecimal(0.2000).compareTo(new BigDecimal(0.2001)));
        //this.dailyProfitDetailService.dailyProfitCount();
       final OrderRecord orderRecord = this.orderRecordService.selectOrderId("JKM2016120113162064441368").get();
        this.dealerService.shallProfit(orderRecord);
        return null;
    }

}
