package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppCmChannelProduct;
import com.jkm.hsy.user.entity.AppCmChannelRelate;
import com.jkm.hsy.user.entity.AppCmCurrentChannel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/5/26.
 */
@Repository
public interface HsyChannelDao {
    public AppCmChannelProduct findAllProductHsyChannel();
    public AppCmChannelProduct findProductHsyChannelByType(AppCmChannelProduct appCmChannelProduct);
    public void insertAppCmCurrentChannel(AppCmCurrentChannel appCmCurrentChannel);
    public void updateAppCmCurrentChannel(AppCmCurrentChannel appCmCurrentChannel);
    public void insertAppCmChannelRelate(AppCmChannelRelate appCmChannelRelate);
    public void updateAppCmChannelRelate(AppCmChannelRelate appCmChannelRelate);
    public List<AppCmChannelRelate> selectAppCmChannelRelateByParam(AppCmChannelRelate appCmChannelRelate);
    public List<AppCmCurrentChannel> findCurrentChannel(@Param("sid")Long sid);
}
