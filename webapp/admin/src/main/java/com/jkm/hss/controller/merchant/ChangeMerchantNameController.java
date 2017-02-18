package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.merchant.helper.request.ChangeRequest;
import com.jkm.hss.merchant.service.ChangeMerchantNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangbin on 2017/2/18.
 */
@Controller
@RequestMapping(value = "/admin/changeMerchantName")
public class ChangeMerchantNameController {

    @Autowired
    private ChangeMerchantNameService changeMerchantNameService;

    @ResponseBody
    @RequestMapping(value = "/change",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> chang(@RequestBody ChangeRequest req){

        changeMerchantNameService.updatChangeName(req.getId(),req.getMerchantChangeName());

        return CommonResponse.simpleResponse(1,"修改成功");
    }

}
