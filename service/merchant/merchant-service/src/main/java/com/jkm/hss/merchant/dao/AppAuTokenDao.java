package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AppAuToken;
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
    void updateAppAuToken(AppAuToken appAuToken);
}
