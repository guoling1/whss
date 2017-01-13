package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppBizShopUserRole;
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
    public List<AppBizCard> findAppBizCardByParam(AppBizCard appBizCard);
}
