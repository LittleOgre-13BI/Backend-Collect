package com.utils;

import com.aliyun.oss.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class OssFileUtils {

    private static String endpoint = "oss-cn-beijing.aliyuncs.com";

    private static String accessKeyId = "LTAI5tGkbp9z8XLMjFQbv72w";

    private static String accessKeySecret = "FvQMunRrgTtEztp9tBkE3p6omiCvVS";

    private static String bucketName = "file-collect";

    private static String dirPath = "collect";

    /**
     * 功能描述:
     *
     * @param:[objectKey, multipartFile 文件的新名称]
     * @return:java.lang.String
     * @date:2018/10/14 15:46
     **/
    public static String uploadFile(String objectName, MultipartFile multipartFile)
            throws OSSException, ClientException, FileNotFoundException {

        objectName = dirPath + "/" + objectName;
        // 创建OSSClient的实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(multipartFile.getBytes()));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl(bucketName, objectName, expiration).toString();
        return url;
    }
}
