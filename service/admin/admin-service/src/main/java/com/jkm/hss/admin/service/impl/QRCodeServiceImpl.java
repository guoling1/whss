package com.jkm.hss.admin.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.base.common.util.QRCodeUtil;
import com.jkm.hss.admin.dao.QRCodeDao;
import com.jkm.hss.admin.entity.CodeQueryResponse;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeActivateStatus;
import com.jkm.hss.admin.enums.EnumQRCodeDistributionStatus;
import com.jkm.hss.admin.enums.EnumQRCodeType;
import com.jkm.hss.admin.helper.FirstLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.MyMerchantCount;
import com.jkm.hss.admin.helper.QRCodeConsts;
import com.jkm.hss.admin.helper.SecondLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.responseparam.ActiveCodeCount;
import com.jkm.hss.admin.helper.responseparam.DistributeCodeCount;
import com.jkm.hss.admin.helper.responseparam.QRCodeList;
import com.jkm.hss.admin.service.QRCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
@Slf4j
@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Autowired
    private QRCodeDao qrCodeDao;

    /**
     * {@inheritDoc}
     *
     * @param qrCode
     */
    @Override
    @Transactional
    public void add(final QRCode qrCode) {
        this.qrCodeDao.insert(qrCode);
    }

    /**
     * {@inheritDoc}
     *
     * @param qrCode
     * @return
     */
    @Override
    @Transactional
    public int updateById(final QRCode qrCode) {
        return this.qrCodeDao.updateById(qrCode);
    }

    /**
     * {@inheritDoc}
     *
     * @param qrCode
     * @return
     */
    @Override
    @Transactional
    public int updateByCode(final QRCode qrCode) {
        return this.qrCodeDao.updateByCode(qrCode);
    }

    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerId
     * @param codeIds
     * @return
     */
    @Override
    public int markCodeToDealer(final long firstLevelDealerId, final List<Long> codeIds) {
        Preconditions.checkState(!CollectionUtils.isEmpty(codeIds), "codeIds can not be empty");
        return this.qrCodeDao.markCodeToDealer(firstLevelDealerId, codeIds);
    }

    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerId
     * @param startCode
     * @param endCode
     * @return
     */
    @Override
    public int markCodeToDealer(final long firstLevelDealerId, final String startCode, final String endCode) {
        return this.qrCodeDao.markCodeToDealerByRange(firstLevelDealerId, startCode, endCode);
    }

    /**
     * {@inheritDoc}
     *
     * @param code
     * @param merchantId
     * @return
     */
    @Override
    public int markAsActivate(final String code, final long merchantId) {
        return this.qrCodeDao.markAsActivate(code, merchantId);
    }

    /**
     * {@inheritDoc}
     *
     * @param codeIds
     */
    @Override
    @Transactional
    public int markAsDistribute(final List<Long> codeIds) {
        if (CollectionUtils.isEmpty(codeIds)) {
            return 0;
        }
        return this.qrCodeDao.markAsDistribute(codeIds);
    }

    /**
     * {@inheritDoc}
     *
     * @param codeIds
     * @param toDealerId
     */
    @Override
    @Transactional
    public int markAsDistribute2(final List<Long> codeIds, final long toDealerId) {
        return this.qrCodeDao.markAsDistribute2(codeIds, toDealerId);
    }


    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @return
     */
    @Override
    @Transactional
    public QRCode initMerchantCode(final long merchantId,final long productId,final String sysType) {
        final Optional<QRCode> latestQRCode = this.getLatestQRCodeForUpdate();
        String startCode;
        if (latestQRCode.isPresent()) {
            startCode = (Long.valueOf(latestQRCode.get().getCode()) + 1) + "";
        } else {
            startCode = QRCodeConsts.start_code_num + 1;
        }
        final QRCode qrCode = new QRCode();
        qrCode.setCode(startCode);
        qrCode.setAdminId(0);
        qrCode.setFirstLevelDealerId(0);
        qrCode.setSecondLevelDealerId(0);
        qrCode.setMerchantId(merchantId);
        qrCode.setSalt(RandomStringUtils.randomAlphanumeric(16));
        qrCode.setSign(qrCode.getSignCode());
        qrCode.setDistributeStatus(EnumQRCodeDistributionStatus.DISTRIBUTION.getCode());
        qrCode.setActivateStatus(EnumQRCodeActivateStatus.ACTIVATE.getCode());
        qrCode.setType(EnumQRCodeType.PUBLIC.getCode());
        qrCode.setProductId(productId);
        qrCode.setSysType(sysType);
        this.add(qrCode);
        return qrCode;
    }


    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<QRCode> getById(final long id) {
        return Optional.fromNullable(this.qrCodeDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     * @return
     */
    @Override
    public List<QRCode> getByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return  Collections.emptyList();
        }
        return this.qrCodeDao.selectByIds(ids);
    }


    /**
     * {@inheritDoc}
     *
     * @param code
     * @return
     */
    @Override
    public Optional<QRCode> getByCode(final String code) {
        return Optional.fromNullable(this.qrCodeDao.selectByCode(code));
    }

    /**
     * {@inheritDoc}
     *
     * @param code
     * @return
     */
    @Override
    public long getDealerIdByCode(final String code) {
        final Optional<QRCode> codeOptional = this.getByCode(code);
        Preconditions.checkState(codeOptional.isPresent(), "code[{}]对应的二维码不存在", code);
        final QRCode qrCode = codeOptional.get();
        if (qrCode.getSecondLevelDealerId() > 0) {
            return qrCode.getSecondLevelDealerId();
        } else if (qrCode.getFirstLevelDealerId() > 0) {
            return qrCode.getFirstLevelDealerId();
        }
        return 0;
    }

    /**
     * 按code查询当前代理商id，一级代理商id,二级代理商id
     *
     * @param code
     * @return
     */
    @Override
    public Triple<Long, Long, Long> getCurrentAndFirstAndSecondByCode(String code) {
        final Optional<QRCode> codeOptional = this.getByCode(code);
        Preconditions.checkState(codeOptional.isPresent(), "code[{}]对应的二维码不存在", code);
        final QRCode qrCode = codeOptional.get();
        Long currentDealerId = 0l;
        Long firstDealerId = qrCode.getFirstLevelDealerId();
        Long secondDealerId = qrCode.getSecondLevelDealerId();
        if (qrCode.getSecondLevelDealerId() > 0) {
            currentDealerId = qrCode.getSecondLevelDealerId();
            return Triple.of(currentDealerId,firstDealerId,secondDealerId);
        } else if (qrCode.getFirstLevelDealerId() > 0) {
            currentDealerId = qrCode.getFirstLevelDealerId();
            return Triple.of(currentDealerId,firstDealerId,secondDealerId);
        }
        return Triple.of(currentDealerId,firstDealerId,secondDealerId);
    }


    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @return
     */
