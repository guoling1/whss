package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppAuToken;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppAuUserToken;
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
    public void updateAppAuUserTokenStatus(@Param("uid")Long uid);
    public List<AppAuUserToken> findAppAuUserTokenByParam(AppAuUserToken appAuUserToken);
    public void insertAppAuUserToken(AppAuUserToken appAuUserToken);
    public void updateAppAuUserTokenByUidAndTid(AppAuUserToken appAuUserToken);
    public void updateAppAuUserTokenStatusByTid(@Param("tid")Long tid);
    public List<AppAuUser> findAppAuUserByIDAndParentSID(AppAuUser appAuUser);
    public List<AppAuUser> findAppAuUserListByParentID(AppAuUser appAuUser);
    public void deleteAppBizShopUserRole(@Param("uid")Long uid);
    public List<AppAuUser> findAppAuUserWithRoleByID(AppAuUser appAuUser);
}
