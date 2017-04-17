package com.jkm.hss.controller.bill;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.enums.EnumTradeType;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.bill.service.ProfitService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.QueryInfoByOrderNoRequest;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/4.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/order")
public class OrderController extends BaseController {

    @Autowired
    private OSSClient ossClient;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private ProfitService profitService;
    /**
     * 提现审核时（查询用户信息）
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryInfoByOrderNo", method = RequestMethod.POST)
    public CommonResponse queryInfoByOrderNo(@RequestBody QueryInfoByOrderNoRequest queryInfoByOrderNoRequest) {
        final String orderNo = queryInfoByOrderNoRequest.getOrderNo();
        if (StringUtils.isEmpty(orderNo)) {
            return CommonResponse.simpleResponse(-1, "交易订单号不能空");
        }
        long accountId;
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(orderNo);
        if (orderOptional.isPresent()) {
            final Order order = orderOptional.get();
            if (EnumTradeType.WITHDRAW.getId() != order.getTradeType()) {
                return CommonResponse.simpleResponse(-1, "交易单[提现单]异常");
            }
            accountId = order.getPayer();
        } else {
            final SettlementRecord settlementRecord = this.settlementRecordService.getBySettleNo(orderNo).get();
            accountId = settlementRecord.getAccountId();
        }
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.getByAccountId(accountId);
        if (merchantInfoOptional.isPresent()) {
            final MerchantInfo merchantInfo = merchantInfoOptional.get();
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("accountId", merchantInfo.getAccountId())
                    .addParam("userName", merchantInfo.getMerchantName())
                    .addParam("userType", "商户")
                    .build();
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getByAccountId(accountId);
        if (dealerOptional.isPresent()) {
            final Dealer dealer = dealerOptional.get();
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("accountId", dealer.getAccountId())
                    .addParam("userName", dealer.getProxyName())
                    .addParam("userType", "代理商")
                    .build();
        }
        return CommonResponse.simpleResponse(-1, "没有查到打款用户信息");
    }

    /**
     * 提现
     * @param req
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawList", method = RequestMethod.POST)
    public CommonResponse withdrawList(@RequestBody WithdrawRequest req) throws ParseException {
        final PageModel<WithdrawResponse> pageModel = new PageModel<WithdrawResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
            }
        List<WithdrawResponse> list = this.orderService.withdrawList(req);
        long count = this.orderService.getNo(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1,"success",pageModel);

        }

    /**
     * 提现统计
     * @param req
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawAmount", method = RequestMethod.POST)
    public CommonResponse withdrawAmount(@RequestBody final WithdrawRequest req) throws ParseException {
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        WithdrawResponse result = orderService.withdrawAmount(req);
        BigDecimal x = new BigDecimal("0.0");
        WithdrawResponse response = new WithdrawResponse();
        if (result==null){
            response.setPoundage(x);
            response.setTradeAmount(x);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", response);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", result);

    }

    /**
     * 提现详情
     * @param req
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawDetail", method = RequestMethod.POST)
    public CommonResponse withdrawDetail(@RequestBody WithdrawRequest req) throws ParseException {
        long idd = req.getIdd();
        long idm = req.getIdm();
        String createTimes = req.getCreateTimes();
        JSONObject jsonObject = new JSONObject();
        if (idd>0){
            WithdrawResponse results = orderService.withdrawDetail(idd,createTimes);
            List<PlayResponse> list = orderService.getPlayMoney(req.getOrderNo());
            List<ProfitResponse> list1 = profitService.getInfo(req.getBusinessOrderNo());
            jsonObject.put("results",results);
            jsonObject.put("list",list);
            jsonObject.put("list1",list1);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", jsonObject);
        }
        if (idm>0){
            WithdrawResponse results = orderService.withdrawDetails(idm,createTimes);
            List<PlayResponse> list = orderService.getPlayMoney(req.getOrderNo());
            List<ProfitResponse> list1 = profitService.getInfo(req.getBusinessOrderNo());
            jsonObject.put("results",results);
            jsonObject.put("list",list);
            jsonObject.put("list1",list1);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", jsonObject);
        }
        return CommonResponse.simpleResponse(-1, "查询异常");
    }

    /**
     * 导出全部
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/downLoad", method = RequestMethod.POST)
    private String downLoad(@RequestBody WithdrawRequest req){
        final String fileZip = this.orderService.downLoad(req, ApplicationConsts.getApplicationConfig().ossBucke());
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "withdraw.xls";
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
