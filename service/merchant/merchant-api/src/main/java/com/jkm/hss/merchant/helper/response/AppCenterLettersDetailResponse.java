package com.jkm.hss.merchant.helper.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Data
public class AppCenterLettersDetailResponse {
    private Long id;
    private String title;
    private String content;
    private Date createTime;
    private List<String> thumbnailUrls;
    private List<String> orgUrls;
    private Long downloadCount;
}
