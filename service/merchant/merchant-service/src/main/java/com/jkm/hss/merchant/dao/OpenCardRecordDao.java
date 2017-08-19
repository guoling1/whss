package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.OpenCardRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Repository
public interface OpenCardRecordDao {
    void insert(OpenCardRecord openCardRecord);

    /**
     * 查找最新的开卡记录
     */
    OpenCardRecord selectCurrentOneByCardNo(@Param("cardNo") String cardNo);

    OpenCardRecord selectByBindCardReqNo(@Param("bindCardReqNo") String bindCardReqNo);


    void updateStatusByBindCardReqNo(@Param("bindCardReqNo") String bindCardReqNo,@Param("status") int status);
}
