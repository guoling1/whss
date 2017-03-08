package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.NoticeRequest;
import com.jkm.hss.merchant.entity.NoticeResponse;

import java.util.List;

/**
 * Created by 张斌 on 2017/3/2.
 */
public interface PushNoticeService {

    /**
     * 插入公告信息
     * @param request
     */
    void add(NoticeRequest request);

    /**
     * 查询公告列表
     * @param request
     * @return
     */
    List<NoticeResponse> selectList(NoticeRequest request);

    /**
     * 查询公告列表总数
     * @param request
     * @return
     */
    int selectListCount(NoticeRequest request);

    /**
     * 查询公告详情
     * @param id
     * @return
     */
    NoticeResponse noticeDetails(int id);

    /**
     * 修改公告
     * @param title
     * @param text
     * @return
     */
    int updateNotice(String title,String text,int id);

    /**
     * 删除公告
     * @param id
     */
    void deleteNotice(int id);

    /**
     * 微信端公告显示
     * @return
     */
    NoticeResponse pageAnnouncement();

    /**
     * 发布公告微信显示列表
     * @param
     * @return
     */
    List<NoticeResponse> list();
}
