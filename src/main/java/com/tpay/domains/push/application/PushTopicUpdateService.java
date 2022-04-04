package com.tpay.domains.push.application;

import com.tpay.domains.push.domain.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushTopicUpdateService {

    private final PushTokenRepository pushTokenRepository;


}