//    @Override
//    public Optional<QRCode> getByMerchantId(final long merchantId) {
//        return Optional.fromNullable(this.qrCodeDao.selectByMerchantId(merchantId));
//    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     * @return
     */
    @Override
    public List<QRCode> getByMerchantIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return this.qrCodeDao.selectByMerchantIds(ids);
    }

    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerId
     * @param status
     * @return
     */
    @Override
    public List<QRCode> getFirstLevelDealerCodeByDealerId(final long firstLevelDealerId, final int status) {
        return this.qrCodeDao.selectFirstLevelDealerCodeByDealerId(firstLevelDealerId, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param secondLevelDealerId
     * @param status
     * @return
     */
    @Override
    public List<QRCode> getSecondLevelDealerCodeByDealerId(final long secondLevelDealerId, final int status) {
        return this.qrCodeDao.selectSecondLevelDealerCodeByDealerId(secondLevelDealerId, status);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public Optional<QRCode> getLatestQRCodeForUpdate() {
        return Optional.fromNullable(this.qrCodeDao.selectLatestQRCodeForUpdate());
    }


    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public int getUnDistributeCodeCountByFirstLevelDealerId(final long dealerId) {
        return this.qrCodeDao.selectUnDistributeCodeCountByFirstLevelDealerId(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public List<QRCode> getUnDistributeCodeByFirstLevelDealerId(long dealerId) {
        return this.qrCodeDao.selectUnDistributeCodeByFirstLevelDealerId(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param startCode
     * @param endCode
     * @param dealerId
     * @return
     */
    @Override
    public int getUnDistributeCodeCountByDealerIdAndRangeCode(final String startCode,
                                                              final String endCode, final long dealerId) {
        return this.qrCodeDao.selectUnDistributeCodeCountByDealerIdAndRangeCode(startCode, endCode, dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param startCode
     * @param endCode
     * @return
     */
    @Override
    public List<QRCode> getUnDistributeCodeByDealerIdAndRangeCode(long dealerId, String startCode, String endCode) {
        return this.qrCodeDao.selectUnDistributeCodeByDealerIdAndRangeCode(dealerId, startCode, endCode);
    }


    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param secondLevelDealerIds
     * @return
     */
    @Override
    public List<DistributeCodeCount> getDistributeCodeCount(final long dealerId, final List<Long> secondLevelDealerIds) {
        if (CollectionUtils.isEmpty(secondLevelDealerIds)) {
            return Collections.emptyList();
        }
        return this.qrCodeDao.selectDistributeCodeCount(dealerId, secondLevelDealerIds);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param secondLevelDealerIds
     * @return
     */
    @Override
    public List<ActiveCodeCount> getActiveCodeCount(final long dealerId, final List<Long> secondLevelDealerIds) {
        if (CollectionUtils.isEmpty(secondLevelDealerIds)) {
            return Collections.emptyList();
        }
        return this.qrCodeDao.selectActiveCodeCount(dealerId, secondLevelDealerIds);
    }

    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerId
     * @return
     */
    @Override
    public Optional<FirstLevelDealerCodeInfo> getFirstLevelDealerCodeInfos(final long firstLevelDealerId) {
        final Pair<Date, Date> lastDayDate = this.getLastDayDate();
        final int lastDayActivateCount = this.qrCodeDao.selectFirstLastDayActivateCount(firstLevelDealerId,
                lastDayDate.getLeft(), lastDayDate.getRight());
        final int residueCount = this.qrCodeDao.getFirstResidueCount(firstLevelDealerId);
        final int distributeCount = this.qrCodeDao.getFirstDistributeCount(firstLevelDealerId);
        final int distributeToSelfCount = this.qrCodeDao.getFirstDistributeToSelfCount(firstLevelDealerId);
        final int unActivateCount = this.qrCodeDao.getFirstUnActivateCount(firstLevelDealerId);
        final int activateCount = this.qrCodeDao.getFirstActivateCount(firstLevelDealerId);
        final FirstLevelDealerCodeInfo firstLevelDealerCodeInfo = new FirstLevelDealerCodeInfo();
        firstLevelDealerCodeInfo.setDistributeToSelfCount(distributeToSelfCount);
        firstLevelDealerCodeInfo.setLastDayActivateCount(lastDayActivateCount);
        firstLevelDealerCodeInfo.setResidueCount(residueCount);
        firstLevelDealerCodeInfo.setDistributeCount(distributeCount);
        firstLevelDealerCodeInfo.setUnActivateCount(unActivateCount);
        firstLevelDealerCodeInfo.setActivateCount(activateCount);
        return Optional.fromNullable(firstLevelDealerCodeInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param secondLevelDealerId
     * @return
     */
    public Optional<SecondLevelDealerCodeInfo> getSecondLevelDealerCodeInfos(final long secondLevelDealerId) {
        final Pair<Date, Date> lastDayDate = this.getLastDayDate();
        final int lastDayActivateCount = this.qrCodeDao.selectSecondLastDayActivateCount(secondLevelDealerId,
                lastDayDate.getLeft(), lastDayDate.getRight());
        final int codeCount = this.qrCodeDao.getSecondCodeCount(secondLevelDealerId);
        final int unActivateCount = this.qrCodeDao.getSecondUnActivateCount(secondLevelDealerId);
        final SecondLevelDealerCodeInfo secondLevelDealerCodeInfo = new SecondLevelDealerCodeInfo();
        secondLevelDealerCodeInfo.setLastDayActivateCount(lastDayActivateCount);
        secondLevelDealerCodeInfo.setCodeCount(codeCount);
        secondLevelDealerCodeInfo.setUnActivateCount(unActivateCount);
        return Optional.fromNullable(secondLevelDealerCodeInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerId
     * @return
     */
    @Override
    public int getDistributeToSecondDealerCodeCount(long firstLevelDealerId) {
        return this.qrCodeDao.selectDistributeToSecondDealerCodeCount(firstLevelDealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId   代理商id
     * @param level   级别
     * @return
     */
    @Override
    public Optional<MyMerchantCount> getMyMerchantCount(final long dealerId, final int level) {
        //本周开始时间
        Date theWeekStartTime = new DateTime().minusDays(new DateTime().getDayOfWeek() - 1).withTimeAtStartOfDay().toDate();
        final int currentWeekActivateCodeCount =
                this.qrCodeDao.selectCurrentWeekActivateCodeCount(dealerId, level, theWeekStartTime, new Date());
        final int activateCodeCount = this.qrCodeDao.selectActivateCodeCount(dealerId, level);
        final MyMerchantCount myMerchantCount = new MyMerchantCount();
        myMerchantCount.setActivateMerchantCount(activateCodeCount);
        myMerchantCount.setCurrentWeekActivateMerchantCount(currentWeekActivateCodeCount);
        return Optional.fromNullable(myMerchantCount);
    }

    /**
     * 下载code
     *
     * @param count
     */
    @Override
    @Transactional
    public String downloadCodeZip(final long adminId, final int count, final String baseUrl,final long productId,final String sysType) {
        final List<QRCode> codes = generateCode(adminId, count,productId,sysType);
        //download
        ZipOutputStream zipOutputStream = null;
        try {
            final String tempDir = QRCodeUtil.getTempDir();
            final File zipFile = new File(tempDir + File.separator + codes.get(0).getCode() + "-" +
                    codes.get(count - 1).getCode() + ".zip");
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (QRCode qrCode : codes) {
                final StringBuilder urlBuilder = new StringBuilder(baseUrl + "?");
                urlBuilder.append("code").append("=").append(qrCode.getCode()).append("&")
                        .append("sign").append("=").append(qrCode.getSign());
                final byte[] codeByte = QRCodeUtil.generateCodeByte(tempDir, urlBuilder.toString(), qrCode.getCode());
                zipOutputStream.putNextEntry(new ZipEntry("QR_CODE" + qrCode.getCode() + "." + QRCodeUtil.DEFAULT_IMAGE_SUFFIX));
                zipOutputStream.write(codeByte);
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
            return zipFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download code zip error", e);
            e.printStackTrace();
        }  finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (final IOException e) {
                    log.error("close zipOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    /**
     * 下载code Excel
     *
     * @param count
     */
    @Override
    @Transactional
    public String downloadExcel(final long adminId, final int count, final String baseUrl,final long productId,final String sysType) {
        final List<QRCode> codes = generateCode(adminId, count,productId,sysType);
        //download
        final String tempDir = QRCodeUtil.getTempDir();
        final File excelFile = new File(tempDir + File.separator + codes.get(0).getCode() + "-" +
                codes.get(count - 1).getCode() + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet(codes, baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download code zip error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * test
     */
    @Override
    @Transactional
    public String downloadExcelByCode(final int adminId, final long startId, final long endId, final String baseUrl) {

        final List<QRCode> codes = this.qrCodeDao.selectByIdRange(startId, endId);
        if (CollectionUtils.isEmpty(codes)) {
            return "";
        }
        final String tempDir = QRCodeUtil.getTempDir();
        final File excelFile = new File(tempDir + File.separator + codes.get(0).getCode() + "-" +
                codes.get(codes.size() - 1).getCode() + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet(codes, baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download code zip error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Long> getUnDistributeCode(final String sysType) {
        return this.qrCodeDao.getUnDistributeCode(sysType);
    }

    /**
     * {@inheritDoc}
     *
     * @param startCode
     * @param endCode
     * @return
     */
    @Override
    public boolean checkRangeQRCode(final String startCode, final String endCode) {
        final int updateCount = this.qrCodeDao.selectCountByRange(startCode, endCode);
        final int checkCount = (int)(Long.valueOf(endCode) - Long.valueOf(startCode) + 1);
        if (updateCount != checkCount) {
            log.info("给代理商分配指定的二维码，校验出错，码段范围是[{}]--[{}],实际可用个数应该是[{}]", startCode, endCode, updateCount);
            return false;
        }
        return true;
    }


    @Override
    public void test(final String startCode, final String endCode) {
        this.qrCodeDao.test(startCode, endCode);
    }


    /**
     * 生成code
     *
     * @param adminId
     * @param count
     * @return
     */
    private List<QRCode> generateCode(final long adminId, final int count,long productId,String sysType) {
        final List<QRCode> codes = new ArrayList<>(count);
        final Optional<QRCode> latestQRCode = this.getLatestQRCodeForUpdate();
        String startCode = QRCodeConsts.start_code_num;
        if (latestQRCode.isPresent()) {
            startCode = latestQRCode.get().getCode();
        }
        for (int i = 1; i <= count; i++) {
            final QRCode qrCode = new QRCode();
            qrCode.setCode(String.valueOf(Long.valueOf(startCode) + i));
            qrCode.setAdminId(adminId);
            qrCode.setFirstLevelDealerId(0);
            qrCode.setSecondLevelDealerId(0);
            qrCode.setMerchantId(0);
            qrCode.setSalt(RandomStringUtils.randomAlphanumeric(16));
            qrCode.setSign(qrCode.getSignCode());
            qrCode.setDistributeStatus(EnumQRCodeDistributionStatus.UN_DISTRIBUTION.getCode());
            qrCode.setActivateStatus(EnumQRCodeActivateStatus.UN_ACTIVATE.getCode());
            qrCode.setType(EnumQRCodeType.SCAN_CODE.getCode());
            qrCode.setProductId(productId);
            qrCode.setSysType(sysType);
            this.add(qrCode);
            codes.add(qrCode);
        }
        return codes;
    }

    /**
     * 生成excleVo
     *
     * @param codes
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateCodeExcelSheet(List<QRCode> codes, final String baseUrl) {
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("qr_code");
        heads.add("卡号");
        heads.add("ID");
        datas.add(heads);
        for (QRCode qrCode : codes) {
            final ArrayList<String> columns = new ArrayList<>();
            final StringBuilder urlBuilder = new StringBuilder(baseUrl + "?");
            urlBuilder.append("code").append("=").append(qrCode.getCode()).append("&")
                    .append("sign").append("=").append(qrCode.getSign());
            columns.add(qrCode.getCode());
            columns.add(urlBuilder.toString());
            datas.add(columns);
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    private Pair<Date, Date> getLastDayDate() {
        final String format = DateFormatUtil.format(new DateTime(new Date().getTime()).minusDays(1).toDate(), DateFormatUtil.yyyy_MM_dd);
        return Pair.of(DateFormatUtil.parse(format + " " + "00:00:01", DateFormatUtil.yyyy_MM_dd_HH_mm_ss),
                DateFormatUtil.parse(format + " " + "23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
    }

    /**
     * 从list中，将连续的二维码分为一组，返回所有组
     *
     * @param qrCodes
     * @return
     */
    public List<Pair<QRCode, QRCode>> getPairQRCodeList(final List<QRCode> qrCodes) {
        final List<Pair<QRCode, QRCode>> pairs = new ArrayList<>();
        while (!CollectionUtils.isEmpty(qrCodes)){
            final Iterator<QRCode> codeIterator = qrCodes.iterator();
            final QRCode first = codeIterator.next();
            codeIterator.remove();
            QRCode pre = first;
            while (codeIterator.hasNext()) {
                final QRCode next = codeIterator.next();
                if ((Long.valueOf(next.getCode()) - Long.valueOf(pre.getCode())) != 1) {
                    break;
                }
                codeIterator.remove();
                pre = next;
            }
            pairs.add(Pair.of(first, pre));
        }
        return pairs;
    }

    /**
     * {@inheritDoc}
     *
     * @param startCode
     * @param endCode
     * @return
     */
    @Override
    public List<Long> getUnDistributeCodeByRangeCode(final String startCode, final String endCode, final String sysType) {
        return this.qrCodeDao.selectUnDistributeCodeByRangeCode(startCode, endCode, sysType);
    }

    @Override
    public CodeQueryResponse getCode(String code) {
        CodeQueryResponse codeQueryResponse = this.qrCodeDao.getCode(code);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getProxyName(long firstLevelDealerId) {
        CodeQueryResponse codeQueryResponse = this.qrCodeDao.getProxyName(firstLevelDealerId);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getProxyName1(long secondLevelDealerId) {
        CodeQueryResponse codeQueryResponse = this.qrCodeDao.getProxyName1(secondLevelDealerId);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getMerchantName(long merchantId) {
        CodeQueryResponse codeQueryResponse = this.qrCodeDao.getMerchantName(merchantId);
        return codeQueryResponse;
    }

    /**
     * 按码段查询
     *
     * @param code
     * @param sysType
     * @return
     */
    @Override
    public Optional<QRCode> getByCode(String code, String sysType) {
        return Optional.fromNullable(this.qrCodeDao.selectByCodeAndSysType(code,sysType));
    }



    /**
     * 绑定店铺个数
     *
     * @param shopId
     * @param sysType
     * @return
     */
    @Override
    public int bindShopCount(long shopId, String sysType) {
        return this.qrCodeDao.bindShopCount(shopId,sysType);
    }

    /**
     * 二维码列表
     *
     * @param shopId
     * @param sysType
     * @return
     */
    @Override
    public List<QRCodeList> bindShopList(long shopId, String sysType) {
        return this.qrCodeDao.bindShopList(shopId,sysType);
    }
    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param startCode
     * @param endCode
     * @param sysType
     * @return
     */
    @Override
    public List<QRCode> getUnDistributeCodeByDealerIdAndRangeCodeAndSysType(long dealerId, String startCode, String endCode,String sysType) {
        return this.qrCodeDao.selectUnDistributeCodeByDealerIdAndRangeCodeAndSysType(dealerId, startCode, endCode,sysType);
    }

    /**
     * 按产品类型查询某个代理商下的所有二维码
     *
     * @param dealerId
     * @param sysType
     * @return
     */
    @Override
    public List<QRCode> getUnDistributeCodeByDealerIdAndSysType(long dealerId, String sysType) {
        return this.qrCodeDao.getUnDistributeCodeByDealerIdAndSysType(dealerId,sysType);
    }

    /**
     * admin查询所有未分配的二维码个数
     *
     * @param sysType
     * @return
     */
    @Override
    public List<QRCode> getUnDistributeCodeBySysType(String sysType) {
        return this.qrCodeDao.getUnDistributeCodeBySysType(sysType);
    }

    /**
     * 根据码段和产品类型
     *
     * @param startCode
     * @param endCode
     * @param sysType
     * @return
     */
    @Override
    public List<QRCode> getUnDistributeCodeByCodeAndSysType(String startCode, String endCode, String sysType) {
        return this.qrCodeDao.getUnDistributeCodeByCodeAndSysType(startCode,endCode,sysType);
    }
}
