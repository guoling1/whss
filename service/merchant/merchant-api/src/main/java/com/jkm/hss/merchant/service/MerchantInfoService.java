package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.MerchantInfoAddRequest;
import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/23.
 */
public interface MerchantInfoService {

    /**
     * 添加商户资料
     * @param
     */
    int update(MerchantInfoAddRequest merchantInfo);



    /**
     * 根据id查询
     */
    Optional<MerchantInfo> selectById(long id);


    /**
     * 根据条件插入
     * @param merchantInfo
     * @return
     */
    long insertSelective(MerchantInfo merchantInfo);

    /**
     * 根据卡号查卡bin和名称
     * @param bankNo
     * @return
     */
//    Optional<MerchantInfo> getByNo(String bankNo);

    /**
     * 根据dealerId代理商查询
     * @param dealerId
     * @return
     */
    List<MerchantInfo> selectByDealerId(long dealerId);

    /**
     * 根据条件修改
     * @param
     */
    int updateBySelective(MerchantInfo merchantInfo);


    /**
     * 查询商户列表
     */
    Optional<MerchantInfo> getAll(MerchantInfo merchantInfo);

    /**
     * 审核商户（更改商户状态）
     * @param requestMerchantInfo
     * @return
     */
    long updateRecord(RequestMerchantInfo requestMerchantInfo);

    /**
     * 扫码注册
     * @param merchantInfo
     * @return
     */
    long regByWxPub(MerchantInfo merchantInfo);
    /**
     * 扫固定码注册
     * @param merchantInfo
     * @return
     */
    long regByCode(MerchantInfo merchantInfo);

    /**
     * 商户提交照片
     * @param merchantInfo
     * @return
     */
    int updatePic(MerchantInfoAddRequest merchantInfo);

    /**
     * 根据code查询
     * @param code
     * @return
     */
    Optional<MerchantInfo> selectByCode(String code);

    /**
     * 更改商户状态
     * @param status
     * @param id
     * @return
     */
    int updateStauts(int status,long id);


    /**
     * 查询商户信息
     * @param merchantIdList
     * @return
     */
    List<MerchantInfo> batchGetMerchantInfo(List<Long> merchantIdList);
}
