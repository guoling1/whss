package com.jkm.hss.controller.code;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;
import com.jkm.base.common.spring.alipay.dispatcher.Dispatcher;
import com.jkm.base.common.spring.alipay.executor.ActionExecutor;
import com.jkm.base.common.spring.alipay.util.LogUtil;
import com.jkm.base.common.spring.alipay.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by xingliujie on 2017/4/21.
 */
@Slf4j
@Controller
@RequestMapping(value = "/fuwuchuang")
public class GatewayController extends HttpServlet {

    @ResponseBody
    @RequestMapping(value = "gateway", method = RequestMethod.GET)
    public void gateway(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //支付宝响应消息
        String responseMsg = "";

        //1. 解析请求参数
        Map<String, String> params = RequestUtil.getRequestParams(request);

        //打印本次请求日志，开发者自行决定是否需要
        LogUtil.log("支付宝请求串", params.toString());

        try {
            //2. 验证签名
            this.verifySign(params);

            //3. 获取业务执行器   根据请求中的 service, msgType, eventType, actionParam 确定执行器
            ActionExecutor executor = Dispatcher.getExecutor(params);

            //4. 执行业务逻辑
            responseMsg = executor.execute();

        }
        catch (AlipayApiException alipayApiException) {
            //开发者可以根据异常自行进行处理
            alipayApiException.printStackTrace();

        }
        catch (Exception exception) {
            //开发者可以根据异常自行进行处理
            exception.printStackTrace();

        } finally {
            //5. 响应结果加签及返回
            try {
                //对响应内容加签
                responseMsg = encryptAndSign(responseMsg,
                        AlipayServiceConstants.ALIPAY_PUBLIC_KEY,
                        AlipayServiceConstants.PRIVATE_KEY, AlipayServiceConstants.CHARSET,
                        false, true, AlipayServiceConstants.SIGN_TYPE);

                //http 内容应答
                response.reset();
                response.setContentType("text/xml;charset=GBK");
                PrintWriter printWriter = response.getWriter();
                printWriter.print(responseMsg);
                response.flushBuffer();

                //开发者自行决定是否要记录，视自己需求
                LogUtil.log("开发者响应串", responseMsg);

            } catch (AlipayApiException alipayApiException) {
                //开发者可以根据异常自行进行处理
                alipayApiException.printStackTrace();
            }
        }
    }

    /**
     * 验签
     *
     * @return
     */
    private void verifySign(Map<String, String> params) throws AlipayApiException {

        if (!AlipaySignature.rsaCheckV2(params, AlipayServiceConstants.ALIPAY_PUBLIC_KEY,
                AlipayServiceConstants.SIGN_CHARSET, AlipayServiceConstants.SIGN_TYPE)) {
            throw new AlipayApiException("verify sign fail.");
        }
    }


    public static String encryptAndSign(String bizContent, String alipayPublicKey, String cusPrivateKey, String charset,
                                        boolean isEncrypt, boolean isSign, String signType) throws AlipayApiException {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(charset)) {
            charset = AlipayConstants.CHARSET_GBK;
        }
        sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        if (isEncrypt) {// 加密
            sb.append("<alipay>");
            String encrypted = AlipaySignature.rsaEncrypt(bizContent, alipayPublicKey, charset);
            sb.append("<response>" + encrypted + "</response>");
            sb.append("<encryption_type>AES</encryption_type>");
            if (isSign) {
                String sign = AlipaySignature.rsaSign(encrypted, cusPrivateKey, charset, signType);
                sb.append("<sign>" + sign + "</sign>");
                sb.append("<sign_type>");
                sb.append(signType);
                sb.append("</sign_type>");
            }
            sb.append("</alipay>");
        } else if (isSign) {// 不加密，但需要签名
            sb.append("<alipay>");
            sb.append("<response>" + bizContent + "</response>");
            String sign = AlipaySignature.rsaSign(bizContent, cusPrivateKey, charset, signType);
            sb.append("<sign>" + sign + "</sign>");
            sb.append("<sign_type>");
            sb.append(signType);
            sb.append("</sign_type>");
            sb.append("</alipay>");
        } else {// 不加密，不加签
            sb.append(bizContent);
        }
        return sb.toString();
    }
}
