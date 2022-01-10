package com.tpay.commons.aws;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
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

  private AmazonS3 s3Client;

  @PostConstruct
  public void setS3Client() {
    AWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

    s3Client = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(this.region)
        .build();
  }

  public String upload(MultipartFile file) throws IOException {
    String filename = file.getOriginalFilename();
    s3Client.putObject(new PutObjectRequest(bucket,filename,file.getInputStream(),null).withCannedAcl(CannedAccessControlList.PublicRead));
    return s3Client.getUrl(bucket,filename).toString();
  }

}
