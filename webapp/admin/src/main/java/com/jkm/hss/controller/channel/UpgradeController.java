package com.jkm.hss.controller.channel;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.UpgradeAndRecommendRequest;
import com.jkm.hss.helper.request.UpgradeRequest;
import com.jkm.hss.helper.response.UpgradeRulesAndRateResponse;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.entity.UpgradeRecommendRules;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hss.product.enums.EnumUpgrade;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRecommendRulesService;
import com.jkm.hss.product.servcie.UpgradeRulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
@Controller
@RequestMapping(value = "/admin/upgrade")
@Slf4j
public class UpgradeController extends BaseController {
    @Autowired
    private UpgradeRulesService upgradeRulesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private UpgradeRecommendRulesService upgradeRecommendRulesService;

    /**
     * 升级推荐设置
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "init", method = RequestMethod.POST)
    public CommonResponse add(@RequestBody final UpgradeRequest req) {
        Optional<Product> productOptional = productService.selectById(req.getProductId());
        if(!productOptional.isPresent()){
            return CommonResponse.simpleResponse(-1,"该产品不存在");
        }
//        List<ProductChannelDetail> productChannelDetails = productChannelDetailService.selectByProductId(req.getProductId());
//        if(productChannelDetails.size()==0){
//            return CommonResponse.simpleResponse(-1,"该产品商户基础费率不存在");
//        }
        Optional<ProductChannelDetail> weixinProductChannelDetail = productChannelDetailService.selectRateByProductIdAndChannelType(req.getProductId(), EnumPaymentChannel.WECHAT_PAY.getId());
        if(!weixinProductChannelDetail.isPresent()){
            return CommonResponse.simpleResponse(-1,"微信基础费率不存在");
        }
        Optional<ProductChannelDetail> zhifubaoProductChannelDetail = productChannelDetailService.selectRateByProductIdAndChannelType(req.getProductId(), EnumPaymentChannel.ALIPAY.getId());
        if(!zhifubaoProductChannelDetail.isPresent()){
            return CommonResponse.simpleResponse(-1,"支付宝基础费率不存在");
        }
        Optional<ProductChannelDetail> fastPayProductChannelDetail = productChannelDetailService.selectRateByProductIdAndChannelType(req.getProductId(), EnumPaymentChannel.UNIONPAY.getId());
        if(!fastPayProductChannelDetail.isPresent()){
            return CommonResponse.simpleResponse(-1,"快捷基础费率不存在");
        }
        //商户升级规则设置
        List<UpgradeRules> upgradeRulesList = new ArrayList<UpgradeRules>();
        UpgradeRules upgradeRules = new UpgradeRules();
//        for(int i=0;i<productChannelDetails.size();i++){
//            if(EnumPayChannelSign.YG_WECHAT.getId()==productChannelDetails.get(i).getChannelTypeSign()){
//                BigDecimal weixinRate = productChannelDetails.get(i).getProductMerchantPayRate();
//                BigDecimal b1 = new BigDecimal(100);
//                upgradeRules.setWeixinRate(weixinRate.multiply(b1));
//            }
//            if(EnumPayChannelSign.YG_ALIPAY.getId()==productChannelDetails.get(i).getChannelTypeSign()){
//                BigDecimal alipayRate = productChannelDetails.get(i).getProductMerchantPayRate();
//                BigDecimal b1 = new BigDecimal(100);
//                upgradeRules.setAlipayRate(alipayRate.multiply(b1));
//            }
//            if(EnumPayChannelSign.YG_UNIONPAY.getId()==productChannelDetails.get(i).getChannelTypeSign()){
//                BigDecimal fastRate = productChannelDetails.get(i).getProductMerchantPayRate();
//                BigDecimal b1 = new BigDecimal(100);
//                upgradeRules.setFastRate(fastRate.multiply(b1));
//            }
//        }

        BigDecimal weixinRate1 = weixinProductChannelDetail.get().getProductMerchantPayRate();
        BigDecimal a1 = new BigDecimal(100);
        upgradeRules.setWeixinRate(weixinRate1.multiply(a1));

        BigDecimal alipayRate1 = zhifubaoProductChannelDetail.get().getProductMerchantPayRate();
        BigDecimal a2 = new BigDecimal(100);
        upgradeRules.setAlipayRate(alipayRate1.multiply(a2));

        BigDecimal fastRate1 = fastPayProductChannelDetail.get().getProductMerchantPayRate();
        BigDecimal a3 = new BigDecimal(100);
        upgradeRules.setFastRate(fastRate1.multiply(a3));

        upgradeRulesList.add(upgradeRules);
        List<UpgradeRules> upgradeRulesArr =  upgradeRulesService.selectAll(req.getProductId());//升级规则

        if (upgradeRulesArr.size()==0){
            for(int i=1;i<4;i++){
                UpgradeRules upgradeRulesTemp = new UpgradeRules();
                upgradeRulesTemp.setType(i);
                upgradeRulesList.add(upgradeRulesTemp);
            }
        }else {
            for (int i=0;i<upgradeRulesArr.size();i++){
                if(upgradeRulesArr.get(i).getType()==1||upgradeRulesArr.get(i).getType()==2||upgradeRulesArr.get(i).getType()==3){
//                    final CommonResponse commonResponse = this.rewardJudge(upgradeRulesArr);
//                    if (1 != commonResponse.getCode()) {
//                        return commonResponse;
//                    }
                    BigDecimal weixinRate = upgradeRulesArr.get(i).getWeixinRate();
                    BigDecimal alipayRate = upgradeRulesArr.get(i).getAlipayRate();
                    BigDecimal fastRate = upgradeRulesArr.get(i).getFastRate();
                    BigDecimal b1 = new BigDecimal(100);
                    upgradeRulesArr.get(i).setWeixinRate(weixinRate.multiply(b1));
                    upgradeRulesArr.get(i).setAlipayRate(alipayRate.multiply(b1));
                    upgradeRulesArr.get(i).setFastRate(fastRate.multiply(b1));
                }
            }

        }


        upgradeRulesList.addAll(upgradeRulesArr);
        UpgradeRulesAndRateResponse upgradeRulesAndRateResponse = new  UpgradeRulesAndRateResponse();
        upgradeRulesAndRateResponse.setUpgradeRulesList(upgradeRulesList);
        //升级推荐分润设置及达标标准设置
        Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(req.getProductId());
        if(upgradeRecommendRulesOptional.isPresent()){
            BigDecimal upgradeRate = upgradeRecommendRulesOptional.get().getUpgradeRate();
            BigDecimal tradeRate = upgradeRecommendRulesOptional.get().getTradeRate();
            BigDecimal b1 = new BigDecimal(100);
            upgradeRulesAndRateResponse.setUpgradeRate(upgradeRate.multiply(b1));
            upgradeRulesAndRateResponse.setTradeRate(tradeRate.multiply(b1));
            upgradeRulesAndRateResponse.setStandard(upgradeRecommendRulesOptional.get().getInviteStandard());
        }
        return CommonResponse.objectResponse(1, "success", upgradeRulesAndRateResponse);
    }

    /**
     *
     * 判断直接奖励+间接奖励是否小于升级费
     * @param req
     * @param req
     */
    private CommonResponse rewardJudge(UpgradeAndRecommendRequest req){
        if (req!=null){
            if (req.getUpgradeRulesList().size()>0){
                for (int i=0;i<req.getUpgradeRulesList().size();i++){
                    BigDecimal directPromoteShall = req.getUpgradeRulesList().get(i).getDirectPromoteShall();
                    BigDecimal inDirectPromoteShall = req.getUpgradeRulesList().get(i).getInDirectPromoteShall();
                    BigDecimal upgradeCost = req.getUpgradeRulesList().get(i).getUpgradeCost();
                    int res = upgradeCost.compareTo(directPromoteShall.add(inDirectPromoteShall));
                    if (res==-1){
                        return CommonResponse.simpleResponse(-1, "升级费必须大于等于直接奖励加间接奖励");
                    }
                }

            }
        }


        return CommonResponse.simpleResponse(1, "");
    }


