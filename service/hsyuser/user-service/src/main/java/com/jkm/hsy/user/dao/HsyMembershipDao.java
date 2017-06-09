package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
}
