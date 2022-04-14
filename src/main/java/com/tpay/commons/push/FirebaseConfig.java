package com.tpay.commons.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tpay.commons.custom.CustomValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@EnableScheduling
@Configuration
public class FirebaseConfig implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            ClassPathResource resource = new ClassPathResource(CustomValue.FIREBASE_SDK_PATH);
            InputStream serviceAccount = resource.getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            FirebaseApp.initializeApp(options);

        } catch (FileNotFoundException e) {
            log.error("Firebase ServiceAccountKey FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            log.error("FirebaseOptions IOException" + e.getMessage());
        }
    }
}
