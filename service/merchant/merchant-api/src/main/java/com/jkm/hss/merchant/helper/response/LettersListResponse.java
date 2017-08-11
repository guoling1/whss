package com.jkm.hss.merchant.helper.response;

import lombok.Data;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Data
public class LettersListResponse {
    private Long id;
    private String title;
    private String content;
    private String adminName;
    private Integer status;
}
