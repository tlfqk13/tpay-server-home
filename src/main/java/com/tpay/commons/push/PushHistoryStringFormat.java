package com.tpay.commons.push;

import com.google.gson.JsonObject;

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

        Set<String> keySet = message.keySet();

        JsonObject notification = message.getAsJsonObject(keySet.toArray()[0].toString());

        String title = removeDoubleQuote(notification.get("title").toString());
        String body = removeDoubleQuote(notification.get("body").toString());

        String type = removeDoubleQuote(keySet.toArray()[1].toString());
        String typeValue = removeDoubleQuote(message.getAsJsonPrimitive(keySet.toArray()[1].toString()).toString());

        String formattedResponse = removeHeader(response);

        result.put("title", title);
        result.put("body", body);
        result.put("type", type);
        result.put("typeValue", typeValue);
        result.put("response", formattedResponse);

        return result;
    }
}
