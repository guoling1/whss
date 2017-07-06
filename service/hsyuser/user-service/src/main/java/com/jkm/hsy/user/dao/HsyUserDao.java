package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HsyUserDao {
    public List<AppAuUser> findAppAuUserByParam(AppAuUser appAuUser);
    public void insert(AppAuUser appAuUser);
    public void update(AppAuUser appAuUser);
    public void updateByID(AppAuUser appAuUser);
    public List<AppAuUser> findAppAuUserByID(@Param("id")Long id);
    public void insertAppAuToken(AppAuToken appAuToken);
    public void updateAppAuToken(AppAuToken appAuToken);
    public List<AppAuToken> findAppAuTokenByAccessToken(@Param("accessToken")String accessToken);
    public List<AppAuToken> findAppAuTokenByClientid(@Param("clientid")String clientid);
    public void updateAppAuUserTokenStatus(@Param("uid")Long uid);
    public void updateAppAuUserTokenStatusByTID(@Param("tid")Long tid);
    public void updateAppAuUserTokenStatusByTIDExceptUID(@Param("tid")Long tid,@Param("uid") Long uid);
    public List<AppAuUserToken> findAppAuUserTokenByParam(AppAuUserToken appAuUserToken);
    public void insertAppAuUserToken(AppAuUserToken appAuUserToken);
    public void updateAppAuUserTokenByUidAndTid(AppAuUserToken appAuUserToken);
    public void updateAppAuUserTokenStatusByTid(@Param("tid")Long tid);
    public List<AppAuUser> findAppAuUserByIDAndParentSID(AppAuUser appAuUser);
    public List<AppAuUser> findAppAuUserListByParentID(AppAuUser appAuUser);
    public void deleteAppBizShopUserRole(@Param("uid")Long uid);
    public List<AppAuUser> findAppAuUserWithRoleByID(AppAuUser appAuUser);
    public void updateHxbsStatus(@Param("status")Integer status,@Param("remarks")String remarks,@Param("uid")Long uid);
    public String findpwdByToken(@Param("acccesstoken") String acccesstoken);
    public AppBizShopUserRole findAppAuUserRole(AppAuUser appAuUser);
    public List<AdminUser> findAdminUserByUID(@Param("uid")Long uid);

    /**
     * 查找所有法人
     *
     * @return
     */
    List<AppAuUser> selectAllCorporationUser();

    /**
     * 更新email
     *
     * @param email
     * @param id
     * @return
     */
    int updateEmailById(@Param("email") String email, @Param("id") long id);

    /**
     * 启用自动发送邮件
     *
     * @param id
     * @return
     */
    int enableAutoSendBalanceAccountEmail(@Param("id") long id);

    /**
     * 禁用自动发送邮件
     *
     * @param id
     * @return
     */
    int disableAutoSendBalanceAccountEmail(@Param("id") long id);
}
