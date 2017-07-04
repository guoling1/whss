package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppAuVerification;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Allen on 2017/1/9.
 */
@Repository
public interface HsyVerificationDao {
    public List<AppAuVerification> findVCodeWithinTime(AppAuVerification appAuVerification);
    public void insert(AppAuVerification appAuVerification);
    public Integer findVCodeCountWithinTime(AppAuVerification appAuVerification);
}
