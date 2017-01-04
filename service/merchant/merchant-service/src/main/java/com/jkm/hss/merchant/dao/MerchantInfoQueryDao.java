package com.jkm.hss.merchant.dao;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Repository
public interface MerchantInfoQueryDao {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Optional<MerchantInfo> selectById(@Param("id") long id);

    /**
     * 根据商户状态查询
     * @param status
     * @return
     */
    Optional<MerchantInfo> selectByStatus(@Param("status") int status);

    /**
     * 根据经销商id查询
     * @param dealerId
     * @return
     */
    Optional<MerchantInfo> selectByDealerId(@Param("dealerId") long dealerId);

    /**
     * 查询所有商户
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfoResponse);

    /**
     * 根据商户名查询
     * @param pageNo
     * @param pageSize
     * @param merchantName
     * @return
     */
    List<MerchantInfoResponse> selectByName(@Param("pageNo") int pageNo,@Param("pageSize") int pageSize,@Param("merchantName") String merchantName);

    /**
     * 查询总数
     * @return
     */
    List<MerchantInfoResponse> getCount();

    /**
     * 查询待审核商户列表
     * @param merchantInfoResponse
     * @return
     */
    List<MerchantInfoResponse> getRecord(MerchantInfoResponse merchantInfoResponse);

    /**
     * 查询代理商
     * @param firstLevelDealerId
     * @return
     */
    MerchantInfoResponse getProxyName(@Param("firstLevelDealerId") long firstLevelDealerId);
}
