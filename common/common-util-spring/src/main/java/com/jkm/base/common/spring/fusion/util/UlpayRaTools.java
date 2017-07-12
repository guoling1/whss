package com.jkm.base.common.spring.fusion.util;

import com.itrus.cryptorole.Recipient;
import com.itrus.cryptorole.Sender;
import com.itrus.cryptorole.bc.RecipientBcImpl;
import com.itrus.cryptorole.bc.SenderBcImpl;
import com.itrus.cvm.CVM;
import com.itrus.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by xingliujie on 2016/10/25.
 */
public class
UlpayRaTools {
    public static final String RET_CODE = "retCode";
    public static final String RET_MSG = "retMsg";
    public static final String CA_SERIAL_NUMBER = "ca_serial_number";
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    private String certPath;
    private String certPass;
    private String cvmPath;
    private static final String ENCRYPT_CHARSET = "UTF-8";
    private static final Logger LOG = LoggerFactory.getLogger(UlpayRaTools.class);

    private static UlpayRaTools tools = null;

    private UlpayRaTools()
    {
        ResourceBundle bundle = ResourceBundle.getBundle("config");

        this.certPath = bundle.getString("EntCertPath");

        this.certPass = bundle.getString("CertPass");

        this.cvmPath = bundle.getString("CvmPath");
    }

    public static final UlpayRaTools getInstance()
    {
        if (null == tools) {
            synchronized (UlpayRaTools.class) {
                if (null == tools) {
                    tools = new UlpayRaTools();
                }
            }
        }
        return tools;
    }

    public String sign(String oriData)
    {
        String signData = "";
        if ((null == this.certPath) || ("".equals(this.certPath))) {
            throw new IllegalArgumentException("EntCertPath IS NOT NULL");
        }
        if ((null == this.certPass) || ("".equals(this.certPass))) {
            throw new IllegalArgumentException("CertPass IS NOT NULL");
        }
        Sender s = new SenderBcImpl();
        try {
            s.initCertWithKey(this.certPath, this.certPass.toCharArray());
            byte[] p7dtach = s.signMessage(oriData.getBytes("UTF-8"));
            signData = Base64.encode(p7dtach);
        } catch (Exception e) {
            LOG.error("数据签名失败：", e);
            return null;
        }
        return signData;
    }

    public Map<String, String> verify(String signData, String oriData)
    {
        if ((this.cvmPath == null) || ("".equals(this.cvmPath))) {
            throw new IllegalArgumentException("CertPass IS NOT NULL");
        }
        Recipient r = new RecipientBcImpl();
        Map res = new HashMap();
        String status = "failed";
        String resInfo = "未知";
        try
        {
            java.security.cert.X509Certificate signer = r.verifySignature(oriData.getBytes("UTF-8"), Base64.decode(signData));

            com.itrus.cert.X509Certificate itrusCert = com.itrus.cert.X509Certificate.getInstance(signer);

            String icaSerialNumber = itrusCert.getICASerialNumber();

            LOG.debug("证书编号为【{}】", icaSerialNumber);
            res.put("ca_serial_number", icaSerialNumber);

            CVM.config(this.cvmPath);

            int ret = CVM.verifyCertificate(itrusCert);

            if (ret != 0) {
                switch (ret) {
                    case -1:
                        resInfo = "CVM初始化错误，请检查配置文件或给CVM增加支持的CA。";
                        break;
                    case 5:
                        resInfo = "CRL不可用，未知状态。";
                        break;
                    case 1:
                        resInfo = "证书已过期。";
                        break;
                    case 4:
                        resInfo = "非法颁发者。";
                        break;
                    case 2:
                        resInfo = "证书已吊销。";
                        break;
                    case 3:
                        resInfo = "不支持的颁发者。请检查cvm.xml配置文件";
                        break;
                    case 6:
                        resInfo = "证书被吊销且已过期。";
                        break;
                    case 0:
                    default:
                        resInfo = "验证证书出现未知错误，请联系证书管理人员。"; break;
                }
            } else {
                status = "success";
                resInfo = "当前签名证书是有效的！";
            }
        } catch (Exception e) {
            LOG.info("验证报文信息失败。", e);
            res.put("failed", "验签异常");
        }
        res.put("retCode", status);
        res.put("retMsg", resInfo);
        LOG.info("验签结果==>{}", res);
        return res;
    }
}
