package com.jkm.hss.dealer.service.impl;

import com.jkm.hss.dealer.dao.OemInfoDao;
import com.jkm.hss.dealer.dao.TemplateInfoDao;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.entity.TemplateInfo;
import com.jkm.hss.dealer.enums.EnumDealerStatus;
import com.jkm.hss.dealer.helper.requestparam.AddOrUpdateOemRequest;
import com.jkm.hss.dealer.helper.response.OemDetailResponse;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.merchant.enums.EnumCommonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingliujie on 2017/5/2.
 */
@Service
public class OemInfoServiceImpl implements OemInfoService {
    @Autowired
    private OemInfoDao oemInfoDao;
    @Autowired
    private TemplateInfoDao templateInfoDao;
    /**
     * 新增
     *
     * @param oemInfo
     */
    @Override
    public void insert(OemInfo oemInfo) {
        oemInfoDao.insert(oemInfo);
    }

    /**
     * 根据分公司编码查询分公司O单配置
     *
     * @param dealerId
     * @return
     */
    @Override
    public OemDetailResponse selectByDealerId(long dealerId) {
        OemDetailResponse oemDetailResponse = new OemDetailResponse();
        List<OemDetailResponse.TemplateInfo> templateInfos = new ArrayList<>();
        OemInfo oemInfo = oemInfoDao.selectByDealerId(dealerId);
        if(oemInfo!=null){
            oemDetailResponse.setId(oemInfo.getId());
            oemDetailResponse.setBrandName(oemInfo.getBrandName());
            oemDetailResponse.setWechatName(oemInfo.getWechatName());
            oemDetailResponse.setWechatCode(oemInfo.getWechatCode());
            oemDetailResponse.setAppId(oemInfo.getAppId());
            oemDetailResponse.setAppSecret(oemInfo.getAppSecret());
            oemDetailResponse.setLogo(oemInfo.getLogo());
            oemDetailResponse.setQrCode(oemInfo.getQrCode());
            List<TemplateInfo> templateInfoListArr = templateInfoDao.selectByOemId(oemInfo.getId());
            if(templateInfoListArr.size()>0){
                for(int i=0;i<templateInfoListArr.size();i++){
                    OemDetailResponse.TemplateInfo templateInfo = new OemDetailResponse.TemplateInfo();
                    templateInfo.setSignCode(templateInfoListArr.get(i).getSignCode());
                    templateInfo.setTemplateId(templateInfoListArr.get(i).getTemplateId());
                    templateInfo.setTemplateName(templateInfoListArr.get(i).getTemplateName());
                    templateInfos.add(templateInfo);
                }
            }else{
                List<TemplateInfo> templateInfoList = templateInfoDao.selectByOemId(0);
                for(int i=0;i<templateInfoList.size();i++){
                    OemDetailResponse.TemplateInfo templateInfo = new OemDetailResponse.TemplateInfo();
                    templateInfo.setSignCode(templateInfoList.get(i).getSignCode());
                    templateInfo.setTemplateName(templateInfoList.get(i).getTemplateName());
                    templateInfos.add(templateInfo);
                }
            }
        }else{
            List<TemplateInfo> templateInfoList = templateInfoDao.selectByOemId(0);
            for(int i=0;i<templateInfoList.size();i++){
                OemDetailResponse.TemplateInfo templateInfo = new OemDetailResponse.TemplateInfo();
                templateInfo.setSignCode(templateInfoList.get(i).getSignCode());
                templateInfo.setTemplateName(templateInfoList.get(i).getTemplateName());
                templateInfos.add(templateInfo);
            }
        }
        return oemDetailResponse;
    }

    /**
     * 配置O单
     *
     * @param addOrUpdateOemRequest
     */
    @Override
    public void addOrUpdate(AddOrUpdateOemRequest addOrUpdateOemRequest) {
        OemInfo oemInfo = oemInfoDao.selectByDealerId(addOrUpdateOemRequest.getDealerId());
        if(oemInfo==null){//新增
            OemInfo oi = new OemInfo();
            oi.setDealerId(addOrUpdateOemRequest.getDealerId());
            oi.setBrandName(addOrUpdateOemRequest.getBrandName());
            oi.setWechatCode(addOrUpdateOemRequest.getWechatCode());
            oi.setWechatName(addOrUpdateOemRequest.getWechatName());
            oi.setAppId(addOrUpdateOemRequest.getAppId());
            oi.setAppSecret(addOrUpdateOemRequest.getAppSecret());
            oi.setQrCode(addOrUpdateOemRequest.getQrCode());
            oi.setLogo(addOrUpdateOemRequest.getLogo());
            oi.setStatus(EnumDealerStatus.NORMAL.getId());
            this.insert(oi);
            for(int i=0;i<addOrUpdateOemRequest.getTemplateInfos().size();i++){

            }
        }else{//修改

        }
    }
}
