package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.PushNoticeDao;
import com.jkm.hss.merchant.entity.NoticeRequest;
import com.jkm.hss.merchant.entity.NoticeResponse;
import com.jkm.hss.merchant.service.PushNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2017/3/2.
 */
@Slf4j
@Service
public class PushNoticeServiceImpl implements PushNoticeService {

    @Autowired
    private PushNoticeDao pushNoticeDao;

    @Override
    public void add(NoticeRequest request) {
        this.pushNoticeDao.add(request);
    }

    @Override
    public List<NoticeResponse> selectList(NoticeRequest request) {
        List<NoticeResponse> list = this.pushNoticeDao.selectList(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setDates(dates);
                }
            }
        }
        return list;
    }

    @Override
    public int selectListCount(NoticeRequest request) {
        return this.pushNoticeDao.selectListCount(request);
    }

    @Override
    public NoticeResponse noticeDetails(int id) {
        NoticeResponse res = this.pushNoticeDao.noticeDetails(id);
        return res;
    }

    @Override
    public int updateNotice(String title,String text,int id) {
        return this.pushNoticeDao.updateNotice(title,text,id);

    }

    @Override
    public void deleteNotice(int id) {
        this.pushNoticeDao.deleteNotice(id);
    }

    @Override
    public NoticeResponse pageAnnouncement() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dates = sdf.format(date);
        NoticeResponse res = this.pushNoticeDao.pageAnnouncement(dates);
        return res;
    }

    @Override
    public List<NoticeResponse> list() {
        List<NoticeResponse> list = this.pushNoticeDao.list();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setDates(dates);
                }
            }
        }
        return list;
    }
}
