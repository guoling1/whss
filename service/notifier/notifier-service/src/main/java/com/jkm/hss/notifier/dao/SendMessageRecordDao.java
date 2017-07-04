package com.jkm.hss.notifier.dao;

import com.jkm.hss.notifier.entity.SendMessageRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by konglingxin on 15/11/3.
 */
@Repository
public interface SendMessageRecordDao {
    /**
     * 插入消息记录
     *
     * @param sendRecord
     */
    int insert(SendMessageRecord sendRecord);

    /**
     * 根据用户id查询记录
     */
    List<SendMessageRecord> getSendMessageRecordByUid(@Param("uid") final long uid,
                                                      @Param("userType") final int userType);

    /**
     * 修改发送记录状态
     *
     * @param id
     * @param status
     */
    void updateStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 根据手机号和发送类型获取该类型信息发送条数
     *
     * @param mobile
     * @param templateId
     * @return
     */
    int getRecordByMobileAndTemp(@Param("mobile") String mobile, @Param("templateId") int templateId);

    /**
     * 根据手机号和发送类型获取最后发送的信息
     *
     * @param mobile
     * @param templateId
     * @return
     */
    SendMessageRecord selectLast(@Param("mobile") String mobile, @Param("templateId") int templateId);
}
