package com.typewrite.game.network.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.typewrite.game.util.LogUtil;

/** Utility class for converting messages to and from JSON format. */
public class JsonConverter {
  private static final Gson gson =
      new GsonBuilder().registerTypeAdapter(Message.class, new MessageTypeAdapter()).create();

  /**
   * Converts a message to JSON.
   *
   * @param message the message to convert
   * @return the JSON representation of the message
   */
  public static String toJson(Message message) {
    return gson.toJson(message);
  }

  /**
   * Converts JSON to a message.
   *
   * @param json the JSON string
   * @return the message
   */
  public static Message fromJson(String json) {
    try {
      return gson.fromJson(json, Message.class);
    } catch (JsonSyntaxException e) {
      LogUtil.error("Failed to parse json: " + json);
      e.printStackTrace();
      return null;
    }
  }
}
