package com.tpay.commons.push;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class PushHistoryStringFormat {

    private static String removeDoubleQuote(String string) {
        return string.replace("\"", "");
    }

    private static String removeHeader(String string) {
        return string.substring(12, string.length() - 2);
    }

    public static Map<String, String> pushHistoryStringFormatter(JsonObject jsonObject, String response) {

        Map<String, String> result = new HashMap<>();

        JsonObject message = jsonObject.getAsJsonObject("message");
        JsonObject notification = message.getAsJsonObject("notification");

        String title = removeDoubleQuote(notification.get("title").toString());
        String body = removeDoubleQuote(notification.get("body").toString());

        String pushType = "";
        if(message.get(PushType.TOPIC.toString()) != null) {
            pushType = PushType.TOPIC.toString();
        } else if(message.get(PushType.TOKEN.toString()) != null) {
            pushType = PushType.TOKEN.toString();
        }
        String pushTypeValue = removeDoubleQuote(message.get(pushType).toString());

        String formattedResponse = removeHeader(response);


        JsonObject data = message.getAsJsonObject("data");

        String pushCategory = removeDoubleQuote(data.get("pushCategory").toString());
        String link = removeDoubleQuote(data.get("link").toString());

        result.put("title", title);
        result.put("body", body);
        result.put("pushType", pushType);
        result.put("pushTypeValue", pushTypeValue);
        result.put("response", formattedResponse);
        result.put("pushCategory", pushCategory);
        result.put("link", link);

        return result;
    }
}
