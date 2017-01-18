package com.jkm.hsy.user.service;

import org.omg.CORBA.portable.InputStream;

/**
 * Created by Administrator on 2017/1/17.
 */
public interface UploadOrDownloadService {

    String getHsyUrl(InputStream inputStream, String fileName);


}
