package com.jkm.hss.controller.channel;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.UpgradeAndRecommendRequest;
import com.jkm.hss.helper.request.UpgradeRequest;
import com.jkm.hss.helper.response.UpgradeRulesAndRateResponse;
import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.UpgradeRecommendRules;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.enums.EnumUpgrade;
import com.jkm.hss.product.helper.response.PartnerRuleSettingResponse;
import com.jkm.hss.product.helper.response.ProductAndBasicResponse;
import com.jkm.hss.product.helper.response.UpgradeRulesResponse;
import com.jkm.hss.product.servcie.*;
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
    @Autowired
    private PartnerRuleSettingService partnerRuleSettingService;


    /**
     * 通道列表
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getProductChannelList", method = RequestMethod.POST)
    public CommonResponse getProductChannelList(@RequestBody final UpgradeRequest req) {
        List<ProductAndBasicResponse> productAndBasicResponses = productChannelDetailService.getProductChannelList(req.getProductId());
        return CommonResponse.objectResponse(1, "查询成功", productAndBasicResponses);
    }

    /**
     * 升级推荐设置
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "init", method = RequestMethod.POST)
    public CommonResponse add(@RequestBody final UpgradeRequest req) {
        UpgradeRulesAndRateResponse upgradeRulesAndRateResponse = new  UpgradeRulesAndRateResponse();
        Optional<Product> productOptional = productService.selectById(req.getProductId());
        if(!productOptional.isPresent()){
            return CommonResponse.simpleResponse(-1,"该产品不存在");
        }
        //商户升级规则设置
        List<PartnerRuleSettingResponse> partnerRuleSettings = partnerRuleSettingService.selectAllByProductId(productOptional.get().getId());
        upgradeRulesAndRateResponse.setPartnerRuleSettingList(partnerRuleSettings);

        List<UpgradeRulesResponse>  upgradeRulesList = new ArrayList<UpgradeRulesResponse>();
        List<UpgradeRules> upgradeRules = upgradeRulesService.selectAll(productOptional.get().getId());
        if(upgradeRules.size()==0){
            UpgradeRulesResponse upgradeRulesResponse = new UpgradeRulesResponse();
            upgradeRulesResponse.setType(EnumUpGradeType.CLERK.getId());
            upgradeRulesResponse.setName(EnumUpGradeType.CLERK.getName());
            upgradeRulesList.add(upgradeRulesResponse);
            UpgradeRulesResponse upgradeRulesResponse1 = new UpgradeRulesResponse();
            upgradeRulesResponse1.setType(EnumUpGradeType.SHOPOWNER.getId());
            upgradeRulesResponse1.setName(EnumUpGradeType.SHOPOWNER.getName());
            upgradeRulesList.add(upgradeRulesResponse1);
            UpgradeRulesResponse upgradeRulesResponse2 = new UpgradeRulesResponse();
            upgradeRulesResponse2.setType(EnumUpGradeType.BOSS.getId());
            upgradeRulesResponse2.setName(EnumUpGradeType.BOSS.getName());
            upgradeRulesList.add(upgradeRulesResponse2);
        }else{
            for(int i=0;i<upgradeRules.size();i++){
                UpgradeRulesResponse upgradeRulesResponse = new UpgradeRulesResponse();
                upgradeRulesResponse.setType(upgradeRules.get(i).getType());
                upgradeRulesResponse.setName(upgradeRules.get(i).getName());
                upgradeRulesResponse.setDirectPromoteShall(upgradeRules.get(i).getDirectPromoteShall());
                upgradeRulesResponse.setInDirectPromoteShall(upgradeRules.get(i).getInDirectPromoteShall());
                upgradeRulesResponse.setPromotionNum(upgradeRules.get(i).getPromotionNum());
                upgradeRulesResponse.setUpgradeCost(upgradeRules.get(i).getUpgradeCost());
                upgradeRulesList.add(upgradeRulesResponse);
            }
        }
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
            if (req.getUpgradeRulesRequestList().size()>0){
                for (int i=0;i<req.getUpgradeRulesRequestList().size();i++){
                    BigDecimal directPromoteShall = req.getUpgradeRulesRequestList().get(i).getDirectPromoteShall();
                    BigDecimal inDirectPromoteShall = req.getUpgradeRulesRequestList().get(i).getInDirectPromoteShall();
                    BigDecimal upgradeCost = req.getUpgradeRulesRequestList().get(i).getUpgradeCost();
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
        if(req.getPartnerRuleSettingList()==null||req.getPartnerRuleSettingList().size()==0){
            return CommonResponse.simpleResponse(-1,"商户升级规则设置不能为空");
        }
        if(req.getUpgradeRulesRequestList()==null||req.getUpgradeRulesRequestList().size()==0){
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
        //商户升级规则
        List<PartnerRuleSetting> partnerRuleSettings = req.getPartnerRuleSettingList();
        BigDecimal b1 = new BigDecimal(100);
        if(partnerRuleSettings.size()<=0){
            return CommonResponse.simpleResponse(-1,"请填写商户费率");
        }else{
            for(int i=0;i<partnerRuleSettings.size();i++){
                if(partnerRuleSettings.get(i).getCommonRate()==null){
                    return CommonResponse.simpleResponse(-1,"普通费率不能为空");
                }
                if(partnerRuleSettings.get(i).getClerkRate()==null){
                    return CommonResponse.simpleResponse(-1,"店员费率不能为空");
                }
                if(partnerRuleSettings.get(i).getShopownerRate()==null){
                    return CommonResponse.simpleResponse(-1,"店长费率不能为空");
                }
                if(partnerRuleSettings.get(i).getBossRate()==null){
                    return CommonResponse.simpleResponse(-1,"老板费率不能为空");
                }
                Optional<PartnerRuleSetting> partnerRuleSettingOptional = partnerRuleSettingService.selectById(partnerRuleSettings.get(i).getId());
                if(partnerRuleSettingOptional.isPresent()){
                    PartnerRuleSetting partnerRuleSetting = new PartnerRuleSetting();
                    partnerRuleSetting.setId(partnerRuleSettingOptional.get().getId());
                    partnerRuleSetting.setProductId(req.getProductId());
                    partnerRuleSetting.setChannelTypeSign(partnerRuleSettings.get(i).getChannelTypeSign());
                    partnerRuleSetting.setDefaultProfitSpace(partnerRuleSettings.get(i).getDefaultProfitSpace().divide(b1));
                    partnerRuleSetting.setCommonRate(partnerRuleSettings.get(i).getCommonRate().divide(b1));
                    partnerRuleSetting.setClerkRate(partnerRuleSettings.get(i).getClerkRate().divide(b1));
                    partnerRuleSetting.setShopownerRate(partnerRuleSettings.get(i).getShopownerRate().divide(b1));
                    partnerRuleSetting.setBossRate(partnerRuleSettings.get(i).getBossRate().divide(b1));
                    partnerRuleSetting.setStatus(EnumUpgrade.NORMAL.getId());
                    partnerRuleSettingService.update(partnerRuleSetting);
                }else{
                    PartnerRuleSetting partnerRuleSetting = new PartnerRuleSetting();
                    partnerRuleSetting.setProductId(req.getProductId());
                    partnerRuleSetting.setChannelTypeSign(partnerRuleSettings.get(i).getChannelTypeSign());
                    partnerRuleSetting.setDefaultProfitSpace(partnerRuleSettings.get(i).getDefaultProfitSpace().divide(b1));
                    partnerRuleSetting.setCommonRate(partnerRuleSettings.get(i).getCommonRate().divide(b1));
                    partnerRuleSetting.setClerkRate(partnerRuleSettings.get(i).getClerkRate().divide(b1));
                    partnerRuleSetting.setShopownerRate(partnerRuleSettings.get(i).getShopownerRate().divide(b1));
                    partnerRuleSetting.setBossRate(partnerRuleSettings.get(i).getBossRate().divide(b1));
                    partnerRuleSetting.setStatus(EnumUpgrade.NORMAL.getId());
                    partnerRuleSettingService.insert(partnerRuleSetting);
                }
            }
        }
        //商户升级规则设置不能为空
        List<UpgradeRules> upgradeRulesList =  upgradeRulesService.selectAll(req.getProductId());//升级规则
        if(upgradeRulesList.size()>0){//修改
            for(int i=0;i<req.getUpgradeRulesRequestList().size();i++){
                Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(req.getProductId(),req.getUpgradeRulesRequestList().get(i).getType());
                if(upgradeRulesOptional.isPresent()){//存在修改
                    UpgradeRules upgradeRules = new UpgradeRules();
                    upgradeRules.setId(upgradeRulesOptional.get().getId());
                    upgradeRules.setProductId(req.getProductId());
                    upgradeRules.setName(req.getUpgradeRulesRequestList().get(i).getName());
                    upgradeRules.setType(req.getUpgradeRulesRequestList().get(i).getType());
                    upgradeRules.setPromotionNum(req.getUpgradeRulesRequestList().get(i).getPromotionNum());
                    upgradeRules.setUpgradeCost(req.getUpgradeRulesRequestList().get(i).getUpgradeCost());
                    upgradeRules.setDirectPromoteShall(req.getUpgradeRulesRequestList().get(i).getDirectPromoteShall());
                    upgradeRules.setInDirectPromoteShall(req.getUpgradeRulesRequestList().get(i).getInDirectPromoteShall());
                    upgradeRules.setStatus(EnumUpgrade.NORMAL.getId());
                    upgradeRulesService.update(upgradeRules);
                }else{//不存在新增
                    UpgradeRules upgradeRules = new UpgradeRules();
                    upgradeRules.setProductId(req.getProductId());
                    upgradeRules.setName(req.getUpgradeRulesRequestList().get(i).getName());
                    upgradeRules.setType(req.getUpgradeRulesRequestList().get(i).getType());
                    upgradeRules.setPromotionNum(req.getUpgradeRulesRequestList().get(i).getPromotionNum());
                    upgradeRules.setUpgradeCost(req.getUpgradeRulesRequestList().get(i).getUpgradeCost());
                    upgradeRules.setDirectPromoteShall(req.getUpgradeRulesRequestList().get(i).getDirectPromoteShall());
                    upgradeRules.setInDirectPromoteShall(req.getUpgradeRulesRequestList().get(i).getInDirectPromoteShall());
                    upgradeRules.setStatus(EnumUpgrade.NORMAL.getId());
                    upgradeRulesService.insert(upgradeRules);
                }
            }
        }else{//新增
            for(int i=0;i<req.getUpgradeRulesRequestList().size();i++){
                UpgradeRules upgradeRules = new UpgradeRules();
                upgradeRules.setProductId(req.getProductId());
                upgradeRules.setName(req.getUpgradeRulesRequestList().get(i).getName());
                upgradeRules.setType(req.getUpgradeRulesRequestList().get(i).getType());
                upgradeRules.setStatus(EnumUpgrade.NORMAL.getId());
                upgradeRules.setUpgradeCost(req.getUpgradeRulesRequestList().get(i).getUpgradeCost());
                upgradeRules.setPromotionNum(req.getUpgradeRulesRequestList().get(i).getPromotionNum());
                upgradeRules.setDirectPromoteShall(req.getUpgradeRulesRequestList().get(i).getDirectPromoteShall());
                upgradeRules.setInDirectPromoteShall(req.getUpgradeRulesRequestList().get(i).getInDirectPromoteShall());
                upgradeRulesService.insert(upgradeRules);
            }
        }
        //升级推荐分润设置及达标标准设置
        Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(req.getProductId());
        BigDecimal upgradeRate = req.getUpgradeRate();
        BigDecimal tradeRate = req.getTradeRate();
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
