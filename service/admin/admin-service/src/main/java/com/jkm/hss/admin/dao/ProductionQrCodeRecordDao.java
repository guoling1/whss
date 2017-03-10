package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import com.jkm.hss.admin.helper.requestparam.ProductionRequest;
import com.jkm.hss.admin.helper.responseparam.ProductionListResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/20.
 */
@Repository
public interface ProductionQrCodeRecordDao {
    /**
     * 插入
     *
     * @param productionQrCodeRecord
     */
    void insert(ProductionQrCodeRecord productionQrCodeRecord);

    /**
     * 按条件查询列表
     *
     * @param productionRequest
     */
    List<ProductionListResponse> selectList(ProductionRequest productionRequest);

    /**
     * 按条件查询列表个数
     *
     * @param productionRequest
     */
    Long selectCount(ProductionRequest productionRequest);
}
