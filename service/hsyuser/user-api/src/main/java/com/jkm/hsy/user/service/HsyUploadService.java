package com.jkm.hsy.user.service;


import java.io.InputStream;

/**
 * Created by Administrator on 2017/1/18.
 */
public interface HsyUploadService {

    void upload(InputStream inputStream, String fileName);
}
