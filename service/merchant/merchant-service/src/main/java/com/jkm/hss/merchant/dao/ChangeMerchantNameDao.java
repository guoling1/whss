package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.MerchantInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangbin on 2017/2/18.
 */
@Repository
public interface ChangeMerchantNameDao {

    /**
     * 如果存在做更新
     * @param id
     */
    void updatChangeName(@Param("id") long id,@Param("merchantChangeName") String merchantChangeName);

    /**
     * 查询更改后的名称
     * @param id
     * @return
     */
    MerchantInfo selectChangeName(long id);
}
