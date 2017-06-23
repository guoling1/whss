package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.base.common.util.email.BaseEmailInfo;
import com.jkm.base.common.util.email.EmailUtil;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.enums.EnumHsyOrderStatus;
import com.jkm.hss.bill.helper.ApplicationConsts;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.HsyBalanceAccountEmailService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.constant.UploadConsts;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShopUserRole;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yulong.zhang on 2017/6/22.
 */
@Slf4j
@Service
public class HsyBalanceAccountEmailServiceImpl implements HsyBalanceAccountEmailService {

    @Autowired
    private HsyUserService hsyUserService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private HSYOrderService hsyOrderService;
    @Autowired
    private OSSClient ossClient;

    /**
     * {@inheritDoc}
     *
     * @param paramData
     * @param appParam
     * @return
     */
    @Override
    public String updateAutoSendBalanceAccountEmail(String paramData, AppParam appParam) throws ApiHandleException {
        final JSONObject paramJo = JSONObject.parseObject(paramData);
        final long userId = paramJo.getLongValue("userId");
        //1:启用，2禁用
        final int autoSend = paramJo.getIntValue("autoSend");
        if (1 == autoSend) {
            this.hsyUserService.enableAutoSendBalanceAccountEmail(userId);
            return "success";
        } else if (2 == autoSend) {
            this.hsyUserService.disableAutoSendBalanceAccountEmail(userId);
            return "success";
        }
        throw new ApiHandleException(ResultCode.PARAM_EXCEPTION, "autoSend值错误");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendWeekBalanceAccountEmail() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
        calendar.add(Calendar.DATE, -1*7);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        final Calendar calendar2 = Calendar.getInstance();
        calendar2.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
        calendar2.add(Calendar.DATE, -1*7);
        calendar2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        final Date startTime = DateFormatUtil.parse(DateFormatUtil.format(calendar.getTime(), DateFormatUtil.yyyy_MM_dd) + " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final Date endTime = DateFormatUtil.parse(DateFormatUtil.format(calendar2.getTime(), DateFormatUtil.yyyy_MM_dd) + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final List<AppAuUser> appAuUsers = this.hsyUserDao.selectAllCorporationUser();
        for (AppAuUser appAuUser : appAuUsers) {
            this.simpleSend(appAuUser.getGlobalID(), startTime, endTime, appAuUser.getEmail(), appAuUser.getRealname(), "");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMonthBalanceAccountEmail() {
        final Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        final Date startTime = DateFormatUtil.parse(DateFormatUtil.format(calendar.getTime(), DateFormatUtil.yyyy_MM_dd) + " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);

        final Calendar calendar2 = Calendar.getInstance();//获取当前日期
        calendar2.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        final Date endTime = DateFormatUtil.parse(DateFormatUtil.format(calendar2.getTime(), DateFormatUtil.yyyy_MM_dd) + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final List<AppAuUser> appAuUsers = this.hsyUserDao.selectAllCorporationUser();
        for (AppAuUser appAuUser : appAuUsers) {
            this.simpleSend(appAuUser.getGlobalID(), startTime, endTime, appAuUser.getEmail(), appAuUser.getRealname(), "");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param paramData
     * @param appParam
     * @return
     */
    @Override
    public String sendBalanceAccountEmail(final String paramData, final AppParam appParam) {
        final JSONObject paramJo = JSONObject.parseObject(paramData);
        final long shopId = paramJo.getLongValue("shopId");
        final String email = paramJo.getString("email");
        final String startTimeStr = paramJo.getString("startTime");
        final String endTimeStr = paramJo.getString("endTime");
        Date startTime = null;
        Date endTime = null;
        if (!StringUtils.isEmpty(startTimeStr)) {
            startTime = DateFormatUtil.parse(startTimeStr, DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        if (!StringUtils.isEmpty(endTimeStr)) {
            endTime = DateFormatUtil.parse(endTimeStr, DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        final AppBizShopUserRole userRole = this.hsyShopDao.findsurByRoleTypeSid(shopId).get(0);
        final AppAuUser appAuUser = this.hsyUserDao.findAppAuUserByID(userRole.getUid()).get(0);
        this.hsyUserService.updateEmailById(email, appAuUser.getId());
        final String merchantNo = appAuUser.getGlobalID();
        this.simpleSend(merchantNo, startTime, endTime, appAuUser.getEmail(), appAuUser.getRealname(), "");
        return "success";
    }

    private void simpleSend(final String merchantNo, final Date startTime, final Date endTime, final String email, final String userName, final String settleDate) {
        final List<HsyOrder> hsyOrders = this.hsyOrderService.getByMerchantNoAndTime(merchantNo, startTime, endTime);
        final ExcelSheetVO excelSheetVO = this.generateExcelSheet(hsyOrders, merchantNo);
        final String fileUrl = this.uploadToServer(excelSheetVO);
        Preconditions.checkState(StringUtils.isEmpty(fileUrl), "上传文件到oss异常");
        final BaseEmailInfo baseEmailInfo = new BaseEmailInfo();
        baseEmailInfo.setServerHost(ApplicationConsts.getApplicationConfig().emailServerHost());
        baseEmailInfo.setServerPort(ApplicationConsts.getApplicationConfig().emailServerPort());
        baseEmailInfo.setValidate(true);
        baseEmailInfo.setUsername(ApplicationConsts.getApplicationConfig().emailUserName());
        baseEmailInfo.setPassword(ApplicationConsts.getApplicationConfig().emailPassword());
        baseEmailInfo.setFromAddress(ApplicationConsts.getApplicationConfig().emailFromAddress());
        baseEmailInfo.setToAddress(email);
        baseEmailInfo.setSubject("钱包++ 对账单");
        final String startDate = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).format(startTime);
        final String endDate = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).format(startTime);
        String tradeDate;
        if (startDate.equals(endDate)) {
            tradeDate = startDate;
        } else {
            tradeDate = startDate + "-" + endDate;
        }
        this.sendEmail(baseEmailInfo, userName, "",
                merchantNo, tradeDate, ApplicationConsts.getApplicationConfig().ossFilePath() + fileUrl);
    }

    private void sendEmail(final BaseEmailInfo baseEmailInfo, final String userName, final String settleDate,
                           final String merchantNo, final String tradeDate, final String url) {
        final StringBuffer emailStr = new StringBuffer();
        emailStr.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
                .append("<html xmlns=\"http://www.w3.org/1999/xhtml\">")
                .append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />")
                .append("<title>钱包++ 对账单</title>")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>")
                .append("</head>")
                .append("<body style=\"margin: 0;padding: 0;\">")
                .append("<div style=\"width: 640px;height: auto;margin: 0 auto;padding: 20px;background-color:#ececec;box-sizing: border-box;\">")
                .append("<div style=\"box-sizing: border-box;\"><img src=\"http://static.jinkaimen.cn/logo.png\" alt=\"\"></div>")
                .append("<div style=\"width: 600px;height: auto;margin: 20px auto 0;background-color:#fff;box-sizing: border-box;padding: 0 15px 10px;\">")
                .append("<div style=\"font-size:20px;color:#222222;line-height:60px;\">尊敬的商户：</div>")
                .append("<div style=\"font-size:20px;color:#222222;\">" + userName + "</div>")
                .append("<br><br>")
                .append("<div style=\"width:560px;height:1px;background: -webkit-linear-gradient(left, white , #33a688, white);background: -o-linear-gradient(right, white, #33a688, white);background: -moz-linear-gradient(right, white, #33a688, white);background: linear-gradient(to right, white , #33a688, white);\"></div>")
                .append("<br><br>")
                .append("<div style=\"font-size:18px;color:#222222;line-height:30px;\">您好，</div>")
                .append("<div style=\"font-size:18px;color:#222222;line-height:30px;\">感谢您使用钱包加加，以下是您" + settleDate + "结算的交易对账单明细</div>")
                .append("<br>")
                .append("<div style=\"font-size:18px;color:#222222;font-weight:bold;line-height:30px;\">商户号：" + merchantNo + "</div>")
                .append("<div style=\"font-size:18px;color:#222222;font-weight:bold;line-height:30px;\">结算交易周期：" + tradeDate + "</div>")
                .append("<div style=\"font-size:18px;color:#222222;line-height:30px;\">具体交易明细请点击以下加密链接下载：</div>")
                .append("<br><br><br>")
                .append("<a href=\" " + url + "\" style=\"display:block;width:230px;height:55px;line-height:55px;margin: 0 auto;border-radius:5px;background-color:#33a688;font-size:20px;color:#ececec;text-decoration:none;text-align: center;\">下载对账文件</a>")
                .append("<br><br><br>")
                .append("<div style=\"font-size:18px;color:#777777;line-height:30px;\">*为了保证交易信息安全，切勿将邮件转发给其他人。</div>")
                .append("<div style=\"font-size:18px;color:#222222;line-height:30px;\">再次感谢您的支持，如有任何疑问，欢迎与我们联系，客服电话</div>")
                .append("<div style=\"font-size:18px;color:#222222;line-height:30px;\"> 400-622-6233 。</div>")
                .append("<br><br>")
                .append("<div style=\"width:560px;height:1px;background: -webkit-linear-gradient(left, white , #b1b1b1, white);background: -o-linear-gradient(right, white, #b1b1b1, white);background: -moz-linear-gradient(right, white, #b1b1b1, white);background: linear-gradient(to right, white , #b1b1b1, white);\"></div>")
                .append("<br>")
                .append("<div style=\"font-size:16px;color:#767676;line-height:30px;text-align:center;\">为了确保收到此邮件，请将CSC@jinkaimen.com添加为您的联系人。</div>")
                .append("</div></div>")
                .append("</body></html>");
        baseEmailInfo.setContent(emailStr.toString());
        EmailUtil.sendEmail(baseEmailInfo);
    }

    /**
     * 生成Excel
     *
     * @param hsyOrders
     * @param merchantNo
     * @return
     */
    private ExcelSheetVO generateExcelSheet(final List<HsyOrder> hsyOrders, final String merchantNo) {
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final ArrayList<List<String>> datas = new ArrayList<>();
        excelSheetVO.setDatas(datas);
        excelSheetVO.setName(merchantNo + "_statement_" + DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMdd));
        final List<String> head = Arrays.asList(new String[]{"订单创建时间", "交易单号", "店铺名称", "店铺二维码", "支付方式", "成功时间", "订单金额", "手续费金额", "已退金额", "订单状态"});
        datas.add(head);
        for (HsyOrder hsyOrder : hsyOrders) {
            final List<String> columns = new ArrayList<>();
            datas.add(columns);
            columns.add(DateFormatUtil.format(hsyOrder.getCreateTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
            columns.add(hsyOrder.getOrderno());
            columns.add(hsyOrder.getShopname());
            columns.add(hsyOrder.getQrcode());
            columns.add(EnumPayChannelSign.idOf(hsyOrder.getPaychannelsign()).getPaymentChannel().getValue());
            columns.add(DateFormatUtil.format(hsyOrder.getPaysuccesstime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
            columns.add(hsyOrder.getAmount().toPlainString());
            columns.add(hsyOrder.getPoundage().toPlainString());
            columns.add(hsyOrder.getRefundamount().toPlainString());
            columns.add(EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getValue());
        }
        return excelSheetVO;
    }

    /**
     * 上传到服务器
     *
     * @param excelSheetVO
     */
    private String uploadToServer(final ExcelSheetVO excelSheetVO) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + excelSheetVO.getName() + ".xls");
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheetVO);
        FileOutputStream fileOutputStream = null;
        String fileName = "hsycheckorder" + File.separator +  DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMdd) + File.separator + ".xls";
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            final ObjectMetadata meta = new ObjectMetadata();
            meta.setCacheControl("public, max-age=31536000");
            meta.setExpirationTime(new DateTime().plusYears(1).toDate());
            meta.setContentType("application/octet-stream ");
            this.ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBindHost(), fileName, new FileInputStream(excelFile), meta);
            return fileName;
        }catch (Exception e){
            log.debug("上传文件失败",e);
        } finally {
            FileUtils.deleteQuietly(excelFile);
        }
        return "";
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    public String getTempDir() {
        final String dir = System.getProperty("java.io.tmpdir") + "hsy" + File.separator + "order";
        final File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

}
