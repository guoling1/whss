package com.jkm.hsy.user.service;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hsy.user.entity.HsyUserShop;
import com.jkm.hsy.user.help.requestparam.UserRequest;

import java.util.List;

/**
 * Created by longwen.jiang on 2017/1/12.
 */
public interface  HsyUserDyService {


    public PageModel<HsyUserShop> findHsyUserShopListByMerchantId(UserRequest userRequest);


    long selectCountByMerchantId(UserRequest userRequest);


    public  void insert(HsyUserShop hs);


    public  void  update(HsyUserShop hs);


    public HsyUserShop findHsyUserShopById(Long Id);


    /**
     * 根据商户ID查找店铺信息
     *
     * @param uid
     * @return
     */
    List<HsyUserShop> findByMerId(Long uid);


}
