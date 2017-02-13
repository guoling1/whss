package com.jkm.hss.controller.code;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Thinkpad on 2017/1/18.
 */
@Slf4j
@Controller
@RequestMapping(value = "/sqb")
public class WebSkipController extends BaseController {
    @Autowired
    private OrderService orderService;
    /**
     * 支付升级成功页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/success/{amount}/{orderId}", method = RequestMethod.GET)
    public String buySuccess(final HttpServletRequest request, final HttpServletResponse response, final Model model, @PathVariable("amount") String amount, @PathVariable("orderId") long orderId) throws IOException {
        model.addAttribute("money", amount);
        final Order order = this.orderService.getById(orderId).get();
        model.addAttribute("firstSn", order.getOrderNo().substring(0, order.getOrderNo().length() - 6));
        model.addAttribute("secondSn", order.getOrderNo().substring(order.getOrderNo().length() - 6, order.getOrderNo().length()));
        return "/buySuccess";
    }

    /**
     * 扫固定码微信支付页面
     * @param request
     * @param response
     * @param model
     * @param merchantId
     * @param name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/paymentWx", method = RequestMethod.GET)
    public String paymentWx(final HttpServletRequest request, final HttpServletResponse response, final Model model, @RequestParam(value = "merchantId", required = true) long merchantId, @RequestParam(value = "name") String name) throws IOException {
        model.addAttribute("mid", merchantId);
        model.addAttribute("merchantName", name);
        return "/payment-wx";
    }

    /**
     * 扫固定码支付宝支付页面
     * @param request
     * @param response
     * @param model
     * @param merchantId
     * @param name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/paymentZfb", method = RequestMethod.GET)
    public String paymentZfb(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "merchantId", required = true) long merchantId,@RequestParam(value = "name") String name) throws IOException {
        model.addAttribute("mid", merchantId);
        model.addAttribute("merchantName", name);
        return "/payment-zfb";
    }
}
