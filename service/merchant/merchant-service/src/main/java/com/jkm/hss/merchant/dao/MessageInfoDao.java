package com.jkm.hss.merchant.dao;

import com.jkm.base.common.util.Page;
import com.jkm.hss.merchant.entity.MessageInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/8/15.
 */
@Repository
public interface MessageInfoDao {
    public void insert(MessageInfo messageInfo);
    public List<MessageInfo> findMessageInfoListByPage(Page<MessageInfo> entity);
    public Integer findMessageInfoListByPageCount(MessageInfo entity);
    public void updateReadStatusBeyondTime(MessageInfo messageInfo);
}
