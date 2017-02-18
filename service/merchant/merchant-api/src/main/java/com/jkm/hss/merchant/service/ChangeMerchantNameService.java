package com.jkm.hss.merchant.service;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface ChangeMerchantNameService {

    /**
     * 如果存在做更新
     * @param id
     */
    void updatChangeName(long id,String merchantChangeName);


}
