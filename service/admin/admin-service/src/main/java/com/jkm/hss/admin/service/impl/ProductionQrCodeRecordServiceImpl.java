package com.jkm.hss.admin.service.impl;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.dao.ProductionQrCodeRecordDao;
import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import com.jkm.hss.admin.helper.requestparam.OemProductionRequest;
import com.jkm.hss.admin.helper.requestparam.ProductionRequest;
import com.jkm.hss.admin.helper.responseparam.ProductionListResponse;
import com.jkm.hss.admin.service.ProductionQrCodeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/20.
 */
@Slf4j
@Service
public class ProductionQrCodeRecordServiceImpl implements ProductionQrCodeRecordService {
    @Autowired
    private ProductionQrCodeRecordDao productionQrCodeRecordDao;
    /**
     * 插入
     *
     * @param productionQrCodeRecord
     */
    @Override
    public void add(ProductionQrCodeRecord productionQrCodeRecord) {
        productionQrCodeRecordDao.insert(productionQrCodeRecord);
    }

    /**
     * 按条件查询列表
     *
     * @param productionRequest
     */
    @Override
    public PageModel<ProductionListResponse> selectList(ProductionRequest productionRequest) {
        final PageModel<ProductionListResponse> pageModel = new PageModel<>(productionRequest.getPageNo(), productionRequest.getPageSize());
        productionRequest.setOffset(pageModel.getFirstIndex());
        productionRequest.setCount(pageModel.getPageSize());
        final Long count = this.productionQrCodeRecordDao.selectCount(productionRequest);
        final List<ProductionListResponse> productionQrCodeRecords = this.productionQrCodeRecordDao.selectList(productionRequest);
        pageModel.setCount(count);
        pageModel.setRecords(productionQrCodeRecords);
        return pageModel;
    }

    /**
     * 分公司按条件查询列表
     *
     * @param oemProductionRequest
     */
    @Override
    public PageModel<ProductionListResponse> selectOemList(OemProductionRequest oemProductionRequest) {
        final PageModel<ProductionListResponse> pageModel = new PageModel<>(oemProductionRequest.getPageNo(), oemProductionRequest.getPageSize());
        oemProductionRequest.setOffset(pageModel.getFirstIndex());
        oemProductionRequest.setCount(pageModel.getPageSize());
        final Long count = this.productionQrCodeRecordDao.selectOemCount(oemProductionRequest);
        final List<ProductionListResponse> productionQrCodeRecords = this.productionQrCodeRecordDao.selectOemList(oemProductionRequest);
        pageModel.setCount(count);
        pageModel.setRecords(productionQrCodeRecords);
        return pageModel;
    }

    /**
     * 更改下载url
     *
     * @param id
     * @param downloadUrl
     * @return
     */
    @Override
    public int updateDownUrl(long id, String downloadUrl) {
        return productionQrCodeRecordDao.updateDownUrl(id,downloadUrl);
    }
}
