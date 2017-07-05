package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.NoticeRequest;
import com.jkm.hss.merchant.entity.NoticeResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2017/3/2.
 */
@Repository
public interface PushNoticeDao {

    /**
     * 插入公告信息
     * @param request
     */
    void add(NoticeRequest request);

    /**
     * 查询公告信息
     * @param request
     * @return
     */
    List<NoticeResponse> selectList(NoticeRequest request);

    /**
     * 查询公告信息列表总数
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
    int updateNotice(@Param("title") String title,@Param("text") String text,@Param("id") int id);

    /**
     * 删除公告
     * @param id
     */
    void deleteNotice(int id);

    /**
     * 微信端公告显示
     * @return
     */
    NoticeResponse pageAnnouncement(@Param("dates") String dates);

    /**
     * 微信端公告显示列表
     * @return
     */
    List<NoticeResponse> list();
}
