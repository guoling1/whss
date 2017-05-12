package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.*;

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

    /**
     * 根据店铺id查询原来的照片
     * @param sid
     * @return
     */
    HsyHistoryPhotoChangeResponse getHsyPhoto(long sid);

    /**
     * 保存hsy历史照片
     * @param sid
     * @param photo
     * @param hsyType
     * @param reasonDescription
     * @param cardName
     * @param operator
     */
    void saveHsyHistory(long sid, String photo, int hsyType, String reasonDescription, String cardName, String operator);

    /**
     * 更改或上传身份证正面
     * @param photoName
     * @param sid
     */
    void saveHsyPhotoChang(String photoName, long sid);

    /**
     * 更改或上传身份证反面
     * @param photoName
     * @param sid
     */
    void saveHsyPhotoChang1(String photoName, long sid);

    /**
     * 更改或上传营业执照
     * @param photoName
     * @param sid
     */
    void saveHsyPhotoChang2(String photoName, long sid);

    /**
     * 更改或上传店面照片
     * @param photoName
     * @param sid
     */
    void saveHsyPhotoChang3(String photoName, long sid);

    /**
     * 更改或上传收银台
     * @param photoName
     * @param sid
     */
    void saveHsyPhotoChang4(String photoName, long sid);

    /**
     * 更改或上传室内照片
     * @param photoName
     * @param sid
     */
    void saveHsyPhotoChang5(String photoName, long sid);

    /**
     * 查好hsy的历史
     * @param request
     * @return
     */
    List<HsyHistoryPhotoChangeResponse> selectHsyHistory(HistoryPhotoChangeRequest request);

    /**
     * 查好hsy的历史总数
     * @param request
     * @return
     */
    int selectHsyHistoryCount(HistoryPhotoChangeRequest request);
}
