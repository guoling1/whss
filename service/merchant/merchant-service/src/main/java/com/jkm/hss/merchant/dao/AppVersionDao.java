package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AppVersion;
import com.jkm.hss.merchant.entity.AppVersionCommon;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/7/25.
 */
@Repository
public interface AppVersionDao {
    public List<AppVersionCommon> findVersionDetail(AppVersionCommon appVersion);
    public List<AppVersionCommon> getAppVersion(AppVersionCommon appVersion);
}