    /**
     * 添加或修改
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    public CommonResponse addOrUpdate(@RequestBody final UpgradeAndRecommendRequest req) {
        if(req.getUpgradeRulesList()==null||req.getUpgradeRulesList().size()==0){
            return CommonResponse.simpleResponse(-1,"商户升级规则设置不能为空");
        }
        Optional<Product> productOptional = productService.selectById(req.getProductId());
        if(!productOptional.isPresent()){
            return CommonResponse.simpleResponse(-1,"该产品不存在");
        }
        final CommonResponse commonResponse = this.rewardJudge(req);
        if (1 != commonResponse.getCode()) {
            return commonResponse;
        }
        //商户升级规则设置不能为空
        List<UpgradeRules> upgradeRulesList =  upgradeRulesService.selectAll(req.getProductId());//升级规则
        if(upgradeRulesList.size()>0){//修改
            for(int i=0;i<req.getUpgradeRulesList().size();i++){
                Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(req.getProductId(),req.getUpgradeRulesList().get(i).getType());
                if(upgradeRulesOptional.isPresent()){//存在修改
                    UpgradeRules upgradeRules = new UpgradeRules();
                    BigDecimal weixinRate = req.getUpgradeRulesList().get(i).getWeixinRate();
                    BigDecimal alipayRate = req.getUpgradeRulesList().get(i).getAlipayRate();
                    BigDecimal fastRate = req.getUpgradeRulesList().get(i).getFastRate();
                    BigDecimal b1 = new BigDecimal(100);
                    upgradeRules.setId(upgradeRulesOptional.get().getId());
                    upgradeRules.setProductId(req.getProductId());
                    upgradeRules.setName(req.getUpgradeRulesList().get(i).getName());
                    upgradeRules.setType(req.getUpgradeRulesList().get(i).getType());
                    upgradeRules.setPromotionNum(req.getUpgradeRulesList().get(i).getPromotionNum());
                    upgradeRules.setUpgradeCost(req.getUpgradeRulesList().get(i).getUpgradeCost());
                    upgradeRules.setWeixinRate(weixinRate.divide(b1));
                    upgradeRules.setAlipayRate(alipayRate.divide(b1));
                    upgradeRules.setFastRate(fastRate.divide(b1));
                    upgradeRules.setDirectPromoteShall(req.getUpgradeRulesList().get(i).getDirectPromoteShall());
                    upgradeRules.setInDirectPromoteShall(req.getUpgradeRulesList().get(i).getInDirectPromoteShall());
                    upgradeRules.setStatus(EnumUpgrade.NORMAL.getId());
                    upgradeRulesService.update(upgradeRules);
                }else{//不存在新增
                    BigDecimal weixinRate = req.getUpgradeRulesList().get(i).getWeixinRate();
                    BigDecimal alipayRate = req.getUpgradeRulesList().get(i).getAlipayRate();
                    BigDecimal fastRate = req.getUpgradeRulesList().get(i).getFastRate();
                    BigDecimal b1 = new BigDecimal(100);
                    req.getUpgradeRulesList().get(i).setStatus(EnumUpgrade.NORMAL.getId());
                    req.getUpgradeRulesList().get(i).setProductId(req.getProductId());
                    req.getUpgradeRulesList().get(i).setProductId(req.getProductId());
                    req.getUpgradeRulesList().get(i).setWeixinRate(weixinRate.divide(b1));
                    req.getUpgradeRulesList().get(i).setAlipayRate(alipayRate.divide(b1));
                    req.getUpgradeRulesList().get(i).setFastRate(fastRate.divide(b1));
                    upgradeRulesService.insert(req.getUpgradeRulesList().get(i));
                }
            }
        }else{//新增
            for(int i=0;i<req.getUpgradeRulesList().size();i++){
                BigDecimal weixinRate = req.getUpgradeRulesList().get(i).getWeixinRate();
                BigDecimal alipayRate = req.getUpgradeRulesList().get(i).getAlipayRate();
                BigDecimal fastRate = req.getUpgradeRulesList().get(i).getFastRate();
                BigDecimal b1 = new BigDecimal(100);
                req.getUpgradeRulesList().get(i).setStatus(EnumUpgrade.NORMAL.getId());
                req.getUpgradeRulesList().get(i).setProductId(req.getProductId());
                req.getUpgradeRulesList().get(i).setWeixinRate(weixinRate.divide(b1));
                req.getUpgradeRulesList().get(i).setAlipayRate(alipayRate.divide(b1));
                req.getUpgradeRulesList().get(i).setFastRate(fastRate.divide(b1));
                upgradeRulesService.insert(req.getUpgradeRulesList().get(i));
            }
        }
        //升级推荐分润设置及达标标准设置
        Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(req.getProductId());
        BigDecimal upgradeRate = req.getUpgradeRate();
        BigDecimal tradeRate = req.getTradeRate();
        BigDecimal rewardRate = req.getRewardRate();
        BigDecimal b1 = new BigDecimal(100);
        if(upgradeRecommendRulesOptional.isPresent()){//修改
            upgradeRecommendRulesOptional.get().setInviteStandard(req.getStandard());
            upgradeRecommendRulesOptional.get().setUpgradeRate(upgradeRate.divide(b1));
            upgradeRecommendRulesOptional.get().setTradeRate(tradeRate.divide(b1));
            upgradeRecommendRulesService.update(upgradeRecommendRulesOptional.get());
        }else{//新增
            UpgradeRecommendRules upgradeRecommendRules = new UpgradeRecommendRules();
            upgradeRecommendRules.setStatus(EnumUpgrade.NORMAL.getId());
            upgradeRecommendRules.setProductId(req.getProductId());
            upgradeRecommendRules.setInviteStandard(req.getStandard());
            upgradeRecommendRules.setUpgradeRate(upgradeRate.divide(b1));
            upgradeRecommendRules.setTradeRate(tradeRate.divide(b1));
            upgradeRecommendRulesService.insert(upgradeRecommendRules);
        }
        return CommonResponse.simpleResponse(1, "操作成功");
    }


}
