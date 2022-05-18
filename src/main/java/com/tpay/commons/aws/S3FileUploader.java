package com.tpay.commons.aws;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@NoArgsConstructor
public class S3FileUploader {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${spring.config.activate.on-profile}")
    private String profileName;

    private AmazonS3 s3Client;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withRegion(this.region)
            .build();
    }

    public String uploadJpg(Long franchiseeIndex, String imageCategory, MultipartFile file) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MediaType.ALL_VALUE);
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentDisposition("attachment; filename=\"" + franchiseeIndex + imageCategory + ".jpg\"");
        String key = profileName + "/" + franchiseeIndex + imageCategory;
        s3Client.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, key).toString();
    }

    /**
     * 하나의 notification에 여러개 이미지 저장될 수 있으므로 return 은 Collections
     */
    public Map<String,String> uploadNotifications(Long notificationIndex, Map<String,MultipartFile> files, List<String> fileNames) throws IOException {
        Map<String, String> paths = new LinkedHashMap<>();
        for (int i = 0; i < files.size(); i++) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(MediaType.ALL_VALUE);
            String keyName = fileNames.get(i);
            MultipartFile value = files.get(keyName);
            objectMetadata.setContentLength(value.getSize());
            String key = profileName + "/notification_" + notificationIndex + "/" + keyName;
            s3Client.putObject(new PutObjectRequest(bucket,key,value.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

            paths.put(keyName,s3Client.getUrl(bucket,key).toString());
        }
        return paths;
    }

    public String deleteJpg(Long franchiseeIndex, String imageCategory) {
        String key = profileName + "/" + franchiseeIndex + imageCategory;
        s3Client.deleteObject(bucket, key);
        return "Delete : " + key;
    }

    public String uploadBarcode(Long id, InputStream inputStream) {
        String uri = "";
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(MediaType.ALL_VALUE);
            objectMetadata.setContentLength(inputStream.available());
            String key = "barcode/" + id;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, inputStream, objectMetadata);
            s3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
            uri = s3Client.getUrl(bucket, key).toString();
        } catch (IOException e) {
            log.error("IO exception");
        }
        return uri;
    }

    public String uploadXlsx(Long franchiseeIndex, XSSFWorkbook xssfWorkbook) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        xssfWorkbook.write(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        objectMetaData.setContentLength((long) byteArrayOutputStream.toByteArray().length);
        byteArrayOutputStream.close();
        objectMetaData.setContentDisposition("attachment; filename=\"" + franchiseeIndex + ".xlsx\"");
        String key = profileName + "/" + franchiseeIndex + "excelTest";
        try {
            s3Client.putObject(new PutObjectRequest(bucket, key, byteArrayInputStream, objectMetaData)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } finally {
            byteArrayInputStream.close();
        }
        return s3Client.getUrl(bucket, key).toString();
    }
}
