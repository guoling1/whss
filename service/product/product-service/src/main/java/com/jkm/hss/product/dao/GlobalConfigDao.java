package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.GlobalConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
@Repository
public interface GlobalConfigDao {
    /**
     * 初始化
     * @param globalConfig
     */
    int insert(GlobalConfig globalConfig);

    /**
     * 修改
     * @param globalConfig
     * @return
     */
    int update(GlobalConfig globalConfig);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    GlobalConfig selectById(@Param("id") long id);

    /**
     * 查询所有记录
     * @return
     */
    List<GlobalConfig> selectAll();
}
