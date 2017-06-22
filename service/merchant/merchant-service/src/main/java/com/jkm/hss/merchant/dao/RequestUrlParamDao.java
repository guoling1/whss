package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.RequestUrlParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/6/21.
 */
@Repository
public interface RequestUrlParamDao {
    String insert(RequestUrlParam requestUrlParam);
    String getRequestUrlByUuid(@Param("uuid") String uuid);
}
