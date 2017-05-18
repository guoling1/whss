package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppPolicyConsumer;
import com.jkm.hsy.user.entity.AppPolicyMember;
import com.jkm.hsy.user.entity.AppPolicyMembershipCard;
import com.jkm.hsy.user.entity.AppPolicyMembershipCardShop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/5/12.
 */
@Repository
public interface HsyMembershipDao {
    public void insertMembershipCard(AppPolicyMembershipCard appPolicyMembershipCard);
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
}
