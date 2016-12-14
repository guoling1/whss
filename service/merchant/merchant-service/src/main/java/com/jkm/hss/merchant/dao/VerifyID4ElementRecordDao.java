package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.VerifyID4ElementRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Repository
public interface VerifyID4ElementRecordDao {

    /**
     * 插入
     *
     * @param verifyID4ElementRecord
     */
    void insert(VerifyID4ElementRecord verifyID4ElementRecord);

    /**
     * 更新
     *
     * @param verifyID4ElementRecord
     */
    int update(VerifyID4ElementRecord verifyID4ElementRecord);

    /**
     *
     * @param id
     * @return
     */
    int updateRemark(@Param("id") long id, @Param("remark") String remark);

    /**
     * 将记录标记为无效
     *
     * @param id
     */
    int markToIneffective(@Param("id") long id, @Param("remark") String remark);

    /**
     * 按手机号查询有效的校验记录
     *
     * @param mobile
     */
    VerifyID4ElementRecord selectRecordByMobile(@Param("mobile") String mobile);
}
