package com.jkm.hss.merchant.dao;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.MerchantInfoAddRequest;
import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/23.
 */
@Repository
public interface MerchantInfoDao {

    /**
     * 添加商户资料
     * @param merchantInfo
     */
    int update(MerchantInfoAddRequest merchantInfo);

    /**
     * 查询
     * @param id
     * @return
     */
    MerchantInfo selectById(long id);

    /**
     * 根据openId查询
     * @param openId
     * @return
     */
    MerchantInfo selectByOpenId(@Param("openId") String openId);


    /**
     * 根据mobile查询
     * @param mobile
     * @return
     */
    MerchantInfo selectByMobile(@Param("mobile") String mobile);
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
    Optional<MerchantInfo> getByNo(@Param("bankNo") String bankNo);

    /**
     * 根据dealerId代理商查询
     * @param dealerId
     * @return
     */
    List<MerchantInfo> selectByDealerId(@Param("dealerId") long dealerId);

    /**
     * 根据条件修改
     * @param
     */
    int updateBySelective(MerchantInfo merchantInfo);

    /**
     * 查询商户列表
     * @return
     */
//    List getAll();
    Optional<MerchantInfo> getAll(MerchantInfo merchantInfo);

    /**
     * 审核商户（更改商户状态）
     * @param requestMerchantInfo
     * @return
     */
    long updateRecord(RequestMerchantInfo requestMerchantInfo);

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
    MerchantInfo selectByCode(@Param("code") String code);

    /**
     * 更改商户状态
     * @param status
     * @param id
     * @return
     */
    int updateStatus(@Param("status") int status,@Param("id") long id);

    /**
     *
     * @param merchantIdList
     * @return
     */
    List<MerchantInfo> batchGetMerchantInfo(@Param("merchantIdList") List<Long> merchantIdList);
}
