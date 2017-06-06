package com.jkm.hss.push.dao;


import com.jkm.hss.push.entity.Push;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * Created by longwen.jiang
 */
@Repository
public interface PushDao {



    /**
     * 插入
     *
     * @param push
     */
    void insert(Push push);


    /**
     * 根据店铺ID查询改店铺下所有注册过的APP
     *
     * @param sid
     */
    List<Map> selectUserAppBySid(String sid);


    /**
     * 根据用户ID查询改店铺下所有注册过的APP
     *
     * @param uid
     */
    List<Map>selectUserAppByUid(String uid);

    /**
     * 查询交易单号
     * @param transactionNumber
     * @return
     */
    String getTransactionNumber(@Param("transactionNumber") String transactionNumber);
}