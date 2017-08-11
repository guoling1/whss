package com.jkm.hss.controller.admin;

import com.aliyun.oss.OSSClient;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.helper.response.OemDetailResponse;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.CenterImage;
import com.jkm.hss.merchant.entity.CenterLetters;
import com.jkm.hss.merchant.helper.request.CenterLettersPublishRequest;
import com.jkm.hss.merchant.helper.request.GetLettersListRequest;
import com.jkm.hss.merchant.helper.response.CenterLettersDetailResponse;
import com.jkm.hss.merchant.helper.response.LettersListResponse;
import com.jkm.hss.merchant.service.CenterLettersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Controller
@RequestMapping(value = "/admin/center")
public class CenterLettersController extends BaseController {
    @Autowired
    private CenterLettersService centerLettersService;
    @Autowired
    private OSSClient ossClient;

    /**
     * 发布
     * @param centerLettersPublishRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public CommonResponse publish(@RequestBody final CenterLettersPublishRequest centerLettersPublishRequest) {
        if(StringUtils.isBlank(centerLettersPublishRequest.getTitle())){
            return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "请填写标题");
        }
        if(StringUtils.isBlank(centerLettersPublishRequest.getContent())){
            return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "请填写内容");
        }
        if(centerLettersPublishRequest.getCenterImages().size()<=0){
            return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "请至少上传一张照片");
        }
        centerLettersPublishRequest.setAdminId(super.getAdminUser().getId());
        centerLettersService.publish(centerLettersPublishRequest);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发布成功");
    }

    /**
     * 列表
     * @param getLettersListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public CommonResponse getList(@RequestBody final GetLettersListRequest getLettersListRequest) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(getLettersListRequest.getEndTime()!=null&&!"".equals(getLettersListRequest.getEndTime())){
            Date dt = sdf.parse(getLettersListRequest.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            getLettersListRequest.setEndTime(sdf.format(rightNow.getTime()));
        }
        PageModel<LettersListResponse> lettersListResponsePageModel = centerLettersService.getList(getLettersListRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",lettersListResponsePageModel);
    }

    /**
     * 修改
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResponse update(@RequestBody final CenterLettersPublishRequest centerLettersPublishRequest) {
        if(centerLettersPublishRequest.getId()<=0){
            return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "缺失参数id");
        }
        if(StringUtils.isBlank(centerLettersPublishRequest.getTitle())){
            return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "请填写标题");
        }
        if(StringUtils.isBlank(centerLettersPublishRequest.getContent())){
            return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "请填写内容");
        }
        if(centerLettersPublishRequest.getCenterImages().size()<=0){
            return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "请至少上传一张照片");
        }
        centerLettersService.update(centerLettersPublishRequest);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "修改成功");
    }

    /**
     * 详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public CommonResponse detail(@PathVariable("id") Long id) {
        CenterLetters centerLetters = centerLettersService.selectById(id);
        List<CenterImage> centerImages = centerLettersService.getImageList(id);
        CenterLettersDetailResponse centerLettersDetailResponse = new CenterLettersDetailResponse();
        centerLettersDetailResponse.setTitle(centerLetters.getTitle());
        centerLettersDetailResponse.setId(centerLetters.getId());
        centerLettersDetailResponse.setContent(centerLetters.getContent());
        List<CenterLettersDetailResponse.CenterImageRes> centerImages1 = new ArrayList<CenterLettersDetailResponse.CenterImageRes>();
        for(int i=0;i<centerImages.size();i++){
            CenterLettersDetailResponse.CenterImageRes ci = new CenterLettersDetailResponse.CenterImageRes();
            ci.setImgUrl(centerImages.get(i).getImgUrl());
            Date expiration = new Date(new Date().getTime() + 30*60*1000);
            if(centerImages.get(i).getImgUrl()!=null&&!"".equals(centerImages.get(i).getImgUrl())){
                URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), centerImages.get(i).getImgUrl(),expiration);
                ci.setShowImgUrl(url.toString());
            }
            centerImages1.add(ci);
        }
        centerLettersDetailResponse.setCenterImages(centerImages1);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",centerLettersDetailResponse);
    }
}
