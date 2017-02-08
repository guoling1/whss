package com.jkm.hsy.user.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.constant.FileType;
import com.jkm.hsy.user.dao.HsyFileDao;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyFileService;
import com.jkm.hsy.user.service.HsyUploadService;
import com.jkm.hsy.user.util.AppDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by Allen on 2017/1/12.
 */
@Service("hsyFileService")
public class HsyFileServiceImpl implements HsyFileService {
    @Autowired
    private HsyFileDao hsyFileDao;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private HsyUploadService hsyUploadService;

    /**HSY001011 上传店铺三张照片*/
    public String insertFileShopAndUpload(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppBizShop appBizShop=null;
        try{
            appBizShop=gson.fromJson(dataParam, AppBizShop.class);
        } catch(Exception e){
            e.printStackTrace();
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        if(files.size()!=3)
            throw new ApiHandleException(ResultCode.UPLOADFILE_COUNT_NOT_RIGHT);
        for (String fileKey : files.keySet()) {
            MultipartFile file=files.get(fileKey);
            if(file==null||(file!=null&&file.getSize()==0))
                throw new ApiHandleException(ResultCode.UPLOADFILE_NOT_EXSITS);
        }

        Set<String> set=files.keySet();
        Iterator<String> it=set.iterator();
        while(it.hasNext()){
            String fileKey=it.next();
            MultipartFile file=files.get(fileKey);
            String type="";
            if(fileKey.equals("fileA")&&FileType.contains(appBizShop.getFileA()))
                type=appBizShop.getFileA();
            else if(fileKey.equals("fileB")&&FileType.contains(appBizShop.getFileB()))
                type=appBizShop.getFileB();
            else if(fileKey.equals("fileC")&&FileType.contains(appBizShop.getFileC()))
                type=appBizShop.getFileC();
            else
                throw new ApiHandleException(ResultCode.FILE_TYPE_NOT_EXSIT);
            String uuid="";
            try {
                uuid=insertFileAndUpload(file, type);
            }catch(Exception e){
                e.printStackTrace();
                throw new ApiHandleException(ResultCode.FILE_UPLOAD_FAIL);
            }
            if(type.equals(FileType.STOREFRONT.fileIndex))
                appBizShop.setStorefrontID(uuid);
            else if(type.equals(FileType.COUNTER.fileIndex))
                appBizShop.setCounterID(uuid);
            else if(type.equals(FileType.INDOOR.fileIndex))
                appBizShop.setIndoorID(uuid);

        }

        Date date=new Date();
        appBizShop.setUpdateTime(date);
        hsyShopDao.update(appBizShop);
        List<AppBizShopUserRole> surList=hsyShopDao.findsurByRoleTypeSid(appBizShop.getId());
        if(surList!=null&&surList.size()!=0)
        {
            AppBizShopUserRole sur= surList.get(0);
            AppAuUser user=new AppAuUser();
            user.setId(sur.getUid());
            user.setAuStep("2");
            user.setUpdateTime(date);
            hsyUserDao.updateByID(user);
        }
        return "";
    }

    public String insertFileAndUpload(MultipartFile file,String type)throws Exception{
        AppCmFile appCmFile=new AppCmFile();
        appCmFile.setUuid(UUID.randomUUID().toString());
        appCmFile.setFileName(file.getOriginalFilename());
        appCmFile.setFileType(type);
        String[] strs=appCmFile.getFileName().split("\\.");
        Date date=new Date();
        appCmFile.setPath(AppConstant.FIlE_ROOT+"/"+type+"/"+ AppDateUtil.formatDate(date,AppDateUtil.DATE_FORMAT_NORMAL)+"/"+appCmFile.getUuid()+"."+strs[strs.length-1]);
        appCmFile.setCreateTime(date);
        appCmFile.setUpdateTime(date);
        File uploadfile=new File(appCmFile.getPath());
        if(!uploadfile.exists())
            uploadfile.mkdirs();
        file.transferTo(uploadfile);
        hsyUploadService.upload(new FileInputStream(uploadfile),appCmFile.getPath());
        hsyFileDao.insert(appCmFile);
        return appCmFile.getUuid();
    }

}
