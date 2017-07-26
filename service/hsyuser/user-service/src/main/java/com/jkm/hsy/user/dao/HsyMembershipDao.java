package com.jkm.hsy.user.dao;

import com.jkm.base.common.util.Page;
import com.jkm.hsy.user.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Allen on 2017/5/12.
 */
@Repository
public interface HsyMembershipDao {
    public void insertMembershipCard(AppPolicyMembershipCard appPolicyMembershipCard);
    public void updateMembershipCard(AppPolicyMembershipCard appPolicyMembershipCard);
    public void insertMembershipCardShop(AppPolicyMembershipCardShop appPolicyMembershipCardShop);
    public void insertMembershipCardShopBatch(List<AppPolicyMembershipCardShop> list);
    public void deleteMembershipCardShop(@Param("mcid")Long mcid);
    public List<AppPolicyConsumer> findConsumerByParam(AppPolicyConsumer appPolicyConsumer);
    public List<AppPolicyMember> findMemberByParam(AppPolicyMember appPolicyMember);
    public List<AppPolicyMember> findMemberCascadeByParam(AppPolicyMember appPolicyMember);
    public List<AppPolicyMembershipCard> findMemberCardByParam(AppPolicyMembershipCard appPolicyMembershipCard);
    public void insertConsumer(AppPolicyConsumer appPolicyConsumer);
    public void updateConsumer(AppPolicyConsumer appPolicyConsumer);
    public void insertMember(AppPolicyMember appPolicyMember);
    public void updateMember(AppPolicyMember appPolicyMember);
    public List<AppPolicyMember> findMemberInfoByID(@Param("id")Long id);
    public Integer findMembershipCardCountOfUserByUID(@Param("uid")Long uid);
    public List<AppPolicyMembershipCard> findMemberCardList(AppPolicyMembershipCard appPolicyMembershipCard);
    public Integer findMemberCountOfUserByUID(@Param("uid")Long uid);
    public List<AppAuUser> findShopNameAndGlobalID(@Param("uid")Long uid);
    public void insertRechargeOrder(AppPolicyRechargeOrder appPolicyRechargeOrder);
    public void updateRechargeOrder(AppPolicyRechargeOrder appPolicyRechargeOrder);
    public List<AppPolicyMembershipCard> findMemberCardByID(@Param("id") Long id);
    public Integer findMemberCardCountByMCID(@Param("mcid")Long mcid);
    public Integer findMemberCardCascadeCountByUID(@Param("uid")Long uid);

    /**
     * 会员列表
     * @param request
     * @return
     */
    List<MemberResponse> getMemberList(MemberRequest request);

    /**
     * 会员总数
     * @param request
     * @return
     */
    int getMemberListCount(MemberRequest request);

    /**
     * 会员详情
     * @param request
     * @return
     */
    MemberResponse getMemberDetails(MemberRequest request);
    public Integer findMemberCardCascadeCountByUID(@Param("uid")Long uid, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    public List<BasicChannel> findChannelAccountID(@Param("channelTypeSign")Integer channelTypeSign);
    public List<AppBizShop> findSuitShopByMCID(@Param("mcid")Long mcid);
    public List<AppPolicyMember> findMemberListByPage(Page<AppPolicyMember> entity);
    public Integer findMemberListByPageCount(AppPolicyMember entity);
    public List<AppPolicyRechargeOrder> findRechargeOrderListByPage(Page<AppPolicyRechargeOrder> entity);
    public Integer findRechargeOrderListByPageCount(AppPolicyRechargeOrder entity);
    public List<AppPolicyRechargeOrder> findRechargeOrderInfo(@Param("id")Long id);
    public AppPolicyMemberStatistic findMemberConsumeStatistic(@Param("uid")Long uid, @Param("startTime") Date startTime,@Param("endTime") Date endTime);
    public List<AppPolicyMembershipCard> findMemberCardAndStatistic(@Param("uid")Long uid);
    public List<AppPolicyMember> findMemberListByOUID(AppPolicyConsumer appPolicyConsumer);
    public List<AppPolicyMember> findMemberListByOUIDAndUID(@Param("openID")String openID,@Param("userID")String userID,@Param("uid")Long uid,@Param("status")Integer status);
    public Date findLastConsumeTime(@Param("mid")Long mid);
    public List<AppPolicyRechargeOrder> findAppPolicyRechargeOrderByParam(@Param("uid") Long uid,@Param("startTime")Date startTime,@Param("endTime")Date endTime);
    public List<AppPolicyRechargeOrder> findRechargeOrderAboutRechargeStatus(@Param("mid") Long mid);
    public List<AppPolicyMembershipCardShop> getMembershipCardShop(@Param("mcid")Long mcid);
    public BigDecimal findConsumeOrderSum(@Param("mcid")Long mcid,@Param("mid")Long mid);

    /**
     * 查询商户列表（会员卡）
     * @param request
     * @return
     */
    List<MerchantMemberResponse> getMerchantMemberList(MemberRequest request);

    /**
     * 查询商户列表（会员卡）总数
     * @param request
     * @return
     */
    List<MerchantMemberResponse> getMerchantMemberLists(MemberRequest request);

    /**
     * 商户会员卡列表
     * @param request
     * @return
     */
    List<MerchantMemberShipResponse> getMemberShipList(MemberRequest request);

    /**
     * 商户会员卡列表有效商户
     * @param uid
     * @return
     */
    List<MerchantMemberShipResponse> getMemberShipLists(@Param("uid") Long uid);

    /**
     * 会员卡列表
     * @param request
     * @return
     */
    List<CardNoResponse> getcardList(MemberRequest request);

    /**
     * 会员卡列表总数
     * @param request
     * @return
     */
    List<CardNoResponse> getcardLists(MemberRequest request);
}
