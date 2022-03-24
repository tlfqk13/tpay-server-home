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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    public String deleteJpg(Long franchiseeIndex, String imageCategory) {
        String key = profileName + "/" + franchiseeIndex + imageCategory;
        s3Client.deleteObject(bucket, key);
        return "Delete : " + key;
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
