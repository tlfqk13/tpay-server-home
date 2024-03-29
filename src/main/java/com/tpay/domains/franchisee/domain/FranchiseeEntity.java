package com.tpay.domains.franchisee.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateDtoRequest;
import com.tpay.domains.franchisee_applicant.application.dto.DetailFranchiseeInfo;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.pos.domain.PosType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.tpay.domains.refund_core.application.dto.RefundCustomValue.REFUND_STEP_ONE;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchisee")
@Entity
@ToString
public class FranchiseeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "memNm", length = 50, nullable = false)
    private String memberName;

    @NotNull
    @Column(name = "memNo", length = 20, nullable = false)
    private String memberNumber;

    @NotNull
    @Column(name = "bizNo", length = 10, nullable = false)
    private String businessNumber;

    @NotNull
    @Column(name = "storeNm", length = 50, nullable = false)
    private String storeName;

    @NotNull
    @Column(name = "storeAddrNum", length = 140, nullable = false)
    private String storeAddressNumber;

    @NotNull
    @Column(name = "storeAddrBasic", length = 140, nullable = false)
    private String storeAddressBasic;

    @NotNull
    @Column(name = "storeAddrDetail", length = 140, nullable = false)
    private String storeAddressDetail;

    @NotNull
    @Column(name = "selNm", length = 40, nullable = false)
    private String sellerName;

    @NotNull
    @Column(name = "storeTel", length = 26, nullable = false)
    private String storeTel;

    @NotNull
    @Column(name = "prdNm", length = 100, nullable = false)
    private String productCategory;

    private String password;

    private long balance;

    @Column(name = "signboard")
    private String signboard; // 간판명

    @Column(name = "storeNumber")
    private String storeNumber; // 매장번호

    @Column(name = "email")
    private String email; // email 주소

    // TODO: 2022/03/16 Boolean 으로 변경요망
    @Column(name = "isTaxRefundShop", length = 5)
    private String isTaxRefundShop;

    @Column(name = "isRefundOnce", length = 5)
    @ColumnDefault("false") // ddl auto-create 아니면 쓸모는 없음
    private Boolean isRefundOnce;

    @Column(length = 2)
    private String refundStep;
    @Column(name = "popUp", length = 1)
    private boolean popUp;

    @Column
    private Boolean isActiveSound;

    @Column
    private Boolean isActiveVibration;

    @Column
    private Boolean isConnectedPos;

    @Column
    @Enumerated(EnumType.STRING)
    private PosType posType;

    @Column
    private double balancePercentage;

    @Builder
    public FranchiseeEntity(
            String businessNumber,
            String storeName,
            String storeAddressNumber,
            String storeAddressBasic,
            String storeAddressDetail,
            String sellerName,
            String storeTel,
            String productCategory,
            String password,

            String signboard,
            String storeNumber,
            String email,
            String isTaxRefundShop,
            double balancePercentage
    ) {
        this.memberName = "";
        this.memberNumber = "";
        this.businessNumber = businessNumber.replaceAll("-", "");
        this.storeName = storeName;
        this.storeAddressNumber = storeAddressNumber;
        this.storeAddressBasic = storeAddressBasic;
        this.storeAddressDetail = storeAddressDetail;
        this.sellerName = sellerName;
        this.storeTel = storeTel;
        this.productCategory = productCategory;
        this.password = password;
        this.balance = 0;

        this.signboard = signboard;
        this.storeNumber = storeNumber;
        this.email = email;
        this.isTaxRefundShop = isTaxRefundShop;
        this.isRefundOnce = false;
        this.popUp = true;
        this.isActiveSound = true;
        this.isActiveVibration = true;
        this.isConnectedPos = false;
        this.refundStep = REFUND_STEP_ONE;
        this.posType = PosType.INIT;
        this.balancePercentage = balancePercentage;
    }

    public FranchiseeEntity changeBalance(SignType signType, long change) {
        this.balance += signType == SignType.POSITIVE ? change : -change;
        if (balance < 0) {
            throw new IllegalArgumentException("Balance should not be negative.");
        }
        return this;
    }

    public FranchiseeEntity withdrawalBalance(long amount) {
        this.balance -= amount;
        if (balance < 0) {
            throw new IllegalArgumentException("Balance should not ne negative.");
        }
        return this;
    }

    public FranchiseeEntity resetPassword(String password) {
        this.password = password;
        return this;
    }

    public void modifyInfo(String storeNumber, String email) {
        this.storeNumber = storeNumber;
        this.email = email;
    }

    public void memberInfo(String memberName, String memberNumber) {
        this.memberName = memberName;
        this.memberNumber = memberNumber;
    }

    public void isRefundOnce() {
        this.isRefundOnce = true;
    }

    public boolean isValidUser(String name, String phoneNumber) {
        return name.equals(this.sellerName) && phoneNumber.equals(this.storeTel);
    }

    public void popUpFalse() {
        this.popUp = false;
    }

    public void updateSound(Boolean isActiveSound) {
        this.isActiveSound = isActiveSound;
    }

    public void updateVibration(Boolean isActiveVibration) {
        this.isActiveVibration = isActiveVibration;
    }

    public void updatePosInfo(Boolean isConnectedPos, PosType posType) {
        this.isConnectedPos = isConnectedPos;
        this.posType = posType;
    }

    public FranchiseeEntity updateFranchisee(FranchiseeUpdateDtoRequest franchiseeUpdateDtoRequest) {
        this.storeNumber = franchiseeUpdateDtoRequest.getStoreNumber();
        this.email = franchiseeUpdateDtoRequest.getEmail();
        return this;
    }

    // 2022/08/19 어드민 상세보기 > 수정하기
    public FranchiseeEntity updateFranchisee(DetailFranchiseeInfo request) {
        this.storeName = request.getStoreName();
        this.sellerName = request.getSellerName();
        this.businessNumber = request.getBusinessNumber().replaceAll("-", "");
        this.storeTel = request.getStoreTel().replaceAll("-", "");
        this.storeNumber = request.getStoreNumber().replaceAll("-", "");
        this.email = request.getEmail();
        this.signboard = request.getSignboard();
        this.productCategory = request.getProductCategory();
        this.storeAddressBasic = request.getStoreAddressBasic();
        this.storeAddressDetail = request.getStoreAddressDetail();
        this.balancePercentage = request.getBalancePercentage();
        this.refundStep = request.getRefundStep();
        return this;
    }

    public double updateBalancePercentage(double balancePercentage) {
        this.balancePercentage = balancePercentage;
        return balancePercentage;
    }

    public void updateRefundStep(String refundStep) {
        this.refundStep = refundStep;
    }
}
