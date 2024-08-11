package com.typewrite.game.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** Utility class for simple JSON serialization. */
public class JsonUtil {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  /**
   * Serializes the specified object into its JSON representation.
   *
   * @param object the object to be serialized.
   * @return a JSON string representation of the object.
   */
  public static String serialize(Object object) {
    return gson.toJson(object);
  }
}
