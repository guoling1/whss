package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by Allen on 2017/1/12.
 */
public interface HsyFileService {
    public String insertFileAndUpload(MultipartFile file,String type)throws Exception;
    public String insertFileShopAndUpload(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException;
}
