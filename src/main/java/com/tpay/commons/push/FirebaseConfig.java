package com.tpay.commons.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tpay.commons.custom.CustomValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@Slf4j
@EnableScheduling
@Configuration
public class FirebaseConfig implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            initFirebaseApp();
        } catch (IOException e) {
            throw new IllegalArgumentException("firebase Init Failed");
        }
    }

    @Bean
    public FirebaseApp initFirebaseApp() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(new ClassPathResource(CustomValue.FIREBASE_SDK_PATH).getInputStream())).build();
        return FirebaseApp.initializeApp(options);
    }
}
