package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.CooperationQueryRequest;
import com.jkm.hss.merchant.entity.CooperationQueryResponse;
import com.jkm.hss.merchant.entity.WebsiteRequest;

import java.util.List;

/**
 * Created by zhangbin on 2017/4/5.
 */
public interface WebsiteService {

    /**
     * 查询用户是否提交资料
     * @param req
     * @return
     */
    int selectWebsite(WebsiteRequest req);

    /**
     * 保存用户提交信息
     * @param req
     */
    void insertWebsite(WebsiteRequest req);

    /**
     * 查询申请合作用户列表
     * @param request
     * @return
     */
    List<CooperationQueryResponse> selectCooperation(CooperationQueryRequest request);

    /**
     * 查询申请合作用户列表总数
     * @param request
     * @return
     */
    int selectCooperationCount(CooperationQueryRequest request);

    /**
     * 查询手机号
     * @param mobile
     * @return
     */
    String selectMobile(String mobile);

    /**
     * 保存信息
     * @param userName
     * @param mobile
     * @param companyName
     * @param provinceCode
     * @param provinceName
     * @param cityCode
     * @param cityName
     * @param countyCode
     * @param countyName
     * @param type
     */
    void saveInfo(String userName, String mobile, String companyName, String provinceCode, String provinceName, String cityCode, String cityName, String countyCode, String countyName, String type);
}
