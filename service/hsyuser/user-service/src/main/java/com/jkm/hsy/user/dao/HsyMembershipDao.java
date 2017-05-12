package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppPolicyMembershipCard;
import com.jkm.hsy.user.entity.AppPolicyMembershipCardMember;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/5/12.
 */
@Repository
public interface HsyMembershipDao {
    public void insertMembershipCard(AppPolicyMembershipCard appPolicyMembershipCard);
    public void insertMembershipCardShop(AppPolicyMembershipCardMember appPolicyMembershipCardMember);
    public void insertMembershipCardShopBatch(List<AppPolicyMembershipCardMember> list);
}
