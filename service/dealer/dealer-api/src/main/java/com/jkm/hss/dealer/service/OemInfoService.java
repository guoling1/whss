package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.helper.requestparam.AddOrUpdateOemRequest;
import com.jkm.hss.dealer.helper.response.OemDetailResponse;

/**
 * Created by xingliujie on 2017/5/2.
 */
public interface OemInfoService {
    /**
     * 新增
     * @param oemInfo
     */
    void insert(OemInfo oemInfo);

    /**
     *根据分公司编码查询分公司O单配置
     * @return
     */
    OemDetailResponse selectByDealerId(long dealerId);

    /**
     * 配置O单
     * @param addOrUpdateOemRequest
     */
    void addOrUpdate(AddOrUpdateOemRequest addOrUpdateOemRequest);

}
