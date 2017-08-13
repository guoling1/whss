package com.jkm.hss.controller.wx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;

/**
 * 断点续传下载用法示例
 *
 */
public class ImageSample {



    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "vRc724tuJUT2G4ZE";
    private static String accessKeySecret = "VusuQzlskl57jS1Vc3M34bazhHu6dO";
    private static String bucketName = "jkm-security";
    private static String key = "admin/20170626/149846945079977331.png";
    
    
    public static void main(String[] args) throws IOException {        

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        
        try {
            String style = "style/avatar_30";

            Date expiration = new Date(new Date().getTime() + 30*60*1000);
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.GET);
            req.setExpiration(expiration);
            req.setProcess(style);
            URL url = ossClient.generatePresignedUrl(req);
            System.out.println(url);

            // 缩放
            style = "image/resize,m_fixed,w_100,h_100";
            GetObjectRequest request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-resize.jpg"));
            
            // 裁剪
            style = "image/crop,w_100,h_100,x_100,y_100,r_1"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-crop.jpg"));
            
            // 旋转
            style = "image/rotate,90"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-rotate.jpg"));
            
            // 锐化
            style = "image/sharpen,100"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-sharpen.jpg"));
            
            // 水印
            style = "image/watermark,text_SGVsbG8g5Zu-54mH5pyN5YqhIQ"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-watermark.jpg"));
            
            // 格式转换
            style = "image/format,png"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-format.png"));
            
            // 图片信息
            style = "image/info"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-info.txt"));
            
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }
}
