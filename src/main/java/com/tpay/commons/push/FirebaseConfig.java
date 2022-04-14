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

import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseConfig implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initFirebaseApp();
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
