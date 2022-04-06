package com.tpay.commons.push;

import com.google.gson.JsonObject;
import com.tpay.commons.util.PushType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        String type = "";
        if(message.get(PushType.TOPIC.toString()) != null) {
            type = PushType.TOPIC.toString();
        } else if(message.get(PushType.TOKEN.toString()) != null) {
            type = PushType.TOKEN.toString();
        }
        String typeValue = removeDoubleQuote(message.get(type).toString());

        String formattedResponse = removeHeader(response);


        JsonObject data = message.getAsJsonObject("data");

        String num = removeDoubleQuote(data.get("num").toString());
        String linking = removeDoubleQuote(data.get("linking").toString());

        result.put("title", title);
        result.put("body", body);
        result.put("type", type);
        result.put("typeValue", typeValue);
        result.put("response", formattedResponse);
        result.put("num", num);
        result.put("linking", linking);

        return result;
    }
}
