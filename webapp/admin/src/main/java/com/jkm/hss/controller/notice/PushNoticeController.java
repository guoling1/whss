package com.jkm.hss.controller.notice;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.NoticeRequest;
import com.jkm.hss.merchant.entity.NoticeResponse;
import com.jkm.hss.merchant.enums.EnumNotice;
import com.jkm.hss.merchant.service.PushNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhangbin on 2017/3/2.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/pushNotice")
public class PushNoticeController extends BaseController {

    @Autowired
    private PushNoticeService pushNoticeService;

    /**
     * 发布
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/notice",method = RequestMethod.POST)
    public CommonResponse notice(@RequestBody NoticeRequest request){
        int productId = request.getProductId();
        String productName = request.getProductName();
        String productType = request.getProductType();
        String title = request.getTitle();
        String text = request.getText();
        String publisher = request.getPublisher();
        if (productId==0) {
            return CommonResponse.simpleResponse(-1, "产品id不能为空");
        }
        if (StringUtils.isBlank(productName)) {
            return CommonResponse.simpleResponse(-1, "产品名称不能为空");
        }
        if (StringUtils.isBlank(productType)) {
            return CommonResponse.simpleResponse(-1, "产品类型不能为空");
        }
        if (StringUtils.isBlank(title)) {
            return CommonResponse.simpleResponse(-1, "公告标题不能为空");
        }
        if (StringUtils.isBlank(text)) {
            return CommonResponse.simpleResponse(-1, "公告正文不能为空");
        }
        if (StringUtils.isBlank(publisher)) {
            return CommonResponse.simpleResponse(-1, "发布人不能为空");
        }
        request.setPublisher(super.getAdminUser().getRealname());
        pushNoticeService.add(request);
        return CommonResponse.simpleResponse(1, "发布成功");
    }

    /**
     * 发布公告列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeList",method = RequestMethod.POST)
    public CommonResponse noticeList(@RequestBody NoticeRequest request){
        final PageModel<NoticeResponse> pageModel = new PageModel<NoticeResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        List<NoticeResponse> list = pushNoticeService.selectList(request);
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getStatus()==1){
                    list.get(i).setPushStatus(EnumNotice.PUBLISHED.getValue());
                }
            }
        }
        int count = pushNoticeService.selectListCount(request);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }

    /**
     * 发布公告微信显示列表
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public CommonResponse list(){
        List<NoticeResponse> list = pushNoticeService.list();
        return CommonResponse.objectResponse(1, "success", list);
    }

    /**
     * 发布详情
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeDetails",method = RequestMethod.POST)
    public CommonResponse noticeDetails(@RequestBody NoticeRequest request){
        try{
            final NoticeResponse result = this.pushNoticeService.noticeDetails(request.getId());

            return  CommonResponse.objectResponse(1, "success", result);
        }catch (final Throwable throwable){
            log.error("获取详情信息异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 修改公告
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateNotice",method = RequestMethod.POST)
    public CommonResponse updateNotice(@RequestBody NoticeRequest request){
        try{
            pushNoticeService.updateNotice(request.getTitle(),request.getText());

            return CommonResponse.simpleResponse(1, "发布成功");
        }catch (final Throwable throwable){
            log.error("修改异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 删除公告
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteNotice",method = RequestMethod.POST)
    public CommonResponse deleteNotice(@RequestBody NoticeRequest request){
        try{
            pushNoticeService.deleteNotice(request.getId());

            return CommonResponse.simpleResponse(1, "删除成功");
        }catch (final Throwable throwable){
            log.error("删除异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 微信端显示公告
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pageAnnouncement",method = RequestMethod.POST)
    public CommonResponse pageAnnouncement(){
        try{
            final NoticeResponse result = this.pushNoticeService.pageAnnouncement();

            return  CommonResponse.objectResponse(1, "success", result);
        }catch (final Throwable throwable){
            log.error("获取详情信息异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }


}
