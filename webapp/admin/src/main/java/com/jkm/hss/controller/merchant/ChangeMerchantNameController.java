package com.jkm.hss.controller.merchant;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.merchant.entity.MerchantInfo;
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
    public CommonResponse chang(@RequestBody ChangeRequest req){
        JSONObject jsonObject = new JSONObject();
        changeMerchantNameService.updatChangeName(req.getId(),req.getMerchantChangeName());
        MerchantInfo res = changeMerchantNameService.selectChangeName(req.getId());
        jsonObject.put("changeMerchantName",res.getMerchantName());

        return CommonResponse.objectResponse(1, "success", jsonObject);
    }


}
