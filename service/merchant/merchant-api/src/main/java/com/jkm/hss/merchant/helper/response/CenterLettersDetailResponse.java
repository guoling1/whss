package com.jkm.hss.merchant.helper.response;

import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Data
public class CenterLettersDetailResponse {
    private Long id;
    private String title;
    private String content;
    private List<CenterImageRes> centerImages;
    @Data
    public static class CenterImageRes{
        private String imgUrl;
        private String showImgUrl;
    }
}
