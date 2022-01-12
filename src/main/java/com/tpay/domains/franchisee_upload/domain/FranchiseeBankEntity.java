package com.tpay.domains.franchisee_upload.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "franchisee_bank")
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
