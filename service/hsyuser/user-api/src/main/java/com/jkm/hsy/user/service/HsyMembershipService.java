package com.jkm.hsy.user.service;

import com.jkm.base.common.util.Page;
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
    public String insertMembershipCard(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMembershipCards(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMemberQr(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMembershipCardsInfo(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateMembershipCardsStatus(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateMembershipCard(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMemberList(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMemberInfo(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findRechargeOrderList(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findRechargeOrderInfo(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findMemberStatistic(String dataParam, AppParam appParam)throws ApiHandleException;
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
    public List<AppBizShop> findSuitShopByMCID(Long mcid);
    public Page<AppPolicyRechargeOrder> findRechargeOrderListByPage(Page<AppPolicyRechargeOrder> pageAll);
    public List<AppPolicyMember> findMemberListByOUID(AppPolicyConsumer appPolicyConsumer);
    public AppPolicyMember findAppPolicyMember(String openID,String userID,Long uid)throws Exception;
}
