package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppCmFile;
import org.springframework.stereotype.Repository;

/**
 * Created by Allen on 2017/1/12.
 */
@Repository
public interface HsyFileDao {
    public void insert(AppCmFile appCmFile);
}
