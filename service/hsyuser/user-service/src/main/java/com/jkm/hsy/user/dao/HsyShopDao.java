package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.constant.Page;
import com.jkm.hsy.user.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/1/10.
 */
@Repository
public interface HsyShopDao {
    public void insert(AppBizShop appBizShop);
    public void insertAppBizShopUserRole(AppBizShopUserRole appBizShopUserRole);
    public void update(AppBizShop appBizShop);
    public void insertAppBizCard(AppBizCard appBizCard);
    public void updateAppBizCard(AppBizCard appBizCard);
    public List<AppBizCard> findAppBizCardByParam(AppBizCard appBizCard);
    public List<AppBizDistrict> findDistrictByParentCode(AppBizDistrict appBizDistrict);
    public List<AppBizShop> findPrimaryAppBizShopByUserID(AppBizShop appBizShop);
    public List<AppBizShopUserRole> findsurByRoleTypeSid(@Param("sid")Long sid);
    public List<AppBizShop> findShopList(AppBizShop appBizShop);
    public List<AppBizShop> findShopDetail(AppBizShop appBizShop);
    public List<AppAuUser> findUserByShopID(@Param("sid")Long sid);
    public List<AppAuUser> findCorporateUserByShopID(@Param("sid")Long sid);
    public List<AppBizShop> findAppBizShopByID(@Param("id")Long id);
    public List<AppBizShop> findAppBizShopByAccountID(@Param("accountID")Long accountID);
    public String findShopNameByID(@Param("id")Long id);
    public List<AppBizShop> findAppBizShopByAccountIDList(List<Long> accountIDList);
    public List<AppBizShop> findPrimaryAppBizShopByAccountID(@Param("accountID")Long accountID);
    public List<AppAuUser> findCorporateUserByShopIDList(List<Long> idList);
    public boolean isShopStatusCheckPass(@Param("accountID")Long accountID);
    public List<AppBizShopUserRole> findAppBizShopUserRoleBySidAndUid(AppBizShopUserRole appBizShopUserRole);
    public List<AppAuUser> findAuUserByAccountID(@Param("accountID")Long accountID);
    public List<AppBizDistrict> findDistrictByCode(@Param("code")String code);
    public List<AppBizBankBranch> findBankBranchListByPage(Page<AppBizBankBranch> entity);
    public Integer findBankBranchListByPageCount(AppBizBankBranch entity);
    public List<AppBizBankBranch> findBankListByPage(Page<AppBizBankBranch> entity);
    public Integer findBankListByPageCount(AppBizBankBranch entity);
    public List<AppBizShop> findShopListByUID(@Param("uid")Long uid);

    /**
     * 查询开户行列表BOSS后台
     * @param bankName
     * @return
     */
    List<AppBizBankBranch> getBankNameList(@Param("bankName") String bankName);

    /**
     * 修改默认结算卡
     * @param cardNo
     * @param bankName
     * @param districtCode
     * @param bankAddress
     */
    void changeSettlementCard(@Param("cardNo") String cardNo,@Param("bankName") String bankName,
                              @Param("districtCode") String districtCode,
                              @Param("bankAddress") String bankAddress,@Param("id") Long id,@Param("branchCode") String branchCode);

    /**
     * 修改对公或者对私
     * @param isPublic
     * @param id
     */
    void changeStatus(@Param("isPublic") int isPublic,@Param("id") Long id);
}
