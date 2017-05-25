package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Repository
public interface MerchantInfoQueryDao {



    /**
     * 查询所有商户
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoRequest req);

    /**
     * 查询总数
     * @return
     */
    int getCount(MerchantInfoRequest req);

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
     * 下载
     * @param req
     * @return
     */
    List<MerchantInfoResponse> downloade(MerchantInfoRequest req);

    /**
     * 查询状态非3或6
     * @param req
     * @return
     */
    List<MerchantInfoResponse> getAll1(MerchantInfoRequest req);

    /**
     * 查询状态非3或6总数
     * @param req
     * @return
     */
    int getCount1(MerchantInfoRequest req);

    /**
     * 下载
     * @param req
     * @return
     */
    List<MerchantInfoResponse> downloade1(MerchantInfoRequest req);

    /**
     * 查询照片根据merchantId
     * @param merchantId
     * @return
     */
    HistoryPhotoChangeResponse getPhoto(@Param("merchantId") long merchantId);

    /**
     * 保存历史
     * @param map
     */
    void saveHistory(Map map);

    /**
     * 修改或上传结算卡
     * @param map
     */
    void savePhotoChang(Map map);

    /**
     * 修改或上传手持结算卡
     * @param map
     */
    void savePhotoChang1(Map map);

    /**
     * 修改或上传手持身份证
     * @param map
     */
    void savePhotoChang2(Map map);

    /**
     * 修改或上传身份证正面
     * @param map
     */
    void savePhotoChang3(Map map);

    /**
     * 修改或上传身份证反面
     * @param map
     */
    void savePhotoChang4(Map map);

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
    HsyHistoryPhotoChangeResponse getHsyPhoto(@Param("sid") long sid);

    /**
     * 保存hsy历史照片
     * @param map
     */
    void saveHsyHistory(Map map);

    /**
     * 更改或上传身份证正面
     * @param map
     */
    void saveHsyPhotoChang(Map map);

    /**
     * 更改或上传身份证反面
     * @param map
     */
    void saveHsyPhotoChang1(Map map);

    /**
     * 更改或上传营业执照
     * @param map
     */
    void saveHsyPhotoChang2(Map map);

    /**
     * 更改或上传店面照片
     * @param map
     */
    void saveHsyPhotoChang3(Map map);

    /**
     * 更改或上传收银台
     * @param map
     */
    void saveHsyPhotoChang4(Map map);

    /**
     * 更改或上传室内照片
     * @param map
     */
    void saveHsyPhotoChang5(Map map);

    /**
     * 更改或上传结算卡照片
     * @param map
     */
    void saveHsyPhotoChang6(Map map);

    /**
     * 查询hsy历史
     * @param request
     * @return
     */
    List<HsyHistoryPhotoChangeResponse> selectHsyHistory(HistoryPhotoChangeRequest request);

    /**
     * 查询hsy历史总数
     * @param request
     * @return
     */
    int selectHsyHistoryCount(HistoryPhotoChangeRequest request);


}
