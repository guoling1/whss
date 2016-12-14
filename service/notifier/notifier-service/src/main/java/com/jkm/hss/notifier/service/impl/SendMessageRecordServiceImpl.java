package com.jkm.hss.notifier.service.impl;

import com.jkm.hss.notifier.dao.SendMessageRecordDao;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.service.SendMessageRecordService;
import com.jkm.hss.notifier.entity.SendMessageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by konglingxin on 15/11/4.
 */
@Service
public class SendMessageRecordServiceImpl implements SendMessageRecordService {
    @Autowired
    private SendMessageRecordDao sendMessageRecordDao;

    @Override
    public long insert(final SendMessageRecord sendRecord) {
        return this.sendMessageRecordDao.insert(sendRecord);
    }

    @Override
    public void updateStatus(final long id, final int status) {
        this.sendMessageRecordDao.updateStatus(id, status);
    }

    @Override
    public List<SendMessageRecord> getMessageRecodeByUid(final long uid, final EnumUserType userType) {
        return this.sendMessageRecordDao.getSendMessageRecordByUid(uid, userType.getId());
    }

    @Override
    public int getRecordByMobileAndTemp(final String mobile, final int templateId) {
        return this.sendMessageRecordDao.getRecordByMobileAndTemp(mobile, templateId);
    }

    @Override
    public SendMessageRecord selectLast(final String mobile, final int templateId) {
        return this.sendMessageRecordDao.selectLast(mobile, templateId);
    }
}
