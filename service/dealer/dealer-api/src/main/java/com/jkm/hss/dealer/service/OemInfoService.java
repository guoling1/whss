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
     * 修改
     * @param oemInfo
     */
    void update(OemInfo oemInfo);

    /**
     *根据分公司编码查询分公司O单配置
     * @return
     */
    OemDetailResponse selectByDealerId(long dealerId);
    /**
     *根据分公司编码查询带appId分公司O单配置
     * @return
     */
    OemDetailResponse selectByDealerIdWithAppId(long dealerId);

    /**
     * 配置O单
     * @param addOrUpdateOemRequest
     */
    void addOrUpdate(AddOrUpdateOemRequest addOrUpdateOemRequest);

    /**
     * 根据自生成号查询分公司信息
     * @param oemNo
     * @return
     */
    Optional<OemInfo> selectByOemNo(String oemNo);

    /**
     * 根据分公司编码查询分公司O单配置
     * @param dealerId
     * @return
     */
    Optional<OemInfo> selectOemInfoByDealerId(long dealerId);
    /**
     * 根据编码查询
     * @param id
     * @return
     */
    Optional<OemInfo> selectById(long id);

}
