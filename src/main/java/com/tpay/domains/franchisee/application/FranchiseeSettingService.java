package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.FranchiseeSettingDto;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.commons.util.SettingSelector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeSettingService {

    private final FranchiseeFindService franchiseeFindService;

    @Transactional
    public FranchiseeSettingDto.Response changeSoundOrVibration(Long franchiseeIndex, FranchiseeSettingDto.Request request) {

        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);

        String s;
        if (request.getSettingSelector().equals(SettingSelector.SOUND)){
            franchiseeEntity.updateSound(request.getIsActiveSound());
            s = "SOUND : " + request.getIsActiveSound();
        } else if (request.getSettingSelector().equals(SettingSelector.VIBRATION)){
            franchiseeEntity.updateVibration(request.getIsActiveVibration());
            s = "VIBRATION : " + request.getIsActiveVibration();
        } else {
            s = "Nothing to Update";
        }

        return FranchiseeSettingDto.Response.builder()
            .message(s)
            .build();
    }
}
