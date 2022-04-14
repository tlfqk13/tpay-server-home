package com.tpay.domains.push.application;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.PushHistoryEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

@RequiredArgsConstructor
@Service
public class PushNotificationService {

    private final PushHistoryRepository pushHistoryRepository;

    private HttpURLConnection getConnection() throws IOException {
        URL url = new URL("https://fcm.googleapis.com/v1/projects/ktp-app-737ea/messages:send");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
    }

    private String getAccessToken() throws IOException {

        String[] scope = {"https://www.googleapis.com/auth/firebase.messaging"};
        GoogleCredential googleCredential = GoogleCredential
            .fromStream(new ClassPathResource(CustomValue.FIREBASE_SDK_PATH).getInputStream())
            .createScoped(Arrays.asList(scope));
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
    public void requestMessageToFcmServer(JsonObject fcmMessage) throws IOException, FirebaseMessagingException {

        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(fcmMessage.toString().getBytes(StandardCharsets.UTF_8));
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

        JsonObject result = new JsonObject();

        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", request.getTitle());
        jNotification.addProperty("body", request.getBody());

        JsonObject jData = new JsonObject();
        jData.addProperty("pushIndex", getMaxIndex());
        jData.addProperty("pushCategory", request.getPushCategory());
        jData.addProperty("link", request.getLink());

        JsonObject jMessage = new JsonObject();
        jMessage.addProperty(request.getPushType().toString(), request.getPushTypeValue());
        jMessage.add("notification", jNotification);
        jMessage.add("data", jData);

        result.add("message", jMessage);

        return result;

    }

    @Transactional
    public void sendMessage(NotificationDto.Request request) {
        try {
            JsonObject notificationMessage = buildNotificationMessage(request);
            System.out.println("FCM token request body for message using common notification object:");
            // TODO: 2022/04/14 new ?
            new GsonBuilder().setPrettyPrinting().create().toJson(notificationMessage);
            requestMessageToFcmServer(notificationMessage);
        } catch (IOException e) {
            e.getStackTrace();
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }


    private String getMaxIndex() {
        Long pushIndex = pushHistoryRepository.maxIndex();
        String pushIndexToString = "";
        if (pushIndex == null) {
            pushIndexToString = "1";
        } else {
            pushIndex = pushIndex + 1;
            pushIndexToString = pushIndex.toString();
        }

        return pushIndexToString;
    }


}

