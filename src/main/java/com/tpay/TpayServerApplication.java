package com.tpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TpayServerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TpayServerApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }
}
