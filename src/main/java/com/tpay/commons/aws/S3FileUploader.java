package com.tpay.commons.aws;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
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
import java.util.ArrayList;
import java.util.List;


/**
 * AWS S3 버킷 업로더
 */
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

    /**
     * 사후면세 지정증 업로드
     */
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
     * 공지사항 파일업로드
     */
    public String uploadNotice(Long noticeIndex, MultipartFile file, String fileName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MediaType.ALL_VALUE);
        objectMetadata.setContentLength(file.getSize());
        String key = "notice/" + profileName + "/" + noticeIndex + "/" + fileName;
        s3Client.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, key).toString();
    }

    /**
     * 공지사항 파일 삭제 (존재하는 파일 전체 삭제)
     */
    public void deleteNotice(Long noticeIndex) {
        List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
        keys.add(new DeleteObjectsRequest.KeyVersion("notice/" + profileName + "/" + noticeIndex + "/mainImg"));
        keys.add(new DeleteObjectsRequest.KeyVersion("notice/" + profileName + "/" + noticeIndex + "/subImg1"));
        keys.add(new DeleteObjectsRequest.KeyVersion("notice/" + profileName + "/" + noticeIndex + "/subImg2"));
        keys.add(new DeleteObjectsRequest.KeyVersion("notice/" + profileName + "/" + noticeIndex + "/subImg3"));
        DeleteObjectsRequest multipleDeleteObjectsRequest = new DeleteObjectsRequest(bucket)
                .withKeys(keys)
                .withQuiet(false);
        DeleteObjectsResult deleteObjectsResult = s3Client.deleteObjects(multipleDeleteObjectsRequest);
        log.trace("successful delete = {}", deleteObjectsResult.getDeletedObjects().size());
    }

    /**
     * 지정증 삭제 (지정증 사진 수정에서 사용됨)
     */
    public String deleteJpg(Long franchiseeIndex, String imageCategory) {
        String key = profileName + "/" + franchiseeIndex + imageCategory;
        s3Client.deleteObject(bucket, key);

        return "Delete : " + key;
    }

    /**
     * 바코드 업로드
     */
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

    /**
     * 바코드 삭제
     */
    public void deleteBarcode(Long id) {
        String key = "barcode/" + id;
        s3Client.deleteObject(bucket, key);
    }

    /**
     * 엑셀파일 업로드
     */
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

    public String uploadXlsx(Long franchiseeIndex, XSSFWorkbook xssfWorkbook
            , StringBuilder fileName, String month,boolean isCms) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        xssfWorkbook.write(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        objectMetaData.setContentLength((long) byteArrayOutputStream.toByteArray().length);
        byteArrayOutputStream.close();
        StringBuilder key = new StringBuilder();
        if(isCms){
            key.append("CmsMonthlyReport/").append(month).append("/").append(fileName);
        }else{
            key.append("VatMonthlyReport/").append(month).append("/").append(fileName);
        }
        //String key = "monthlyReport/" + month + "/" + fileName;
        try {
            s3Client.putObject(new PutObjectRequest(bucket, String.valueOf(key), byteArrayInputStream, objectMetaData)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } finally {
            byteArrayInputStream.close();
        }
        return s3Client.getUrl(bucket, String.valueOf(key)).toString();
    }
}
