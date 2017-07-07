package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.*;

import java.text.ParseException;
import java.util.List;

/**
 * Created by zhangbin on 2016/12/2.
 */
public interface QueryMerchantInfoRecordService {

    /**
     * 查询商户详情
     * @param merchantInfo
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfo) throws ParseException;




    /**
     * 审核记录
     * @param merchantInfo
     * @return
     */
    List<LogResponse> getLog(MerchantInfoResponse merchantInfo) throws ParseException;

    /**
     * 查询推荐信息
     * @param id
     * @return
     */
    ReferralResponse getRefInformation(long id);

    /**
     * 查询手续费率
     * @param id
     * @return
     */
    SettleResponse getSettle(long id);

    /**
     * 查询推荐信息
     * @param id
     * @return
     */
    MerchantInfoResponse getrecommenderInfo(long id);

    /**
     * hss补填联行号
     * @param req
     */
    void saveNo(SaveLineNoRequest req);

    /**
     * hss补填联行号（除审核通过之外）
     * @param req
     */
    void saveNo1(SaveLineNoRequest req);

    /**
     * 查状态
     * @param id
     * @return
     */
    int getStatus(long id);

    /**
     * 查找账户id
     * @param id
     * @return
     */
    int getAccountId(long id);
}
