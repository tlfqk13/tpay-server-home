package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
@Profile(value = "security")
public class CustomUserDetailsService implements UserDetailsService {

    private final FranchiseeRepository franchiseeRepository;

    @Override
    public UserDetails loadUserByUsername(String bizNumber) throws UsernameNotFoundException {
        return franchiseeRepository.findByBusinessNumber(bizNumber)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private User createUser(FranchiseeEntity franchisee) {

        List<SimpleGrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new org.springframework.security.core.userdetails.User(franchisee.getBusinessNumber(),
                franchisee.getPassword(),
                grantedAuthorities);
    }
}
