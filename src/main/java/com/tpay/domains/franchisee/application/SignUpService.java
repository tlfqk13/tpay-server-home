package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.commons.exception.detail.InvalidBusinessNumberException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.commons.regex.RegExType;
import com.tpay.commons.regex.RegExUtils;
import com.tpay.domains.alimtalk.domain.dto.AlimTalkApiDto;
import com.tpay.domains.alimtalk.presentation.AlimTalkService;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantSaveService;
import com.tpay.domains.push.application.PushTokenService;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.domain.PushTokenEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpService {

    private final FranchiseeRepository franchiseeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegExUtils regExUtils;
    private final FranchiseeApplicantSaveService franchiseeApplicantSaveService;
    private final UserPushTokenService userPushTokenService;
    private final PushTokenService pushTokenService;
    private final AlimTalkService alimTalkService;

    @Transactional
    public Long signUp(FranchiseeSignUpRequest request, boolean isApi) {

        String businessNumber = request.getBusinessNumber();
        if (!regExUtils.validate(RegExType.BUSINESS_NUMBER, businessNumber)) {
            throw new InvalidBusinessNumberException(
                    ExceptionState.INVALID_BUSINESS_NUMBER,
                    "Required Business Number Format : (XXX-XX-XXXXX)");
        }

        String password = request.getPassword();
        if (!isApi) {
            if (!regExUtils.validate(RegExType.PASSWORD, password)) {
                throw new InvalidPasswordException(
                        ExceptionState.INVALID_PASSWORD, "Invalid Password Format");
            }
        }

        if (franchiseeRepository.existsByBusinessNumber(businessNumber.replaceAll("-", ""))) {
            throw new AlreadyExistsException(ExceptionState.ALREADY_EXISTS, "Franchisee Already Exists");
        }

        if (!regExUtils.validate(RegExType.EMAIL, request.getEmail())) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Email Format");
        }

        String encodedPassword = null;
        if (!isApi) {
            encodedPassword = passwordEncoder.encode(password);
        }
        double defaultBalancePercentage = 0;
        FranchiseeEntity franchiseeEntity =
                FranchiseeEntity.builder()
                        .businessNumber(request.getBusinessNumber())
                        .storeName(request.getStoreName())
                        .storeAddressNumber(request.getStoreAddressNumber())
                        .storeAddressBasic(request.getStoreAddressBasic())
                        .storeAddressDetail(request.getStoreAddressDetail())
                        .sellerName(request.getSellerName())
                        .storeTel(request.getStoreTel().replaceAll("-", ""))
                        .productCategory(request.getProductCategory())
                        .password(encodedPassword)
                        .signboard(request.getSignboard())
                        .storeNumber(request.getStoreNumber().replaceAll("-", ""))
                        .email(request.getEmail())
                        .isTaxRefundShop(request.getIsTaxRefundShop())
                        .balancePercentage(defaultBalancePercentage)
                        .build();
        franchiseeRepository.save(franchiseeEntity);
        franchiseeApplicantSaveService.save(franchiseeEntity);

        log.trace("==========================회원가입===========================");
        log.trace("[사업자번호] : {}", request.getBusinessNumber());
        log.trace("[가맹점  명] : {}", request.getStoreName());
        log.trace("[대표자  명] : {}", request.getSellerName());
        // 토큰 테이블에 토큰 저장
        if (!(request.getPushToken() == null)) {
            PushTokenEntity pushTokenEntity = new PushTokenEntity(request.getPushToken());
            PushTokenEntity findPushTokenEntity = pushTokenService.saveIfNotExists(pushTokenEntity);
            // 유저-토큰 테이블 세이브 (기존 데이터는 삭제)
            userPushTokenService.deleteIfExistsAndSave(franchiseeEntity, findPushTokenEntity);
        } else {
            log.trace("===================토큰없이 회원가입 완료====================");
        }

        // 알림톡 메시지 전달
        AlimTalkApiDto.Request message
                = alimTalkService.createAlimtalkMessage(request.getStoreName(), request.getStoreNumber());
        alimTalkService.sendAlimTalkApiMessage(message);
        return franchiseeEntity.getId();
    }

    private String joinStrings(String... strings) {
        return String.join("|", strings);
    }

    public Long signUp(FranchiseeSignUpRequest request) {
        return signUp(request, false);
    }
}
