package com.jkm.hsy.user.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.hss.merchant.entity.NoticeRequest;
import com.jkm.hss.merchant.entity.NoticeResponse;
import com.jkm.hss.merchant.enums.EnumNotice;
import com.jkm.hss.merchant.service.PushNoticeService;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.entity.AppBizDistrict;
import com.jkm.hsy.user.entity.AppNotice;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.jkm.hss.product.enums.EnumProductType;

/**
 * Created by wayne on 17/5/4.
 */
@Service("HsyNoticeService")
public class HsyNoticeServiceImpl implements HsyNoticeService {
    @Autowired
    private PushNoticeService pushNoticeService;

    public String noticeList(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        NoticeRequest apprequest=null;
        try{
            apprequest=gson.fromJson(dataParam, NoticeRequest.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        if(apprequest==null){
            apprequest=new NoticeRequest();
            apprequest.setProductType(EnumProductType.HSY.getId());
            apprequest.setOffset(1);
            apprequest.setPageSize(1);
        }

        List<NoticeResponse> list = pushNoticeService.selectList(apprequest);
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getStatus()==1){
                    list.get(i).setPushStatus(EnumNotice.PUBLISHED.getValue());
                }
            }
        }
        List<AppNotice> hsylist= Lists.transform(list, new Function<NoticeResponse, AppNotice>() {
            @Override
            public AppNotice apply(NoticeResponse noticeResponse) {
                AppNotice hsyNoticeResponse=new AppNotice();
                hsyNoticeResponse.setCreateTime(noticeResponse.getDates());
                hsyNoticeResponse.setId(noticeResponse.getId());
                hsyNoticeResponse.setStatus(noticeResponse.getStatus());
                hsyNoticeResponse.setPushStatus(noticeResponse.getPushStatus());
                hsyNoticeResponse.setTitle(noticeResponse.getTitle());
                hsyNoticeResponse.setUrl("http://192.168.1.99:8080/notice/detail?noticeId="+noticeResponse.getId());
                //http://hsy.qianbaojiajia.com
                return hsyNoticeResponse;
            }
        });
        return gson.toJson(hsylist);
    }
}
