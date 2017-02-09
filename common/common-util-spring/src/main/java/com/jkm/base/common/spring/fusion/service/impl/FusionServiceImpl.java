package com.jkm.base.common.spring.fusion.service.impl;

import com.jkm.base.common.spring.fusion.helper.fusion.*;
import com.jkm.base.common.spring.fusion.helper.fusion.body.RequestBody100004;
import com.jkm.base.common.spring.fusion.helper.fusion.body.RequestBody200004;
import com.jkm.base.common.spring.fusion.helper.fusion.detail.RequestDetail100004;
import com.jkm.base.common.spring.fusion.helper.fusion.detail.RequestDetail200004;
import com.jkm.base.common.spring.fusion.helper.fusion.head.RequestHead;
import com.jkm.base.common.spring.fusion.helper.fusion.head.RequestHead2;
import com.jkm.base.common.spring.fusion.service.FusionService;
import com.jkm.base.common.spring.fusion.util.*;
import com.jkm.base.common.util.SnGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xingliujie on 2017/2/7.
 */
@Slf4j
@Service
public class FusionServiceImpl implements FusionService {
    /**
     * 通过原始报文和商户号对报文进行验签
     *
     * @param orgMsg 系统请求报文
     * @return
     */
    @Override
    public boolean isSignature(String orgMsg) {
        if (StringUtils.isEmpty(orgMsg)) {
            return false;
        }
        StringBuffer orgBuf = new StringBuffer();
        String startSign = "<SIGNED_MSG>";
        String endSign = "</SIGNED_MSG>";
        int startIndex = orgMsg.indexOf(startSign);
        if(startIndex <=0 ){
            return false;
        }
        int endIndex = orgMsg.indexOf(endSign);
        if(endIndex <=0 ){
            return false;
        }
        orgBuf.append(orgMsg.substring(0, startIndex)).append(startSign).append(endSign)
                .append(orgMsg.substring(endIndex + endSign.length()));
        String signStr = orgMsg.substring(startIndex + startSign.length(), endIndex);
        Map<String, String> signRet = UlpayRaTools.getInstance().verify(signStr, orgBuf.toString());
        log.info("验签结果==>{}",signRet);
        if (!UlpayRaTools.SUCCESS.equals(signRet.get(UlpayRaTools.RET_CODE))) {
            log.error("验签失败 ==>{}",signRet.get(UlpayRaTools.RET_MSG));
            throw new IllegalArgumentException(signRet.get(UlpayRaTools.RET_MSG));
        }
        return true;
    }

    /**
     * 响应报文加签处理
     *
     * @param orgMsg
     * @return
     */
    @Override
    public String addSignatrue(String orgMsg) {
        String signLabel = "<SIGNED_MSG>signedMsg</SIGNED_MSG>";
        String signLabelEmpty = "<SIGNED_MSG></SIGNED_MSG>";
        orgMsg = orgMsg.replace(signLabel, signLabelEmpty);
        StringBuffer signBuffer = new StringBuffer("<SIGNED_MSG>").append(UlpayRaTools.getInstance().sign(orgMsg))
                .append("</SIGNED_MSG>");
        return orgMsg.replace(signLabelEmpty, signBuffer.toString());
    }

    /**
     * 银行卡鉴权
     *
     * @param cardAuthData
     * @return
     */
    @Override
    public Map<String, Object> cardAuth(CardAuthData cardAuthData) throws Exception{
        Map<String, Object> ret = new HashMap<String, Object>();
        Request100004 request100004 = this.createCardAuth(cardAuthData);
        String xml = XmlUtil.toXML(request100004);
        log.info("****************xml生成authen*********************-"
                + xml);
        // 加签
        log.info("****************xml加签*********************");
        xml = this.addSignatrue(xml);
        // 加压加密
        log.info("****************xml加压加密*********************");
        String Base64 = GZipUtil.gzipString(xml);
        // 通讯使用HTTPS进行通讯
        log.info("****************xml通讯使用HTTPS进行通讯*********************");
        String response1 = HttpUtils.sendPostMessage(Base64, Constants.CARD_AUTH,
                    Constants.transfer_charset);
        // 解压解密返回信息
        String response2 = null;
        if (!StringUtils.isEmpty(response1)) {
            log.info("****************xml通讯返回*********************");
            response2 = GZipUtil.ungzipString(response1);
            boolean isSuc = this.isSignature(response2);
            if (!isSuc) {
                log.info("验签失败！！");
                ret.put("msg", "验签失败");
                ret.put("code",-1);
                return ret;
            }
            log.info("验签成功！！");
            log.info("{}",response2);
            Response100004 response100004 = XmlUtil.fromXML(response2,
                    Response100004.class);
            ret.put("msg", "鉴权成功");
            ret.put("code",0);
            return ret;
        }else{
            log.info("鉴权服务器返回异常！！");
            ret.put("msg", "鉴权服务器返回异常");
            ret.put("code",-1);
            return ret;
        }
    }

