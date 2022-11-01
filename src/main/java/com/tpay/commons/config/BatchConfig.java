package com.tpay.commons.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.batch.point_batch.application.PointConfirmedService;
import com.tpay.domains.batch.point_batch.application.PointDeleteService;
import com.tpay.domains.batch.push_batch.application.PushBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;


/**
 * 배치 Configuration
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final PointConfirmedService pointConfirmedService;
    private final PointDeleteService pointDeleteService;
    private final PushBatchService pushBatchService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //파이어베이스 init
        initFirebaseApp();

        //포인트 관련 init
        pointConfirmedService.updateStatus();
        pointDeleteService.deletePoint();

        //푸시 관련 init
        pushBatchService.batchPush();

    }

    @Bean
    public FirebaseApp initFirebaseApp() {
        try {
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(new ClassPathResource(CustomValue.FIREBASE_SDK_PATH).getInputStream())).build();
            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Project Init Failed");
        }
    }
}
