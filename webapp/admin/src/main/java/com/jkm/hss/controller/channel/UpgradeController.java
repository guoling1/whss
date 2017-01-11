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
        List<ProductChannelDetail> productChannelDetails = productChannelDetailService.selectByProductId(req.getProductId());
        if(productChannelDetails.size()==0){
            return CommonResponse.simpleResponse(-1,"该产品商户基础费率不存在");
        }
        //商户升级规则设置
        List<UpgradeRules> upgradeRulesList = new ArrayList<UpgradeRules>();
        UpgradeRules upgradeRules = new UpgradeRules();
        for(int i=0;i<productChannelDetails.size();i++){
            if(EnumPayChannelSign.YG_WEIXIN.getId()==productChannelDetails.get(i).getChannelTypeSign()){
                upgradeRules.setWeixinRate(productChannelDetails.get(i).getProductMerchantPayRate());
            }
            if(EnumPayChannelSign.YG_ZHIFUBAO.getId()==productChannelDetails.get(i).getChannelTypeSign()){
                upgradeRules.setAlipayRate(productChannelDetails.get(i).getProductMerchantPayRate());
            }
            if(EnumPayChannelSign.YG_YINLIAN.getId()==productChannelDetails.get(i).getChannelTypeSign()){
                upgradeRules.setFastRate(productChannelDetails.get(i).getProductMerchantPayRate());
            }
        }
        upgradeRulesList.add(upgradeRules);
        List<UpgradeRules> upgradeRulesArr =  upgradeRulesService.selectAll(req.getProductId());//升级规则

        if (upgradeRulesArr.size()==0){
            for(int i=1;i<4;i++){
                UpgradeRules upgradeRulesTemp = new UpgradeRules();
                upgradeRulesTemp.setType(i);
                upgradeRulesList.add(upgradeRulesTemp);
            }
        }

//        if(upgradeRulesArr.size()>0){
//            for(int j=0;j<upgradeRulesArr.size();j++){
//                upgradeRulesList.add(upgradeRulesArr.get(j).getType(),upgradeRulesArr.get(j));
//            }
//        }else {
//            for(int i=1;i<4;i++){
//                UpgradeRules upgradeRulesTemp = new UpgradeRules();
//                upgradeRulesTemp.setType(i);
//                upgradeRulesList.add(upgradeRulesTemp);
//            }
//        }
        upgradeRulesList.addAll(upgradeRulesArr);
        UpgradeRulesAndRateResponse upgradeRulesAndRateResponse = new  UpgradeRulesAndRateResponse();
        upgradeRulesAndRateResponse.setUpgradeRulesList(upgradeRulesList);
        //升级推荐分润设置及达标标准设置
        Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(req.getProductId());
        if(upgradeRecommendRulesOptional.isPresent()){
            upgradeRulesAndRateResponse.setStandard(upgradeRecommendRulesOptional.get().getInviteStandard());
            upgradeRulesAndRateResponse.setRewardRate(upgradeRecommendRulesOptional.get().getRewardRate());
            upgradeRulesAndRateResponse.setTradeRate(upgradeRecommendRulesOptional.get().getTradeRate());
            upgradeRulesAndRateResponse.setUpgradeRate(upgradeRecommendRulesOptional.get().getUpgradeRate());
        }
        return CommonResponse.objectResponse(1, "success", upgradeRulesAndRateResponse);
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
        //商户升级规则设置不能为空
        List<UpgradeRules> upgradeRulesList =  upgradeRulesService.selectAll(req.getProductId());//升级规则
        if(upgradeRulesList.size()>0){//修改
            for(int i=0;i<req.getUpgradeRulesList().size();i++){
                Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(req.getProductId(),req.getUpgradeRulesList().get(i).getType());
                if(upgradeRulesOptional.isPresent()){//存在修改
                    UpgradeRules upgradeRules = new UpgradeRules();
                    upgradeRules.setId(upgradeRulesOptional.get().getId());
                    upgradeRules.setProductId(req.getProductId());
                    upgradeRules.setName(req.getUpgradeRulesList().get(i).getName());
                    upgradeRules.setType(req.getUpgradeRulesList().get(i).getType());
                    upgradeRules.setPromotionNum(req.getUpgradeRulesList().get(i).getPromotionNum());
                    upgradeRules.setUpgradeCost(req.getUpgradeRulesList().get(i).getUpgradeCost());
                    upgradeRules.setWeixinRate(req.getUpgradeRulesList().get(i).getWeixinRate());
                    upgradeRules.setAlipayRate(req.getUpgradeRulesList().get(i).getAlipayRate());
                    upgradeRules.setFastRate(req.getUpgradeRulesList().get(i).getFastRate());
                    upgradeRules.setAlipayRate(req.getUpgradeRulesList().get(i).getAlipayRate());
                    upgradeRules.setDirectPromoteShall(req.getUpgradeRulesList().get(i).getDirectPromoteShall());
                    upgradeRules.setInDirectPromoteShall(req.getUpgradeRulesList().get(i).getInDirectPromoteShall());
                    upgradeRules.setStatus(EnumUpgrade.NORMAL.getId());
                    upgradeRulesService.update(upgradeRules);
                }else{//不存在新增
                    req.getUpgradeRulesList().get(i).setStatus(EnumUpgrade.NORMAL.getId());
                    req.getUpgradeRulesList().get(i).setProductId(req.getProductId());
                    upgradeRulesService.insert(req.getUpgradeRulesList().get(i));
                }
            }
        }else{//新增
            for(int i=0;i<req.getUpgradeRulesList().size();i++){
                req.getUpgradeRulesList().get(i).setStatus(EnumUpgrade.NORMAL.getId());
                req.getUpgradeRulesList().get(i).setProductId(req.getProductId());
                upgradeRulesService.insert(req.getUpgradeRulesList().get(i));
            }
        }
        //升级推荐分润设置及达标标准设置
        Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(req.getProductId());
        if(upgradeRecommendRulesOptional.isPresent()){//修改
            upgradeRecommendRulesOptional.get().setInviteStandard(req.getStandard());
            upgradeRecommendRulesOptional.get().setUpgradeRate(req.getUpgradeRate());
            upgradeRecommendRulesOptional.get().setTradeRate(req.getTradeRate());
            upgradeRecommendRulesOptional.get().setRewardRate(req.getRewardRate());
            upgradeRecommendRulesService.update(upgradeRecommendRulesOptional.get());
        }else{//新增
            UpgradeRecommendRules upgradeRecommendRules = new UpgradeRecommendRules();
            upgradeRecommendRules.setStatus(EnumUpgrade.NORMAL.getId());
            upgradeRecommendRules.setProductId(req.getProductId());
            upgradeRecommendRules.setInviteStandard(req.getStandard());
            upgradeRecommendRules.setUpgradeRate(req.getUpgradeRate());
            upgradeRecommendRules.setTradeRate(req.getTradeRate());
            upgradeRecommendRules.setRewardRate(req.getRewardRate());
            upgradeRecommendRulesService.insert(upgradeRecommendRules);
        }
        return CommonResponse.simpleResponse(1, "操作成功");
    }


}