    /**
     * 银行卡鉴权
     *
     * @param queryCardAuthData
     * @return
     */
    @Override
    public Map<String, Object> queryCardAuth(QueryCardAuthData queryCardAuthData) throws Exception {
        Map<String, Object> ret = new HashMap<String, Object>();
        Request200004 request200004 = this.createQueryCardAuth(queryCardAuthData);
        String xml = XmlUtil.toXML(request200004);
        log.info("****************xml生成authen*********************-"
                + xml);
        // 加签
        log.info("****************xml加签*********************");
        xml = this.addSignatrue(xml);
        // 加压加密
        log.info("****************xml加压加密*********************");
        String Base64 = GZipUtil.gzipString(xml);
        // 通讯使用HTTPS进行通讯
        log.info("****************xml通讯使用HTTPS进行通讯*********************");
        String response1 = HttpUtils.sendPostMessage(Base64, Constants.QUERY_CARD_AUTH,
                Constants.transfer_charset);
        // 解压解密返回信息
        String response2 = null;
        if (!StringUtils.isEmpty(response1)) {
            log.info("****************xml通讯返回*********************");
            response2 = GZipUtil.ungzipString(response1);
            boolean isSuc = this.isSignature(response2);
            if (isSuc) {
                ret.put("signMsg", "验签成功！！");
                log.info("验签成功！！");
            } else {
                log.info("验签失败！！");
                ret.put("code",-1);
                ret.put("msg","验签失败！！");
                return ret;
            }
            log.info("{}",response2);
            Response200004 response200004 = XmlUtil.fromXML(response2,
                    Response200004.class);
            if("0000".equals(response200004.getInfo().getRetCode())&&"S".equals(response200004.getBody().getTransDetail().getRET_COD())){
                ret.put("code",0);
                ret.put("msg","扣费成功");
                return ret;
            }else{
                ret.put("code",-1);
                ret.put("msg","扣费失败");
                return ret;
            }
        }else{
            log.info("鉴权服务器查询异常");
            ret.put("code",-1);
            ret.put("msg","鉴权服务器查询异常");
            return ret;
        }
    }

    private Request100004 createCardAuth(CardAuthData requestData) {
        Request100004 authen = new Request100004();
        RequestHead head = new RequestHead();
        head.setTrxCode("100004");//固定
        head.setVersion("01");//固定
        head.setDataType(Constants.DATA_TYPE_XML);//固定
        head.setLevel(Constants.LEVEL_0);//固定
        head.setReqSn(requestData.getReqSn());
        head.setSignedMsg("signedMsg");
        RequestBody100004 body = new RequestBody100004();
        RequestDetail100004 detail = new RequestDetail100004();
        detail.setMERCHANT_ID(Constants.MERC_ID);
        detail.setSEND_TIME(DateUtils.getDateString(new Date(),
                DateUtils.formate_string_hhmmss));
        detail.setSEND_DT(DateUtils.getDateString(new Date(),
                DateUtils.formate_string_yyyyMMdd));
        detail.setCARD_NO(requestData.getCrdNo());
        detail.setACCOUNT_NAME(requestData.getAccountName());
        detail.setID_TYPE("00");//只支持身份证
        detail.setID(requestData.getIdNo());//证件号
        detail.setTEL(requestData.getPhoneNo());//手机号
        body.setTransDetail(detail);
        authen.setBody(body);
        authen.setInfo(head);
        return authen;
    }
    private Request200004 createQueryCardAuth(QueryCardAuthData requestData) {
        Request200004 querycardAuth = new Request200004();
        RequestHead2 head2 = new RequestHead2();
        head2.setTrxCode("200004");//固定
        head2.setVersion("01");//固定
        head2.setDataType(Constants.DATA_TYPE_XML);//固定
        head2.setLevel(Constants.LEVEL_0);//固定
        head2.setReqSn(SnGenerator.generateFusionReqSn());
        head2.setSignedMsg("signedMsg");
        head2.setMerchantId(Constants.MERC_ID);
        RequestBody200004 body = new RequestBody200004();
        RequestDetail200004 detail = new RequestDetail200004();
        detail.setQUERY_SN(requestData.getQuerySn());
        detail.setQUERY_DATE(requestData.getQuerySn().substring(0,8));
        body.setTransDetail(detail);
        querycardAuth.setBody(body);
        querycardAuth.setInfo(head2);
        return querycardAuth;
    }
}
