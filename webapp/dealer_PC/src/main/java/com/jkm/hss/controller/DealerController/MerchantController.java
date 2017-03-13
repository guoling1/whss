package com.jkm.hss.controller.DealerController;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.MerchantListRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by xingliujie on 2017/3/3.
 */
@Slf4j
@RestController
@RequestMapping(value = "/daili/merchant")
public class MerchantController extends BaseController {
    /**
     * 商户列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/merchantList", method = RequestMethod.POST)
    public CommonResponse merchantList(final HttpServletResponse response,@RequestBody final MerchantListRequest merchantListRequest) {
        long dealerId = super.getDealerId();
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", "");
    }
}
