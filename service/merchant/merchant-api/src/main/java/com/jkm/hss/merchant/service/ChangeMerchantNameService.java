package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.MerchantInfo;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface ChangeMerchantNameService {

    /**
     * 如果存在做更新
     * @param id
     */
    void updatChangeName(long id,String merchantChangeName);

    /**
     * 查询更改后的名称
     * @param id
     * @return
     */
    MerchantInfo selectChangeName(long id);
}
