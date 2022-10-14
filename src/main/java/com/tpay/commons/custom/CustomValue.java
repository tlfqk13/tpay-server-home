package com.tpay.commons.custom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomValue {

    public static String IAMPORT_API_KEY;
    public static String IAMPORT_SECRET;
    public static String REFUND_SERVER;
    public static String APPLICATION_CODE;
    public static String PUSH_API_URI;
    public static String PUSH_CONFIG_PATH;
    public static String BARCODE_SAVE_PATH;
    public static String FIREBASE_SDK_PATH;
    public static String S3_BASE_URI;

    @Value("${custom.push.sdk-path}")
    public void setFirebaseSdkPath(String firebaseSdkPath) {FIREBASE_SDK_PATH = firebaseSdkPath;}

    @Value("${custom.iamport.api-key}")
    public void setIamportApiKey(String iamportApiKey) {
        IAMPORT_API_KEY = iamportApiKey;
    }

    @Value("${custom.iamport.secret}")
    public void setIamportSecret(String iamportSecret) {
        IAMPORT_SECRET = iamportSecret;
    }

    @Value("${custom.refund.server}")
    public void setRefundServer(String refundServer) {
        REFUND_SERVER = refundServer;
    }

    @Value("${custom.refund.application.code}")
    public void setApplicationCode(String applicationCode) {
        APPLICATION_CODE = applicationCode;
    }

    @Value("${custom.push.api-url}")
    public void setPushApiUri(String apiUri) {PUSH_API_URI = apiUri;}

    @Value("${custom.push.config-path}")
    public void setConfigPath(String configPath) {PUSH_CONFIG_PATH = configPath;}

    @Value("${custom.barcode.save-path}")
    public void setBarcodeSavePath(String barcodeSavePath) {BARCODE_SAVE_PATH = barcodeSavePath;}

    @Value("${custom.s3.base-uri}")
    public void setS3BaseUri(String baseUri) {S3_BASE_URI = baseUri;}


}
