package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hsy.user.constant.Page;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

/**
 * Created by Allen on 2017/6/20.
 */
public interface HsyOrderScanService {
    public String insertHsyOrder(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateHsyOrderPay(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findHsyOrderRelateInfo(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findConsumeOrderList(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findConsumeOrderInfo(String dataParam, AppParam appParam)throws ApiHandleException;

    public Page<HsyOrder> findConsumeOrderListByPage(Page<HsyOrder> pageAll);
}
