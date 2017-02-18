package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.ChangeMerchantNameDao;
import com.jkm.hss.merchant.service.ChangeMerchantNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangbin on 2017/2/18.
 */
@Slf4j
@Service
public class ChangeMerchantNameServiceImpl implements ChangeMerchantNameService {

    @Autowired
    private ChangeMerchantNameDao changeMerchantNameDao;

    @Override
    public void updatChangeName(long id,String merchantChangeName) {
        changeMerchantNameDao.updatChangeName(id,merchantChangeName);
    }

}
