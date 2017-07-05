package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Allen on 2017/5/12.
 */
public interface HsyMembershipService {
    /**app*/
    public String insertMemshipCard(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMemshipCards(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMemberQr(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMemshipCardsInfo(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateMemshipCardsStatus(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateMemshipCard(String dataParam, AppParam appParam)throws ApiHandleException;
    /**web*/
    public AppPolicyConsumer findConsumerByOpenID(String openID);
    public AppPolicyConsumer findConsumerByUserID(String userID);
    public AppPolicyMember findMemberByCIDAndUID(Long cid, Long uid);
    public AppPolicyMember findMemberByCIDAndMCID(Long cid, Long mcid);
    public List findMemberCardByUID(Long uid);
    public AppPolicyConsumer insertOrUpdateConsumer(AppPolicyConsumer appPolicyConsumer);
    public AppPolicyMember saveMember(Long cid,Long mcid,Integer status,Long accountID,Long receiptAccountID);
    public AppPolicyConsumer findConsumerByCellphone(String consumerCellphone);
    public void insertVcode(String sn, String code, String cellphone, Integer type);
    public AppAuVerification findRightVcode(String cellphone);
    public AppPolicyMember findMemberInfoByID(Long mid);
    public AppPolicyRechargeOrder saveOrder(AppPolicyMember appPolicyMember, String type,String source,BigDecimal amount);
    public void updateOrder(AppPolicyRechargeOrder appPolicyRechargeOrder,String tradeNO,Long tradeID);
}
