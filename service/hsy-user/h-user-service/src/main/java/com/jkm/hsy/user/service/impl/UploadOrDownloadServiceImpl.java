package com.jkm.hsy.user.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.hsy.user.service.UploadOrDownloadService;
import com.jkm.hsy.user.util.OSSUtil;
import org.joda.time.DateTime;
import org.omg.CORBA.portable.InputStream;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/1/17.
 */
public class UploadOrDownloadServiceImpl implements UploadOrDownloadService {

    @Autowired
    private OSSClient ossClient;

    @Override
    public String getHsyUrl(InputStream inputStream, String fileName) {
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/octet-stream ");
        ossClient.putObject(OSSUtil.getApplicationConfig().ossBucke(), fileName, inputStream,meta);
        return fileName;
    }
}
