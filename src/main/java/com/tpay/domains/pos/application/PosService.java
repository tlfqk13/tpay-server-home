package com.tpay.domains.pos.application;

import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.pos.domain.PosType;
import com.tpay.domains.pos.domain.UpdatePosTypeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PosService {

    private final FranchiseeFindService franchiseeFindService;


    @Transactional
    public Boolean updatePosType(Long franchiseeIndex, UpdatePosTypeDto.Request request) {

        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        PosType posType = posNameToPosType(request);
        franchiseeEntity.updatePosInfo(request.getIsConnected(),posType);
        return true;
    }


    private static PosType posNameToPosType(UpdatePosTypeDto.Request request){
        String posType = request.getPosType();
        PosType posTypeEnum = PosType.INIT;
        switch (posType) {
            case "그로잉세일즈":
                posTypeEnum = PosType.P001;
                break;
            case "OK 포스":
                posTypeEnum = PosType.P002;
                break;
            case "포스 뱅크":
                posTypeEnum = PosType.P003;
                break;
        }
        return posTypeEnum;
    }
}
