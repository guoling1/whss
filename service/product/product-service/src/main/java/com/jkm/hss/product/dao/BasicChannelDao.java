package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.BasicChannel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-24.
 */
@Repository
public interface BasicChannelDao {

    /**
     * 初始化
     * @param basicChannel
     */
    void init(BasicChannel basicChannel);

    /**
     * 查询
     * @param channelTypeSign
     * @return
     */
    BasicChannel selectByChannelTypeSign(@Param("channelTypeSign") int channelTypeSign);

    /**
     * 查询
     * @param channelSource
     * @return
     */
    BasicChannel selectByChannelSource(@Param("channelSource") String channelSource);

    /**
     * 查询
     * @return
     */
    List<BasicChannel> selectAll();

    /**
     * 修改
     * @param basicChannel
     */
    void update(BasicChannel basicChannel);

    BasicChannel selectById(long id);
}
