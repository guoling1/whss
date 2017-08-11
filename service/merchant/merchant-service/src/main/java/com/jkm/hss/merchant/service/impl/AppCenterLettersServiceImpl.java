package com.jkm.hss.merchant.service.impl;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PolicyConditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.merchant.constant.AppConstant;
import com.jkm.hss.merchant.dao.CenterLettersDao;
import com.jkm.hss.merchant.entity.CenterImage;
import com.jkm.hss.merchant.entity.CenterLetters;
import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.exception.ResultCode;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.merchant.helper.request.GetLettersListRequest;
import com.jkm.hss.merchant.helper.response.AppCenterLettersDetailResponse;
import com.jkm.hss.merchant.helper.response.LettersListResponse;
import com.jkm.hss.merchant.service.AppCenterLettersService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/8/11.
 */
@Slf4j
@Service("appCenterLettersService")
public class AppCenterLettersServiceImpl implements AppCenterLettersService {
    @Autowired
    private CenterLettersDao centerLettersDao;
    @Autowired
    private OSSClient ossClient;

    /**
     * HSS001011 下载次数加1
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    public String plusDownLoad(String dataParam, AppParam appParam) throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        CenterLetters centerLetters=null;
        try{
            centerLetters=gson.fromJson(dataParam, CenterLetters.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        centerLettersDao.PlusCount(centerLetters.getId());
        return "";
    }

    public String getCenterLettersList(String dataParam, AppParam appParam) throws ApiHandleException {
        JSONObject jo = JSONObject.fromObject(dataParam);
        int pageNo = jo.getInt("pageNo");
        int pageSize = jo.getInt("pageSize");
        List<AppCenterLettersDetailResponse> appCenterLettersDetailResponse = new ArrayList<AppCenterLettersDetailResponse>();
        PageModel<AppCenterLettersDetailResponse> pageModel = new PageModel<>(pageNo, pageSize);
        GetLettersListRequest getLettersListRequest = new GetLettersListRequest();
        getLettersListRequest.setOffset(pageModel.getFirstIndex());
        getLettersListRequest.setPageNo(pageNo);
        getLettersListRequest.setPageSize(pageSize);
        Long count = centerLettersDao.getLettersCount(getLettersListRequest);
        pageModel.setCount(count);
        List<LettersListResponse> lettersListResponses =  centerLettersDao.getLettersList(getLettersListRequest);
        if(CollectionUtils.isEmpty(lettersListResponses)){
            pageModel.setRecords(Collections.<AppCenterLettersDetailResponse>emptyList());
        }else{
            for(int i=0;i<lettersListResponses.size();i++){
                AppCenterLettersDetailResponse appCenterLettersDetailResponse1 = new AppCenterLettersDetailResponse();
                appCenterLettersDetailResponse1.setId(lettersListResponses.get(i).getId());
                appCenterLettersDetailResponse1.setTitle(lettersListResponses.get(i).getTitle());
                appCenterLettersDetailResponse1.setContent(lettersListResponses.get(i).getContent());
                appCenterLettersDetailResponse1.setCreateTime(lettersListResponses.get(i).getCreateTime());
                List<CenterImage> centerImages = centerLettersDao.getImageList(lettersListResponses.get(i).getId());
                List<String> thumbnailUrls = new ArrayList<>();
                List<String> orgUrls = new ArrayList<>();
                for(int j=0;j<centerImages.size();j++){
                    Date expiration = new Date(new Date().getTime() + 30*60*1000);
                    URL url = ossClient.generatePresignedUrl("jkm-security", centerImages.get(j).getImgUrl(),expiration);
                    thumbnailUrls.add(url.toString()+"&x-oss-process=style/avatar_300");
                    orgUrls.add(url.toString()+"&x-oss-process=style/avatar_300");
                }
                appCenterLettersDetailResponse1.setThumbnailUrls(thumbnailUrls);
                appCenterLettersDetailResponse1.setOrgUrls(orgUrls);
                appCenterLettersDetailResponse.add(appCenterLettersDetailResponse1);
            }
            pageModel.setRecords(appCenterLettersDetailResponse);
        }
        return JSONObject.fromObject(pageModel).toString();
    }
}
