package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/1/12.
 */
public enum FileType {
    LICENCE(1,"LICENCE","营业执照"),
    STOREFRONT(2,"STOREFRONT","店面照片"),
    COUNTER(3,"COUNTER","收银台"),
    INDOOR(4,"INDOOR","室内照"),
    IDCARDF(5,"IDCARDF","身份证正面照"),
    IDCARDB(6,"IDCARDB","身份证背面照"),
    IDCARDH(7,"IDCARDH","身份证手持照"),
    IDCARDC(8,"IDCARDC","结算卡号正面照"),
    CONTRACTID(9,"CONTRACTID","签约合同照");

    public int fileKey;
    public String fileIndex;
    public String fileValue;

    FileType(int fileKey,String fileIndex, String fileValue) {
        this.fileKey = fileKey;
        this.fileIndex=fileIndex;
        this.fileValue = fileValue;
    }

    public static boolean contains(String fileIndex){
        for(FileType fileType : FileType.values()){
            if(fileType.fileIndex.equals(fileIndex)){
                return true;
            }
        }
        return false;
    }
}
