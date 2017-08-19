package com.jkm.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.jkm.api.enums.EnumOpenCardStatus;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.requestparam.OpenCardQueryRequest;
import com.jkm.api.helper.requestparam.OpenCardRequest;
import com.jkm.api.helper.responseparam.OpenCardQueryResponse;
import com.jkm.api.service.OpenCardService;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.hss.merchant.dao.OpenCardRecordDao;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.OpenCardRecord;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Slf4j
@Service
public class OpenCardServiceImpl implements OpenCardService {
    @Autowired
    private OpenCardRecordDao openCardRecordDao;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private HttpClientFacade httpClientFacade;

    /**
     * {@inheritDoc}
     *
     * @param openCardRequest
     * @return
     */
    @Override
    public String kuaiPayOpenCard(final OpenCardRequest openCardRequest) {
        if (StringUtils.isBlank(openCardRequest.getBindCardReqNo())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"绑卡流水不能为空");
        }
        if (StringUtils.isBlank(openCardRequest.getMerchantNo())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"商户号不能为空");
        }
        if (StringUtils.isBlank(openCardRequest.getCardNo())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"卡号不能为空");
        }
        if (StringUtils.isBlank(openCardRequest.getFrontUrl())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"前台通知地址不能为空");
        }
        OpenCardRecord record = openCardRecordDao.selectByBindCardReqNo(openCardRequest.getBindCardReqNo());
        if(record!=null){
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQSN_EXIST);
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.getByMarkCode(openCardRequest.getMerchantNo());
        if (!merchantInfoOptional.isPresent()){
            throw new JKMTradeServiceException(JKMTradeErrorCode.MERCHANT_NOT_EXIST);
        }
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(openCardRequest.getCardNo());
        if (!bankCardBinOptional.isPresent()) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.CARDNO_FORMAT_ERROR);
        }
        // TODO 开卡已经成功了，是否可以再次开卡
        final OpenCardRecord openCardRecord = new OpenCardRecord();
        openCardRecord.setBindCardReqNo(openCardRequest.getBindCardReqNo());
        openCardRecord.setMerchantNo(openCardRequest.getMerchantNo());
        openCardRecord.setCardNo(openCardRequest.getCardNo());
        openCardRecord.setFrontUrl(openCardRequest.getFrontUrl());
        openCardRecord.setStatus(EnumOpenCardStatus.SUBMIT.getId());
        openCardRecord.setCreateTime(new Date());
        openCardRecordDao.insert(openCardRecord);
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("merchantNo", openCardRequest.getMerchantNo());
        paramsMap.put("merchantOrderNo", openCardRequest.getBindCardReqNo());
        paramsMap.put("merchantReqTime", openCardRecord.getCreateTime().getTime() + "");
        paramsMap.put("cardNo", openCardRequest.getCardNo());
        try {
            final String result = this.httpClientFacade.jsonPost(MerchantConsts.getMerchantConfig().cardOpen(), paramsMap);
            if (result != null && !"".equals(result)) {
                final JSONObject jo = JSON.parseObject(result);
                if(jo.getIntValue("code") == 1){
                    final String html = jo.getJSONObject("result").getString("html");
                    log.info("商户号[{}]-流水号[{}]，开卡请求网关成功，html-[{}]", openCardRequest.getMerchantNo(), openCardRequest.getBindCardReqNo(), html);
                    return html;
                }
                throw new JKMTradeServiceException(JKMTradeErrorCode.COMMON_ERROR, jo.getString("msg"));
            }
            throw new JKMTradeServiceException(JKMTradeErrorCode.SYS_ERROR);
        } catch (final Throwable e) {
            log.error("商户号[" + openCardRequest.getMerchantNo() + "]-流水号[" + openCardRequest.getBindCardReqNo() + "]，开卡请求网关异常", e);
            throw new JKMTradeServiceException(JKMTradeErrorCode.SYS_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param openCardQueryRequest
     * @param response
     * @return
     */
    @Override
    public void kuaiPayOpenCardQuery(final OpenCardQueryRequest openCardQueryRequest, final OpenCardQueryResponse response) {
        if (StringUtils.isBlank(openCardQueryRequest.getCardNo())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"卡号不能为空");
        }
        if (StringUtils.isBlank(openCardQueryRequest.getMerchantNo())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"商户号不能为空");
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.getByMarkCode(openCardQueryRequest.getMerchantNo());
        if (!merchantInfoOptional.isPresent()){
            throw new JKMTradeServiceException(JKMTradeErrorCode.MERCHANT_NOT_EXIST);
        }
        final MerchantInfo merchant = merchantInfoOptional.get();
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(openCardQueryRequest.getCardNo());
        if (!bankCardBinOptional.isPresent()) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.CARDNO_FORMAT_ERROR);
        }
        final AccountBank accountBank = accountBankService.selectCreditListByBankNo(merchantInfoOptional.get().getAccountId(),openCardQueryRequest.getCardNo());
        if(accountBank == null){
            try {
                final Map<String, String> paramsMap = new HashMap<>();
                final OpenCardRecord openCardRecord = this.openCardRecordDao.selectCurrentOneByCardNo(openCardQueryRequest.getCardNo());
                if (null == openCardRecord) {
                    response.setCardNo(openCardQueryRequest.getCardNo());
                    throw new JKMTradeServiceException(JKMTradeErrorCode.COMMON_ERROR, "开卡记录不存在");
                }
                paramsMap.put("merchantNo", openCardQueryRequest.getMerchantNo());
                paramsMap.put("merchantOrderNo", openCardRecord.getBindCardReqNo());
                paramsMap.put("merchantReqTime",  openCardRecord.getCreateTime().getTime() + "");
                final String result = this.httpClientFacade.jsonPost(MerchantConsts.getMerchantConfig().cardQuery(), paramsMap);
                if (result != null && !"".equals(result)) {
                    final JSONObject jo = JSON.parseObject(result);
                    if(jo.getIntValue("code") == 1){
                        if ("1".equals(jo.getJSONObject("result").getString("activateStatus"))) {
                            final String token = jo.getJSONObject("result").getString("token");
                            log.info("商户号[{}]-流水号[{}]，开卡查询成功--开卡成功-token[{}]", openCardQueryRequest.getMerchantNo(), openCardQueryRequest.getCardNo(), token);
                            openCardRecordDao.updateStatusByBindCardReqNo(openCardRecord.getBindCardReqNo(), 1);
                            accountBankService.bindCard(merchant.getAccountId(), openCardQueryRequest.getCardNo(), bankCardBinOptional.get().getBankName(),
                                    merchant.getReserveMobile(), bankCardBinOptional.get().getShorthand(), token);
                            response.setCardNo(MerchantSupport.decryptBankCard(accountBank.getBankNo()));
                            response.setMobile(merchant.getReserveMobile());
                            response.setBankName(accountBank.getBankName());
                            response.setBankBin(accountBank.getBankBin());
                            response.setIsDefault( 1 == accountBank.getIsDefault() ? "Y" : "N");
                            response.setBindStatus("SUCCESS");
                            return;
                        }
                        openCardRecordDao.updateStatusByBindCardReqNo(openCardRecord.getBindCardReqNo(), 2);
                        log.info("商户号[{}]-流水号[{}]，开卡查询成功--开卡失败", openCardQueryRequest.getMerchantNo(), openCardQueryRequest.getCardNo());
                        response.setCardNo(openCardQueryRequest.getCardNo());
                        response.setBindStatus("FAIL");
                        return;
                    }
                    log.info("商户号[{}]-流水号[{}]，开卡查询失败", openCardQueryRequest.getMerchantNo(), openCardQueryRequest.getCardNo());
                    response.setCardNo(openCardQueryRequest.getCardNo());
                    response.setBindStatus("UNKNOWN");
                    return;
                }
                throw new JKMTradeServiceException(JKMTradeErrorCode.SYS_ERROR);
            } catch (final Throwable e) {
                log.error("商户号[" + openCardQueryRequest.getMerchantNo() + "]， 卡号[" + openCardQueryRequest.getCardNo() + "]，查询网关开卡异常", e);
                throw new JKMTradeServiceException(JKMTradeErrorCode.SYS_ERROR, "网关异常");
            }
        }else{
            //直接返回
            log.info("商户号[{}]-流水号[{}]，开卡查询成功--开卡成功-", openCardQueryRequest.getMerchantNo(), openCardQueryRequest.getCardNo());
            response.setCardNo(MerchantSupport.decryptBankCard(accountBank.getBankNo()));
            response.setMobile(merchant.getReserveMobile());
            response.setBankName(accountBank.getBankName());
            response.setBankBin(accountBank.getBankBin());
            response.setIsDefault( 1 == accountBank.getIsDefault() ? "Y" : "N");
            response.setBindStatus("SUCCESS");
        }
    }
}
