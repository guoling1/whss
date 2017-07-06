package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppAuUserToken;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/6/6.
 */
@Repository
public interface HsyActiveDao {
    public List<AppAuUserToken> findLoginInfoByAccessToken(@Param("accessToken")String accessToken);
}
