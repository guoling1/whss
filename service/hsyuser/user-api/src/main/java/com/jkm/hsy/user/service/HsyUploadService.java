package com.jkm.hsy.user.service;

import org.omg.CORBA.portable.InputStream;

/**
 * Created by Administrator on 2017/1/18.
 */
public interface HsyUploadService {

    void upload(InputStream inputStream,String fileName);
}
