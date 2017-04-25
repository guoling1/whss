package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.HistoryPhotoChangeRequest;
import com.jkm.hss.merchant.entity.HistoryPhotoChangeResponse;
import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * @param merchantId
     * @param photo
     * @param type
     * @param reasonDescription
     * @param cardName
     * @param operator
     */
    void saveHistory(@Param("merchantId") long merchantId,@Param("photo") String photo,@Param("type") int type
            ,@Param("reasonDescription") String reasonDescription,@Param("cardName") String cardName,
                     @Param("operator") String operator);

    /**
     * 修改或上传结算卡
     * @param photoName
     */
    void savePhotoChang(@Param("photoName") String photoName);

    /**
     * 修改或上传手持结算卡
     * @param photoName
     */
    void savePhotoChang1(@Param("photoName") String photoName);

    /**
     * 修改或上传手持身份证
     * @param photoName
     */
    void savePhotoChang2(@Param("photoName") String photoName);

    /**
     * 修改或上传身份证正面
     * @param photoName
     */
    void savePhotoChang3(@Param("photoName") String photoName);

    /**
     * 修改或上传身份证反面
     * @param photoName
     */
    void savePhotoChang4(@Param("photoName") String photoName);

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
