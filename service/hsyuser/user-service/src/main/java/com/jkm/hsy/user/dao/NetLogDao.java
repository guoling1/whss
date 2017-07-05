package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.NetLog;
import com.jkm.hsy.user.help.requestparam.NetLogRequest;
import com.jkm.hsy.user.help.requestparam.NetLogResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/19.
 */
@Repository
public interface NetLogDao {
    void insert(NetLog netLog);
    List<NetLogResponse> selectListByUserId(NetLogRequest netLogRequest);
    Long selectCountByUserId(long userId);
}

