package com.tpay.domains.push.test.application;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tpay.commons.util.PushType;
import com.tpay.domains.push.test.application.dto.NotificationDto;
import com.tpay.domains.push.test.domain.PushHistoryEntity;
import com.tpay.domains.push.test.domain.PushHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

@RequiredArgsConstructor
@Service
public class PushNotificationService {
    private static final String PROJECT_ID = "ktp-app-737ea";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = {MESSAGING_SCOPE};
    public static final String MESSAGE_KEY = "message";

    private final PushHistoryRepository pushHistoryRepository;

    private HttpURLConnection getConnection() throws IOException {
        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
    }

    private String getAccessToken() throws IOException {
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new ClassPathResource("ktp-app-737ea-firebase-adminsdk-nr2ly-18a6c8df40.json").getInputStream())
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    @Transactional
    public void requestMessageToFcmServer(JsonObject fcmMessage) throws IOException {
        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(fcmMessage.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            String response = inputStreamToString(connection.getInputStream());
            pushHistoryRepository.save(PushHistoryEntity.fromJsonObjectAndResponse(fcmMessage, response));
            System.out.println("Message sent to Firebase for delivery, response:");
            System.out.println(response);
        } else {
            System.out.println("Unable to send message to Firebase:");
            String response = inputStreamToString(connection.getErrorStream());
            System.out.println(response);
        }
    }


    private JsonObject buildNotificationMessage(NotificationDto.Request request) {

        String title = request.getTitle();
        String body = request.getBody();
        PushType type = request.getType();
        String typeValue = request.getTypeValue();
        String num = request.getNum();
        String linking = request.getLinking();

        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", title);
        jNotification.addProperty("body", body);

        JsonObject jData = new JsonObject();
        jData.addProperty("num", num);
        jData.addProperty("linking", linking);

        JsonObject jMessage = new JsonObject();
        jMessage.addProperty(type.toString(), typeValue);
        jMessage.add("notification", jNotification);
        jMessage.add("data", jData);

        JsonObject result = new JsonObject();
        result.add(MESSAGE_KEY, jMessage);

        return result;
    }

    @Transactional
    public void sendMessage(NotificationDto.Request request) throws IOException {
        JsonObject notificationMessage = buildNotificationMessage(request);
        System.out.println("FCM token request body for message using common notification object:");
        prettyPrint(notificationMessage);
        requestMessageToFcmServer(notificationMessage);


    }


    private void prettyPrint(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(jsonObject) + "\n");
    }

}

