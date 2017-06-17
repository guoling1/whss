package com.jkm.hsy.user.service;
import com.jkm.hsy.user.entity.AppBizBankBranch;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2017/1/10.
 */
public interface HsyShopService {
    public String updateHsyShop(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException;
    public String updateHsyShopContact(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException;
    public String updateHsyShopContact1o6(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException;
    public String updateHsyShopEmail(String dataParam,AppParam appParam)throws ApiHandleException;
    public String insertHsyCard(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findHsyCardBankByBankNO(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findDistrictByParentCode(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findIndustryList(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findShopList(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findShopDetail(String dataParam,AppParam appParam)throws ApiHandleException;
    public String insertBranchShop(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findContractInfo(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findBankBranchList(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findBankList(String dataParam,AppParam appParam)throws ApiHandleException;

    /**
     * 查询开户行列表BOSS后台
     * @param bankName
     * @return
     */
    List<AppBizBankBranch> getBankNameList(String bankName);

    /**
     * 查询开户行列表BOSS后台对私
     * @param cardNo
     * @return
     */
    String getPersonalBankNameList(String cardNo);

    /**
     * 修改默认结算卡
     * @param cardNo
     * @param bankName
     * @param districtCode
     * @param bankAddress
     */
    void changeSettlementCard(String cardNo, String bankName, String districtCode, String bankAddress);
}
