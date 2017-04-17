package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.WebsiteDao;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/4/5.
 */
@Service
public class WebsiteServiceImpl implements WebsiteService {

    @Autowired
    private WebsiteDao  websiteDao;

    @Override
    public int selectWebsite(WebsiteRequest req) {
        return this.websiteDao.selectWebsite(req);
    }

    @Override
    public void insertWebsite(WebsiteRequest req) {
        this.websiteDao.insertWebsite(req);
    }

    @Override
    public List<CooperationQueryResponse> selectCooperation(CooperationQueryRequest request) {
        List<CooperationQueryResponse> list = this.websiteDao.selectCooperation(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
            }
        }
        return list;
    }

    @Override
    public int selectCooperationCount(CooperationQueryRequest request) {
        return this.websiteDao.selectCooperationCount(request);
    }

    @Override
    public String selectMobile(String mobile) {
        return this.websiteDao.selectMobile(mobile);
    }

    @Override
    public void saveInfo(Map map) {
        this.websiteDao.saveInfo(map);
    }

    @Override
    public List<JoinResponse> selectJoin(JoinRequest request) {
        List<JoinResponse> list = this.websiteDao.selectJoin(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
            }
        }
        return list;
    }

    @Override
    public int selectJoinCount(JoinRequest request) {
        return this.websiteDao.selectJoinCount(request);
    }
}
