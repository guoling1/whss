package com.jkm.api.service;

import com.jkm.api.helper.requestparam.OpenCardQueryRequest;
import com.jkm.api.helper.requestparam.OpenCardRequest;

import java.util.Map;

/**
 * Created by xingliujie on 2017/8/17.
 */
public interface OpenCardService {
    /**
     * 快捷绑卡
     *
     * @param openCardRequest
     * @return
     */
    String kuaiPayOpenCard(OpenCardRequest openCardRequest);

    /**
     * 绑卡查询
     *
     * @param openCardQueryRequest
     * @return
     */
    Map kuaiPayOpenCardQuery(OpenCardQueryRequest openCardQueryRequest);
}
