package com.jkm.hss.controller.admin;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.entity.AdminUserPassport;
import com.jkm.hss.admin.entity.DataDictionary;
import com.jkm.hss.admin.helper.requestparam.DataDictionaryRequest;
import com.jkm.hss.admin.service.DataDictionaryService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.helper.DealerConsts;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.AdminUserLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xingliujie on 2017/2/22.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/dict")
public class DataDictionaryController extends BaseController{
    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 根据类型查询列表
     * @param dataDictionaryRequest
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectAllByType", method = RequestMethod.POST)
    public CommonResponse selectAllByType(@RequestBody final DataDictionaryRequest dataDictionaryRequest, final HttpServletResponse response) {
        List<DataDictionary> list =  dataDictionaryService.selectAllByType(dataDictionaryRequest.getDictType());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }
}
