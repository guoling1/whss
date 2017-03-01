package com.jkm.hss.controller.channel;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.ChannelAddRequest;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumBasicChannelStatus;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.BasicChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-29.
 */
@Controller
@RequestMapping(value = "/admin/channel")
@Slf4j
public class ChannelController extends BaseController {

    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private AccountService accountService;
    /**
     * 添加通道
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public CommonResponse add(@RequestBody final ChannelAddRequest request) {
        try{
            final Optional<BasicChannel> basicChannelOptional1 = this.basicChannelService.selectByChannelTypeSign(EnumPayChannelSign.of(request.getChannelName()).getId());
            Preconditions.checkNotNull(!basicChannelOptional1.isPresent(), "该通道已经存在");
            //判断该通道的渠道来源是否同属一个,若一个则使用同一个人资金帐号,若不同,则为该渠道创建新的资金帐号
            final Optional<BasicChannel> basicChannelOptional =
                    this.basicChannelService.selectByChannelSource(request.getChannelSource());
            final BasicChannel basicChannel = new BasicChannel();
            if (basicChannelOptional.isPresent()){
                //如果已经有了
                basicChannel.setAccountId(basicChannelOptional.get().getAccountId());
            }else{
                //没有,则创建
                final long accountId = this.accountService.initAccount("基本通道账户");
                basicChannel.setAccountId(accountId);
            }
            basicChannel.setChannelTypeSign(EnumPayChannelSign.of(request.getChannelName()).getId());
            basicChannel.setChannelName(request.getChannelName());
            basicChannel.setThirdCompany(request.getThirdCompany());
            basicChannel.setChannelSource(request.getChannelSource());
            basicChannel.setBasicTradeRate(request.getBasicTradeRate().divide(new BigDecimal(100)));
            basicChannel.setBasicWithdrawFee(request.getBasicWithdrawFee());
            basicChannel.setBasicBalanceType(request.getBasicBalanceType());
            basicChannel.setStatus(EnumBasicChannelStatus.USEING.getId());
            this.basicChannelService.add(basicChannel);
            return CommonResponse.simpleResponse(1,"success");
         }catch(final Throwable throwable){
            log.error("添加通道失败,异常信息:" + throwable.getMessage());
        }
        return  CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 获取通道列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list() {
        try{
            final List<BasicChannel> list = this.basicChannelService.selectAll();
            if (list.size()>0){
                for (int i=0;i<list.size();i++){
                    BigDecimal basicTradeRate = list.get(i).getBasicTradeRate();
                    BigDecimal res = new BigDecimal(100);
                    list.get(i).setBasicTradeRate(basicTradeRate.multiply(res));
                }
            }
            return  CommonResponse.objectResponse(1, "success", list);
        }catch (final Throwable throwable){
            log.error("获取通道列表异常,异常信息:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 修改通道
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public CommonResponse update(@RequestBody final BasicChannel request) {
        try{
            final BasicChannel basicChannel = this.basicChannelService.selectById(request.getId());
            request.setBasicTradeRate(request.getBasicTradeRate().divide(new BigDecimal(100)));
            request.setChannelTypeSign(basicChannel.getChannelTypeSign());
            this.basicChannelService.update(request);
            return  CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable throwable){
            log.error("获取通道列表异常,异常信息:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }


}
