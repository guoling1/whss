package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.CooperationQueryRequest;
import com.jkm.hss.merchant.entity.CooperationQueryResponse;
import com.jkm.hss.merchant.entity.WebsiteRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/4/5.
 */
@Repository
public interface WebsiteDao {

    /**
     * 查询商户是否提交资料
     * @param req
     * @return
     */
    int selectWebsite(WebsiteRequest req);

    /**
     * 保存用户提交资料
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
    String selectMobile(@Param("mobile") String mobile);

    void saveInfo(Map map);
}
