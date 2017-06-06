package com.jkm.hsy.user.service.impl;

import com.jkm.hsy.user.dao.HsyActiveDao;
import com.jkm.hsy.user.entity.AppAuUserToken;
import com.jkm.hsy.user.service.HsyActiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2017/6/6.
 */
@Slf4j
@Service
public class HsyActiveServiceImpl implements HsyActiveService {
    @Autowired
    private HsyActiveDao hsyActiveDao;

    public AppAuUserToken findLoginInfoByAccessToken(String accessToken){
        List<AppAuUserToken> list=hsyActiveDao.findLoginInfoByAccessToken(accessToken);
        if(list!=null&&list.size()!=0)
            return list.get(0);
        else
            return null;
    }
}
