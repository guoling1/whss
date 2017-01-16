package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by Allen on 2017/1/10.
 */
public interface HsyShopService {
    public String updateHsyShop(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException;
    public String updateHsyShopContact(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException;
    public String insertHsyCard(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findHsyCardBankByBankNO(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findDistrictByParentCode(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findIndustryList(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findShopList(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findShopDetail(String dataParam,AppParam appParam)throws ApiHandleException;
    public String insertBranchShop(String dataParam,AppParam appParam)throws ApiHandleException;
    public String findContractInfo(String dataParam,AppParam appParam)throws ApiHandleException;
}
