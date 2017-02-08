package com.jkm.hss.controller.userShop;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.response.UserShopResponse;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hsy.user.Enum.EnumUserStatus;
import com.jkm.hsy.user.entity.HsyUser;
import com.jkm.hsy.user.entity.HsyUserShop;
import com.jkm.hsy.user.help.requestparam.UserRequest;
import com.jkm.hsy.user.service.HsyUserDyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by longwen.jiang on 2017-01-13
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserShopController extends BaseController {

    @Autowired
    private HsyUserDyService hsyUserService;

    @Autowired
    private SendMessageService sendMessageService;




    /**
     * 根据商户ID查询所有门店员工信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserShopListById" , method = RequestMethod.POST)
    public CommonResponse findUserShopListById(final HttpServletRequest request ,@RequestBody final UserRequest userRequest) {

        //String uid = request.getParameter("uid");

        String uid = userRequest.getUid();
        try{
            Preconditions.checkState(!Strings.isNullOrEmpty(uid), "条件不能为空");
        }catch (Exception e){
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
       // Integer  uids= Integer.parseInt(uid);
        PageModel<HsyUserShop> pageModel=hsyUserService.findHsyUserShopListByMerchantId(userRequest);
//        final List<UserShopResponse> responses = Lists.transform(dealerList, new Function<HsyUserShop, UserShopResponse>() {
//            @Override
//            public UserShopResponse apply(HsyUserShop input) {
//                final UserShopResponse userShopResponse = new UserShopResponse();
//                userShopResponse.setUid(input.getUid());
//                userShopResponse.setNickName(input.getUser().getNickName());
//                userShopResponse.setSid(input.getSid());
//                userShopResponse.setSname(input.getSname());
//                userShopResponse.setStatus(input.getStatus());
//                userShopResponse.setRole(input.getRole());
//                return userShopResponse;
//            }
//        });
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", pageModel);
    }


    /**
     * 根据员工ID查询员工信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserShopDetailById" , method = RequestMethod.POST)
    public CommonResponse findUserShopDetailById(final HttpServletRequest request,@RequestBody final UserRequest userRequest) {

      //  String uid = request.getParameter("id");

        String uid = userRequest.getUid();
        try{
        Preconditions.checkState(!Strings.isNullOrEmpty(uid), "条件不能为空");
        }catch (Exception e){
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
        Long  uids= Long.parseLong(uid);
        HsyUserShop  us=hsyUserService.findHsyUserShopById(uids);


        final UserShopResponse userShopResponse = new UserShopResponse();
        userShopResponse.setUid(us.getUid());
        userShopResponse.setNickName(us.getUser().getNickName());
        userShopResponse.setCellphone(us.getUser().getCellphone());
        userShopResponse.setIdcard(us.getUser().getIdcard());
        userShopResponse.setIdcardb(us.getUser().getIdcardb());
        userShopResponse.setIdcardf(us.getUser().getIdcardf());
        userShopResponse.setIdcardh(us.getUser().getIdcardh());
        userShopResponse.setRealName(us.getUser().getRealName());
        userShopResponse.setSid(us.getSid());
        userShopResponse.setSname(us.getSname());
        userShopResponse.setStatus(us.getStatus());
        userShopResponse.setRole(us.getRole());



        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", userShopResponse);
    }


    /**
     * 根据商户ID查找店铺信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findShopByMerId" , method = RequestMethod.POST)
    public CommonResponse findShopByMerId(final HttpServletRequest request,@RequestBody final UserRequest userRequest) {

        //  String uid = request.getParameter("id");

        String uid = userRequest.getUid();
        try{
            Preconditions.checkState(!Strings.isNullOrEmpty(uid), "条件不能为空");
        }catch (Exception e){
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
        Long  uids= Long.parseLong(uid);
        List<HsyUserShop > us=hsyUserService.findByMerId(uids);




        final List<UserShopResponse> responses = Lists.transform(us, new Function<HsyUserShop, UserShopResponse>() {
            @Override
            public UserShopResponse apply(HsyUserShop input) {
                final UserShopResponse userShopResponse = new UserShopResponse();
                userShopResponse.setSid(input.getSid());
                userShopResponse.setSname(input.getSname());
                return userShopResponse;
            }
        });

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", responses);
    }


    /**
     *新增员工信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addUser" , method = RequestMethod.POST)
    public CommonResponse addUser(final HttpServletRequest request ,@RequestBody final UserRequest userRequest) {

        String uname = userRequest.getUname();
        String role = userRequest.getRole();
        String cellphone = userRequest.getCellphone();
        String sid = userRequest.getSid();


        try {
            Preconditions.checkState(!Strings.isNullOrEmpty(uname), "姓名不能为空");
            Preconditions.checkState(!Strings.isNullOrEmpty(role), "角色不能为空");
            Preconditions.checkState(!Strings.isNullOrEmpty(cellphone), "电话不能为空");
            Preconditions.checkState(!Strings.isNullOrEmpty(sid), "店铺不能为空");
        }catch (Exception e){
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }



        //生成八位随机数字作为密码
        int random=(int)(Math.random()*100000000);

       String dcriptPwd= MerchantSupport.passwordDigest(random+"","JKM");

        final HashMap<String, String> data = new HashMap<>();
        data.put("code", random+"");
        final SendMessageParams params = SendMessageParams.builder()
                .uid("321")
                .mobile(cellphone)
                .noticeType(EnumNoticeType.EMPLOYEE_PASSWORD)
                .userType(EnumUserType.FOREGROUND_USER)
                .data(data)
                .build();
        sendMessageService.sendMessage(params);
        HsyUserShop  hus = new  HsyUserShop();
        hus.setRole(Integer.parseInt(role));
        hus.setSid(Integer.parseInt(sid));
        hus.setCreateTime(new Date());
        hus.setStatus(1);

        HsyUser hu = new HsyUser();
        hu.setCreateTime(new Date());
        hu.setCellphone(cellphone);
        hu.setNickName(uname);
        hu.setPassword(dcriptPwd);
        hu.setStatus(EnumUserStatus.PENDINGAUDIT.getCode());
        hus.setUser(hu);
        hsyUserService.insert(hus);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", "");
    }


    /**
     * 修改员工信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUser" , method = RequestMethod.POST)
    public CommonResponse updateUser(final HttpServletRequest request,@RequestBody final UserRequest userRequest) {

        String uname = userRequest.getUname();
        String role = userRequest.getRole();
        String cellphone = userRequest.getCellphone();
        String sid = userRequest.getSid();
        String uid = userRequest.getUid();
        String status = userRequest.getStatus();

        Preconditions.checkState(!Strings.isNullOrEmpty(uid), "uid不能为空");

        HsyUserShop  hus = new  HsyUserShop();

        if(role!=null) {
            hus.setRole(Integer.parseInt(role));
        }

        if(sid!=null) {
            hus.setSid(Integer.parseInt(sid));
        }
        hus.setUid(Long.parseLong(uid));
        hus.setUpdateTime(new Date());
       // hus.setStatus(Integer.parseInt(status));

        HsyUser hu = new HsyUser();

        hu.setId(Long.parseLong(uid));
        if(cellphone!=null) {
            hu.setCellphone(cellphone);
        }

        if(uname!=null) {
            hu.setNickName(uname);
        }
        hu.setUpdateTime(new Date());

        if(status!=null){
            hu.setStatus(Integer.parseInt(status));
        }
        hus.setUser(hu);
        hsyUserService.update(hus);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", "");
    }


}
