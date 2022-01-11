package com.tpay.domains.franchisee_upload.application;

import com.tpay.commons.aws.S3FileUploader;
import com.tpay.domains.franchisee_upload.domain.BusinessLicenseEntity;
import com.tpay.domains.franchisee_upload.domain.BusinessLicenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class FranchiseeUploadService {

  private final S3FileUploader s3FileUploader;
  private final BusinessLicenseRepository businessLicenseRepository;

  @Transactional
  public void uploadDocuments(Long franchiseeIndex, MultipartFile businessLicenseImage) {
    try {
      String s3Path = s3FileUploader.upload(franchiseeIndex.toString(), businessLicenseImage);
      BusinessLicenseEntity businessLicenseEntity = BusinessLicenseEntity.builder()
          .s3Path(s3Path)
          .build();
      businessLicenseRepository.save(businessLicenseEntity);
      System.out.println(s3Path);
    } catch (IOException ioE) {
      System.out.println("s3FileUpload Service Failed");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("global Exception");
    }
  }
}
