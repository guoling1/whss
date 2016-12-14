package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.SavePicDao;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.SavePicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangbin on 2016/11/24.
 */
@Slf4j
@Service
public class SavePicServiceImpl implements SavePicService {

    @Autowired
    private SavePicDao savePicDao;
    @Override
    public int save(MerchantInfo merchantInfo) {
        return this.savePicDao.save(merchantInfo);
//        return 0;
    }
}
