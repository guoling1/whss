package com.jkm.hsy.user.service.impl;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hsy.user.dao.NetLogDao;
import com.jkm.hsy.user.entity.NetLog;
import com.jkm.hsy.user.help.requestparam.NetLogRequest;
import com.jkm.hsy.user.help.requestparam.NetLogResponse;
import com.jkm.hsy.user.service.NetLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/19.
 */
@Slf4j
@Service
public class NetLogServiceImpl implements NetLogService {
    @Autowired
    private NetLogDao netLogDao;

    @Override
    public void insert(NetLog netLog) {
        netLogDao.insert(netLog);
    }

    @Override
    public PageModel<NetLogResponse> selectByUserId(NetLogRequest netLogRequest) {
        PageModel<NetLogResponse> pageModel = new PageModel<NetLogResponse>(netLogRequest.getPageNo(), netLogRequest.getPageSize());
        netLogRequest.setOffset(pageModel.getFirstIndex());
        List<NetLogResponse> netLogResponses =  netLogDao.selectListByUserId(netLogRequest);
        long counts = netLogDao.selectCountByUserId(netLogRequest.getUserId());
        pageModel.setCount(counts);
        pageModel.setRecords(netLogResponses);
        return pageModel;
    }
}
