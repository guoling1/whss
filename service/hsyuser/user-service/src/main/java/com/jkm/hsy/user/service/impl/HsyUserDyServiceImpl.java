package com.jkm.hsy.user.service.impl;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hsy.user.dao.HsyUserDyDao;
import com.jkm.hsy.user.entity.HsyUser;
import com.jkm.hsy.user.entity.HsyUserShop;
import com.jkm.hsy.user.help.requestparam.UserRequest;
import com.jkm.hsy.user.service.HsyUserDyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangwei on 5/27/16.
 */
@Slf4j
@Service
public class HsyUserDyServiceImpl implements HsyUserDyService {

    @Autowired
    private HsyUserDyDao hsyUserDao;
    private PageModel<HsyUserShop> pageModel;

    @Override
    public long selectCountByMerchantId(UserRequest userRequest) {
        return hsyUserDao.selectCountByMerchantId(userRequest);
    }

    @Override
    public PageModel<HsyUserShop> findHsyUserShopListByMerchantId(UserRequest userRequest) {

        int pageNo=userRequest.getPageNo();
        int pageSize=userRequest.getPageSize();

        final PageModel<HsyUserShop> pageModel = new PageModel<>(pageNo, pageSize);


        userRequest.setOffset(pageModel.getFirstIndex());
        userRequest.setCount(pageModel.getPageSize());
        Long count=hsyUserDao.selectCountByMerchantId(userRequest);
        List<HsyUserShop> list=hsyUserDao.selectByMerchantId(userRequest);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return  pageModel;
    }

    @Override
    public HsyUserShop findHsyUserShopById(Long Id) {
        return  hsyUserDao.selectById(Id);
    }

    @Override
    public List<HsyUserShop> findByMerId(Long uid) {
        return hsyUserDao.selectByMerId(uid);
    }

    @Override
    public void insert(HsyUserShop hs) {

        HsyUser  hu=hs.getUser();
        long  uid=hsyUserDao.insertUser(hu);
        hs.setUid(uid);

        hsyUserDao.insertUserShop(hs);
    }

    @Override
    public void update(HsyUserShop hs) {


        hsyUserDao.updateUser(hs.getUser());

        hsyUserDao.updateUserShop(hs);
    }
}
