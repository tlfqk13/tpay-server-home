package com.tpay.domains.franchisee_upload.domain;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "franchisee_upload")
@Entity
public class FranchiseeUploadEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Long franchiseeIndex;

  @NotNull
  private String imageCategory;

  @NotNull
  private String s3Path;

  private String taxFreeStoreNumber;

  @OneToOne
  @JoinColumn(name = "franchisee_id")
  private FranchiseeEntity franchiseeEntity;

  public void update(String s3Path) {
    this.s3Path = s3Path;
  }

  public String updateTaxFreeStoreNumber(String taxFreeStoreNumber){
    this.taxFreeStoreNumber ="제 "+taxFreeStoreNumber.substring(0,3)+"-"+taxFreeStoreNumber.substring(3)+"호";
    return this.taxFreeStoreNumber;
  }
}
