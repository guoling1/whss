package com.jkm.hss.controller.notice;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.NoticeRequest;
import com.jkm.hss.merchant.entity.NoticeResponse;
import com.jkm.hss.merchant.enums.EnumNotice;
import com.jkm.hss.merchant.enums.EnumType;
import com.jkm.hss.merchant.service.PushNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2017/3/8.
 */
@Slf4j
@Controller
@RequestMapping(value = "/notice")
public class NoticeController extends BaseController {

    @Autowired
    private PushNoticeService pushNoticeService;

    /**
     * 发布公告列表
     * @param
     * @return
     */

    @RequestMapping(value = "/noticeListJSP",method = RequestMethod.GET)
    public String noticeListJSP() {
        String url="notisList";
        return url;
    }

    /**
     * 发布详情
     * @param
     */
    @RequestMapping(value = "/noticeDetailsJSP",method = RequestMethod.POST)
    public String noticeDetailsJSP(){
        return "notisDetails";
    }

    /**
     * 发布公告列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeList",method = RequestMethod.POST)
    public CommonResponse noticeList(@RequestBody NoticeRequest request) throws ParseException {
        final PageModel<NoticeResponse> pageModel = new PageModel<NoticeResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        if(request.getEndTime()!=null&&!"".equals(request.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(request.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            request.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<NoticeResponse> list = pushNoticeService.selectList(request);
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getStatus()==1){
                    list.get(i).setPushStatus(EnumNotice.PUBLISHED.getValue());
                }
                if (list.get(i).getType().equals("1")){
                    list.get(i).setType(EnumType.MAINTAIN.getValue());
                }
                if (list.get(i).getType().equals("2")){
                    list.get(i).setType(EnumType.NOTICE.getValue());
                }
            }
        }
        int count = pushNoticeService.selectListCount(request);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
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
            if (result.getType().equals("1")){
                result.setType(EnumType.MAINTAIN.getValue());
            }
            if (result.getType().equals("2")){
                result.setType(EnumType.NOTICE.getValue());
            }
            return  CommonResponse.objectResponse(1, "success", result);
        }catch (final Throwable throwable){
            log.error("获取详情信息异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }
}
