package com.jkm.hss.merchant.dao;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.ContinueBankInfoRequest;
import com.jkm.hss.merchant.helper.request.MerchantInfoAddRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
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
//    Optional<MerchantInfo> getByNo(@Param("bankNo") String bankNo);

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
    List<MerchantInfo> getAll();
//    Optional<MerchantInfo> getAll(MerchantInfo merchantInfo);

    /**
     * 审核商户（更改商户状态）
     * @param requestMerchantInfo
     * @return
     */
//    long updateRecord(RequestMerchantInfo requestMerchantInfo);

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

    /**
     * 插入accountId
     *
     * @param accountId
     * @param status
     * @param merchantId
     * @return
     */
    int addAccountId(@Param("accountId") long accountId, @Param("status") int status, @Param("merchantId") long merchantId,@Param("checkedTime") Date checkedTime);

    /**
     * 根据accountId查询
     *
     * @param accountId
     * @return
     */
    MerchantInfo selectByAccountId(@Param("accountId") long accountId);

    /**
     * 去升级
     * @param merchantId
     * @param level
     * @return
     */
    int toUpgrade(@Param("merchantId") long merchantId, @Param("level") int level);

    /**
     * 初始化推荐版本数据
     * @param merchantInfo
     */
    void updateByCondition(MerchantInfo merchantInfo);

    /**
     * 按accountIds批量查询
     *
     * @param accountIds
     * @return
     */
    List<MerchantInfo> batchSelectByAccountIds(@Param("accountIds") List<Long> accountIds);
    /**
     * 修改信用卡信息
     * @param creditCard
     * @param creditCardName
     * @param creditCardShort
     * @param id
     * @return
     */
    int updateCreditCard(@Param("creditCard") String creditCard,@Param("creditCardName") String creditCardName,@Param("creditCardShort") String creditCardShort,@Param("id") long id);
    /**
     * 完善支行信息
     * @param continueBankInfoRequest
     * @return
     */
    int updateBranchInfo(ContinueBankInfoRequest continueBankInfoRequest);

    /**
     * 修改认证状态
     * @param isAuthen
     * @param id
     * @return
     */
    int toAuthen(@Param("isAuthen") String isAuthen, @Param("id") long id);
}
