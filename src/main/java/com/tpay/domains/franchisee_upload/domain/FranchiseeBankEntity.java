package com.tpay.domains.franchisee_upload.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Table(name = "franchisee_bank")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Entity
public class FranchiseeBankEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String accountNumber;

  @NotNull
  private String bankName;

  @NotNull
  private String withdrawalDate;
}

