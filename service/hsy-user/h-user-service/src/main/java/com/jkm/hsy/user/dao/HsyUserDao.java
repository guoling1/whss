package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppAuUser;
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
}
