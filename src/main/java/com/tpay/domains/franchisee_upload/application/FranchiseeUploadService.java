package com.tpay.domains.franchisee_upload.application;

import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.UnknownException;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankRepository;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class FranchiseeUploadService {

  private final S3FileUploader s3FileUploader;
  private final FranchiseeUploadRepository franchiseeUploadRepository;
  private final FranchiseeFindService franchiseeFindService;
  private final FranchiseeBankRepository franchiseeBankRepository;

  @Transactional
  public String uploadDocuments(Long franchiseeIndex, FranchiseeBankInfo franchiseeBankInfo, String imageCategory, MultipartFile uploadImage) {
    try {
      FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
      FranchiseeBankEntity franchiseeBankEntity = FranchiseeBankEntity.builder()
          .accountNumber(franchiseeBankInfo.getAccountNumber().replaceAll("-", ""))
          .bankName(franchiseeBankInfo.getBankName())
          .withdrawalDate(franchiseeBankInfo.getWithdrawalDate())
          .franchiseeEntity(franchiseeEntity)
          .build();
      franchiseeBankRepository.save(franchiseeBankEntity);
    } catch (Exception e) {
      throw new UnknownException(ExceptionState.UNKNOWN, "Bank Info Save Fail");
    }
    boolean exists = franchiseeUploadRepository.existsByFranchiseeIndexAndImageCategory(franchiseeIndex, imageCategory);
    try {

      if (exists) {
        printUpdateFranchisee();
        String delete = s3FileUploader.delete(franchiseeIndex, imageCategory);
        System.out.println(delete);
        String s3Path = s3FileUploader.upload(franchiseeIndex, imageCategory, uploadImage);
        FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadRepository.findByFranchiseeIndexAndImageCategory(franchiseeIndex, imageCategory);
//        필요 없음 - s3Path를 만드는 규칙이 franchiseeIndex와 imageCategory 임
//        franchiseeUploadEntity.update(s3Path);
        return s3Path;

      } else {
        printNewFranchisee();
        String s3Path = s3FileUploader.upload(franchiseeIndex, imageCategory, uploadImage);
        FranchiseeUploadEntity franchiseeUploadEntity = FranchiseeUploadEntity.builder().franchiseeIndex(franchiseeIndex).imageCategory(imageCategory).s3Path(s3Path).build();
        franchiseeUploadRepository.save(franchiseeUploadEntity);
        return s3Path;
      }
    } catch (Exception e) {
      throw new UnknownException(ExceptionState.UNKNOWN, "S3 Image Upload Fail");
    }
  }

  void printNewFranchisee() {
    System.out.println("========================");
    System.out.println("=====      New     =====");
    System.out.println("=====  Franchisee  =====");
    System.out.println("========================");
  }

  void printUpdateFranchisee() {
    System.out.println("========================");
    System.out.println("=====    Update    =====");
    System.out.println("=====  Franchisee  =====");
    System.out.println("========================");
  }
}
