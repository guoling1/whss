package com.jkm.hss.notifier.service.impl;

import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.notifier.dao.VerificationCodeDao;
import com.jkm.hss.notifier.entity.VerificationCode;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SmsConsts;
import com.jkm.hss.notifier.service.SmsAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangwei on 6/11/15.
 */
@Slf4j
@Service
public class SmsAuthServiceImpl implements SmsAuthService {

    @Autowired
    private VerificationCodeDao verificationCodeDao;

    /**
     * {@inheritDoc}
     *
     * @param mobile
     * @param code
     * @param verificationCodeType
     * @return {@inheritDoc}
     */
    @Override
    public Pair<Integer, String> checkVerifyCode(final String mobile, final String code, final EnumVerificationCodeType verificationCodeType) {
        final VerificationCode vCode = this.verificationCodeDao.selectLast(mobile, verificationCodeType.getId());
        if (vCode == null) {
            return Pair.of(-1, "未发送验证码");
        }
        if (vCode.getStatus() == 0 || vCode.getRetryCount() >= SmsConsts.MAX_VERIFY_RETRY_COUNT) {
            return Pair.of(-2, "验证码无效");
        }
        if (new DateTime(vCode.getCreateTime()).plusMinutes(SmsConsts.VERIFY_VALID_PERIOD).isBeforeNow()) {
            return Pair.of(-3, "验证码已过期");
        }

        if (!vCode.getCode().equalsIgnoreCase(code)) {
            this.verificationCodeDao.increaseRetryCount(vCode.getId(), SmsConsts.MAX_VERIFY_RETRY_COUNT);
            return Pair.of(-4, "验证码错误");
        }

        final int result = this.verificationCodeDao.updateStatus(vCode.getId(), 0);
        return result > 0 ? Pair.of(1, "验证码正确") : Pair.of(result, "验证码无效");
    }

    /**
     * {@inheritDoc}
     *
     * @param mobile
     * @param verificationCodeType
     * @return
     */
    @Override
    public Pair<Integer, String> getVerifyCode(final String mobile, final EnumVerificationCodeType verificationCodeType) {
        if (!ValidateUtils.isMobile(mobile)) {
            return Pair.of(-1, "手机号错误");
        }

        if (isOverDailySendLimit(mobile, verificationCodeType)) {
            return Pair.of(-2, "超过每天发送数");
        }

        final VerificationCode lastCode = this.verificationCodeDao.selectLast(mobile, verificationCodeType.getId());
        if (log.isDebugEnabled() && lastCode != null) {
            log.debug("最后一个验证码{}", lastCode);
        }
        if (isCodeSendTooOften(lastCode)) {
            return Pair.of(-3, "验证码发送太频繁");
        }

        final String code = getCode(lastCode);

        final VerificationCode verificationCode = new VerificationCode();
        verificationCode.setMobile(mobile);
        verificationCode.setType(verificationCodeType.getId());
        verificationCode.setCode(code);
        verificationCode.setStatus(1);

        final int insertResult = this.verificationCodeDao.insert(verificationCode);
        if (insertResult == 1) {
            return Pair.of(1, code);
        }

        return Pair.of(-4, "获取验证码错误");
    }

    private boolean isOverDailySendLimit(final String mobile,
                                         final EnumVerificationCodeType verificationCodeType) {
        return this.verificationCodeDao.selectTodaySendCount(mobile, verificationCodeType.getId()) >=
                verificationCodeType.getDailyCount();
    }

    private boolean isCodeSendTooOften(final VerificationCode lastCode) {
        return lastCode != null
                && isCreateTimeInMinSendInterval(lastCode);
    }

    private String getCode(final VerificationCode lastCode) {
        String code = null;
        //5分钟内且重试小于 MAX_VERIFY_RETRY_COUNT ，验证码不变
        if (isCodeReUseAble(lastCode)) {
            code = lastCode.getCode();
        } else {
            code = RandomStringUtils.randomNumeric(6);
        }
        return code;
    }

    private boolean isCodeReUseAble(final VerificationCode lastCode) {
        return lastCode != null && lastCode.getStatus() == 1
                && isCreateTimeInFiveMinutes(lastCode)
                && lastCode.getRetryCount() == 0;
    }

    private boolean isCreateTimeInFiveMinutes(final VerificationCode lastCode) {
        return new DateTime(lastCode.getCreateTime()).plusMinutes(5).isAfterNow();
    }

    private boolean isCreateTimeInMinSendInterval(final VerificationCode lastCode) {
        return new DateTime(lastCode.getCreateTime())
                .plusSeconds(SmsConsts.VERIFY_SEND_INTERVAL).isAfterNow();
    }
}
