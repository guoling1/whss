package com.jkm.hss.dealer.service;
import com.jkm.hss.dealer.helper.response.HomeReportResponse;

import java.util.Date;

/**
 * Created by wayne on 17/4/27.
 * 代理商报表
 */
public interface ReportService {

    /**
     * 查询
     * @param dealerId
     * @param startTime
     * @param endTime
     * @return
     */
    HomeReportResponse getHomeReport(long dealerId,final long acountid,int level, String startTime,String endTime);

    /**
     * 代理商仓储数据生成
     */
    void initReport();
}
