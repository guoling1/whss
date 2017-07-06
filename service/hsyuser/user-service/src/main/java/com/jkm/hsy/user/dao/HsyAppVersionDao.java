package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppCmComponent;
import com.jkm.hsy.user.entity.AppVersion;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/2/15.
 */
@Repository
public interface HsyAppVersionDao {
    public List<AppVersion> getAppVersion(AppVersion appVersion);
    public List<AppVersion> getAppVersionAndroid(AppVersion appVersion);
    public List<AppVersion> findVersionDetailByVersionCode(AppVersion appVersion);
    public List<AppCmComponent> findAllPageComponent();
}
