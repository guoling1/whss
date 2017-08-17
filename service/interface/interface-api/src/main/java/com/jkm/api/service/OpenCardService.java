package com.jkm.api.service;

import com.jkm.api.helper.requestparam.OpenCardQueryRequest;
import com.jkm.api.helper.requestparam.OpenCardRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/8/17.
 */
public interface OpenCardService {
    public String kuaiPayOpenCard(OpenCardRequest openCardRequest);
    public Map kuaiPayOpenCardQuery(OpenCardQueryRequest openCardQueryRequest);
}
