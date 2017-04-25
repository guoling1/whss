package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.HistoryPhotoChangeRequest;
import com.jkm.hss.merchant.entity.HistoryPhotoChangeResponse;
import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;

import java.util.List;


/**
 * Created by zhangbin on 2016/11/27.
 */
public interface MerchantInfoQueryService {

    /**
     * 查询所有商户
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoRequest req);

    /**
     * 查询 总数
     * @return
     */
//    int getCount(MerchantInfoRequest req);

    /**
     * 查询待审核商户列表
     * @param req
     * @return
     */
    List<MerchantInfoResponse> getRecord(MerchantInfoRequest req);

    /**
     * 查询待审核总数
     * @return
     */
    int getCountRecord(MerchantInfoRequest req);


    /**
     * 导出所有商户
     * @param req
     * @param baseUrl
     * @return
     */
    String downloadExcel(MerchantInfoRequest req, String baseUrl);

    int getCount(MerchantInfoRequest req);

    /**
     * 查询商户照片
     * @param merchantId
     * @return
     */
    HistoryPhotoChangeResponse getPhoto(long merchantId);

    /**
     * 保存历史
     * @param merchantId
     * @param photo
     * @param type
     * @param reasonDescription
     * @param cardName
     * @param operator
     */
    void saveHistory(long merchantId, String photo, int type, String reasonDescription, String cardName, String operator);

    /**
     * 修改或上传结算卡
     * @param photoName
     */
    void savePhotoChang(String photoName,long merchantId);

    /**
     * 修改或上传手持结算卡
     * @param photoName
     */
    void savePhotoChang1(String photoName,long merchantId);

    /**
     * 修改或上传手持身份证
     * @param photoName
     */
    void savePhotoChang2(String photoName,long merchantId);

    /**
     * 修改或上传身份证正面
     * @param photoName
     */
    void savePhotoChang3(String photoName,long merchantId);

    /**
     * 修改或上传身份证反面
     * @param photoName
     */
    void savePhotoChang4(String photoName,long merchantId);

    /**
     * 查询商户认证历史
     * @param request
     * @return
     */
    List<HistoryPhotoChangeResponse> selectHistory(HistoryPhotoChangeRequest request);

    /**
     * 查询商户认证历史分页
     * @param request
     * @return
     */
    int selectHistoryCount(HistoryPhotoChangeRequest request);



}
