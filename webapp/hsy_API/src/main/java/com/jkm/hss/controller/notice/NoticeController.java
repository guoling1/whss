package com.jkm.hss.controller.notice;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.jkm.hss.helper.response.HsyNoticeResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wayne on 17/5/4.
 */
@Slf4j
@Controller
@RequestMapping(value = "/notice")
public class NoticeController extends BaseController {
    @Autowired
    private PushNoticeService pushNoticeService;

    /**
     * 公告详情页面
     * @param request
     * @param response
     * @param model
     * @param noticeId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String noticedetail(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "noticeId", required = true) long noticeId){
        model.addAttribute("id",noticeId);
        return "/notice-detail";
    }

    /**
     * 公告列表
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/noticeList",method = RequestMethod.POST)
    public CommonResponse noticeList() throws ParseException {
        NoticeRequest request=new NoticeRequest();
        request.setPageNo(1);
        request.setPageSize(1);
        final PageModel<HsyNoticeResponse> pageModel = new PageModel<HsyNoticeResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        if(request.getEndTime()!=null&&!"".equals(request.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(request.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            request.setEndTime(sdf.format(rightNow.getTime()));
        }
        request.setProductType("hsy");
        List<NoticeResponse> list = pushNoticeService.selectList(request);
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getStatus()==1){
                    list.get(i).setPushStatus(EnumNotice.PUBLISHED.getValue());
                }
            }
        }

        List<HsyNoticeResponse> hsylist= Lists.transform(list, new Function<NoticeResponse, HsyNoticeResponse>() {
            @Override
            public HsyNoticeResponse apply(NoticeResponse noticeResponse) {
                HsyNoticeResponse hsyNoticeResponse=new HsyNoticeResponse();
                hsyNoticeResponse.setCreateTime(noticeResponse.getDates());
                hsyNoticeResponse.setId(noticeResponse.getId());
                hsyNoticeResponse.setStatus(noticeResponse.getStatus());
                hsyNoticeResponse.setPushStatus(noticeResponse.getPushStatus());
                hsyNoticeResponse.setTitle(noticeResponse.getTitle());
                hsyNoticeResponse.setUrl("http://hsy.qianbaojiajia.com/"+noticeResponse.getId());
                return hsyNoticeResponse;
            }
        });
        return CommonResponse.objectResponse(1, "success", hsylist);
    }

    /**
     * 公告详情
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeDetails",method = RequestMethod.POST)
    public CommonResponse noticeDetails(@RequestBody NoticeRequest request){
        try{
            final NoticeResponse result = this.pushNoticeService.noticeDetails(request.getId());
            if(result!=null) {
                if (result.getType().equals("1")) {
                    result.setType(EnumType.MAINTAIN.getValue());
                }
                if (result.getType().equals("2")) {
                    result.setType(EnumType.NOTICE.getValue());
                }
                if(result.getStatus()==1){
                    result.setPushStatus(EnumNotice.PUBLISHED.getValue());
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dates = sdf.format(result.getCreateTime());
                result.setDates(dates);
            }
            return  CommonResponse.objectResponse(1, "success", result);
        }catch (final Throwable throwable){
            log.error("获取详情信息异常:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }

}
