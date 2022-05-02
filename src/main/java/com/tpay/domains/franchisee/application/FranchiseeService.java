package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.FindAllResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FranchiseeService {

    private final FranchiseeFindService franchiseeFindService;

    public List<FindAllResponse> findAll() {
        List<FranchiseeEntity> franchiseeEntityList = franchiseeFindService.findAll();
        return franchiseeEntityList.stream().map(FindAllResponse::of).collect(Collectors.toList());
    }
}
