package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/4/18.
 */
@Repository
public interface HsyCmbcDao {
    /**
     * 根据编码查询好收银用户
     * @param id
     * @return
     */
    AppAuUser selectByUserId(@Param("id") long id);

    /**
     * 根据编码查询好收银店铺
     * @param id
     * @return
     */
    AppBizShop selectByShopId(@Param("id") long id);

    /**
     * 根据userId查询好收银银行卡
     * @param userId
     * @return
     */
    AppBizCard selectByCardId(@Param("userId") long userId);

    /**
     * 根据userId更改开通产品状态
     * @param id
     * @return
     */
    void updateHxbUserById(@Param("hxbOpenProduct") int hxbOpenProduct,@Param("hxbOpenProductRemarks") String hxbOpenProductRemarks,@Param("id") long id);
}
