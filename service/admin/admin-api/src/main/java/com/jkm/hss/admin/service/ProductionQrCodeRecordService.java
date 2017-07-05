package com.jkm.hss.admin.service;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import com.jkm.hss.admin.helper.requestparam.OemProductionRequest;
import com.jkm.hss.admin.helper.requestparam.ProductionRequest;
import com.jkm.hss.admin.helper.responseparam.ProductionListResponse;


/**
 * Created by xingliujie on 2017/2/20.
 */
public interface ProductionQrCodeRecordService {
    /**
     * 插入
     *
     * @param productionQrCodeRecord
     */
    void add(ProductionQrCodeRecord productionQrCodeRecord);
    /**
     * 按条件查询列表
     *
     * @param productionRequest
     */
    PageModel<ProductionListResponse> selectList(ProductionRequest productionRequest);
    /**
     * 分公司按条件查询列表
     *
     * @param oemProductionRequest
     */
    PageModel<ProductionListResponse> selectOemList(OemProductionRequest oemProductionRequest);

    /**
     * 更改下载url
     * @param id
     * @param downloadUrl
     * @return
     */
    int updateDownUrl(long id,String downloadUrl);
}
