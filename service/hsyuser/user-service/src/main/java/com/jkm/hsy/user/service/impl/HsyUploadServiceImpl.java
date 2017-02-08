package com.jkm.hsy.user.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.hsy.user.constant.UploadConsts;
import com.jkm.hsy.user.service.HsyUploadService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.omg.CORBA.portable.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/1/18.
 */
@Slf4j
@Service
public class HsyUploadServiceImpl implements HsyUploadService {
    @Autowired
    private OSSClient ossClient;

    @Override
    public void upload(InputStream inputStream, String fileName) {
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/octet-stream ");
        try {
            ossClient.putObject(UploadConsts.getApplicationConfig().ossBucke(), fileName, inputStream,meta);

        }catch (Exception e){
            log.debug("上传文件失败",e);

        }
    }
}
