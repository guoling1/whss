package com.jkm.hsy.user.service;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hsy.user.entity.NetLog;
import com.jkm.hsy.user.help.requestparam.NetLogRequest;
import com.jkm.hsy.user.help.requestparam.NetLogResponse;

/**
 * Created by xingliujie on 2017/6/19.
 */
public interface NetLogService {
    void insert(NetLog netLog);
    PageModel<NetLogResponse> selectByUserId(NetLogRequest netLogRequest);
}
