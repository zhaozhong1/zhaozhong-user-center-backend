package com.zhaozhong.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
@Component
public class AliOSSUtils {

    @Autowired
    AliOSSProperties aliOSSProperties = new AliOSSProperties();
    /**
     * 放入阿里云OSS存储空间
     * @param
     * @return 存储地址URL
     */
    public String putAvatar(@RequestBody MultipartFile avatarFile) throws IOException {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(aliOSSProperties.getAccessKeyId(), aliOSSProperties.getAccessKeySecret());
        OSS ossClient = new OSSClientBuilder().build(aliOSSProperties.getEndpoint(),credentialsProvider);
        InputStream inputStream = avatarFile.getInputStream();
        String originalFilename = avatarFile.getOriginalFilename();
        String objectName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf('.'));
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
        String returnStr = null;
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliOSSProperties.getBucketName(), objectName, inputStream);
            // 上传。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            returnStr = aliOSSProperties.getEndpoint().split("//")[0]+"//"+aliOSSProperties.getBucketName()+"."+aliOSSProperties.getEndpoint().split("//")[1]+"/"+objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            return returnStr;
        }
    }
}
