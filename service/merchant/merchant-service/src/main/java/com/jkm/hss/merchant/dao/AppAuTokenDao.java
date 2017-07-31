package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AppAuToken;
import com.jkm.hss.merchant.entity.AppAuUserToken;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/7/24.
 */
@Repository
public interface AppAuTokenDao {
    void insert(AppAuToken appAuToken);
    void update(AppAuToken appAuToken);
    List<AppAuToken> findAppAuTokenByAccessToken(@Param("accessToken")String accessToken);
    List<AppAuToken> findAppAuTokenByClientId(@Param("clientId")String clientId);
    void updateAppAuUserTokenStatusByTid(@Param("tid")Long tid);
    void updateAppAuUserTokenStatusByTidExceptUid(@Param("tid")Long tid,@Param("uid") Long uid);
    List<AppAuUserToken> findAppAuUserTokenByParam(AppAuUserToken appAuUserToken);
    void updateAppAuUserTokenByUidAndTid(AppAuUserToken appAuUserToken);
    void insertAppAuUserToken(AppAuUserToken appAuUserToken);
    void updateAppAuToken(AppAuToken appAuToken);
}
