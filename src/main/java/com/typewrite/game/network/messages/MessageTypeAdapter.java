package com.typewrite.game.network.messages;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/** Custom adapter for serializing and deserializing messages. */
public class MessageTypeAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

  @Override
  public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject obj = (JsonObject) context.serialize(src);
    obj.addProperty("messageType", src.getClass().getSimpleName());
    return obj;
  }

  @Override
  public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    String messageType = jsonObject.get("messageType").getAsString();

    return switch (messageType) {
      case "PlayerJoinRequest" -> context.deserialize(jsonObject, PlayerJoinRequest.class);
      case "PlayerJoinResponse" -> context.deserialize(jsonObject, PlayerJoinResponse.class);
      case "PlayerReadyNotification" ->
          context.deserialize(jsonObject, PlayerReadyNotification.class);
      case "PlayerReadyResponse" -> context.deserialize(jsonObject, PlayerReadyResponse.class);
      case "GameStartNotification" -> context.deserialize(jsonObject, GameStartNotification.class);
      case "PlayerProgressUpdate" -> context.deserialize(jsonObject, PlayerProgressUpdate.class);
      case "PlayerProgressBroadcast" -> // Add this case for PlayerProgressBroadcast
          context.deserialize(jsonObject, PlayerProgressBroadcast.class);
      case "TextSelectedNotification" ->
          context.deserialize(jsonObject, TextSelectedNotification.class);
      case "TextMessage" -> context.deserialize(jsonObject, TextMessage.class);
      case "GameFinished" -> context.deserialize(jsonObject, GameFinished.class);
      case "EmojiMessage" -> context.deserialize(jsonObject, EmojiMessage.class);
      case "AvatarSelected" -> context.deserialize(jsonObject, AvatarSelected.class);
      default -> throw new JsonParseException("Unknown message type: " + messageType);
    };
  }
}
