package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.MerchantInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangbin on 2016/11/24.
 */
@Repository
public interface SavePicDao {

    int save(MerchantInfo merchantInfo);
}
