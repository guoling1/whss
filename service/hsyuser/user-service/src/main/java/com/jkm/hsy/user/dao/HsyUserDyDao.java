package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.HsyUser;
import com.jkm.hsy.user.entity.HsyUserShop;
import com.jkm.hsy.user.help.requestparam.UserRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by longwen.jiang on 2017-01-12.
 */
@Repository
public interface HsyUserDyDao {

    /**
     * 插入
     *
     * @param hsyUserShop
     */
    void insert(HsyUserShop hsyUserShop);

    /**
     * 更新
     *
     * @param hsyUserShop
     * @return
     */
    int update(HsyUserShop hsyUserShop);

    /**
     * 根据商户ID查找
     *
     * @param userRequest
     * @return
     */
    List<HsyUserShop> selectByMerchantId(UserRequest userRequest);


    /**
     * 根据商户ID查找
     *
     * @param userRequest
     * @return
     */
    long selectCountByMerchantId(UserRequest userRequest);


    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    HsyUserShop selectById(@Param("id") long id);

    /**
     * 根据商户ID查找店铺信息
     *
     * @param uid
     * @return
     */
    List<HsyUserShop> selectByMerId(@Param("uid") long uid);

    /**
     * 新增HsyUser
     *
     * @param hu
     * @return
     */
    long   insertUser(HsyUser hu);


    /**
     * 新增HsyUserShop
     *
     * @param hus
     * @return
     */
    void   insertUserShop(HsyUserShop hus);

    /**
     * 更新HsyUser
     *
     * @param hu
     * @return
     */
    void  updateUser(HsyUser hu);

    /**
     * 更新HsyUser
     *
     * @param hus
     * @return
     */
    void  updateUserShop(HsyUserShop hus);


}
