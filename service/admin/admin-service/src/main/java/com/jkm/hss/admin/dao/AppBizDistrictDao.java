package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/8.
 */
@Repository
public interface AppBizDistrictDao {
    /**
     * 查询所有省
     * @return
     */
    List<AppBizDistrictResponse> findAllProvinces();

    /**
     * 根据省份编码查询该省下所有的市
     * @param code
     * @return
     */
    List<AppBizDistrictResponse> findCityByProvinceCode(@Param("code") String code);
    /**
     * 查询记录
     * @return
     */
    List<AppBizDistrictResponse> findByCode(@Param("code") String code);


}
