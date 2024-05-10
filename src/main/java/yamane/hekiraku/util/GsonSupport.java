/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.util;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.Hekiraku;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.UserList;
import yamane.hekiraku.model.record.PostRecord;

public class GsonSupport {

  private static Logger logger;
  private static Gson gson;

  static {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(PostRecord.class, PostRecord.SERIALIZER);
    builder.registerTypeAdapter(UserList.class, UserList.SERIALIZER);
    if(Hekiraku.isDebug()) {
      builder.setPrettyPrinting();
    }
    gson = builder.create();

    logger = Logger.getLogger(Hekiraku.class.getSimpleName());
  }
  
  private GsonSupport() {
  }
  
  public static JsonObject toJson(String json) {
    JsonObject obj = gson.fromJson(json, JsonObject.class);
    if(Hekiraku.isDebug()) {
      StringBuilder b = new StringBuilder("Response:");
      b.append(System.lineSeparator()).append(toJson(obj));
      logger.log(Level.INFO, b.toString());
    }
    return obj;
  }
  
  public static String toJson(Object obj) {
    if(Hekiraku.isDebug()) {
      try {
        StringWriter writer = new StringWriter();
        JsonWriter jw = gson.newJsonWriter(writer);
        jw.setIndent("    ");
        gson.toJson(obj,obj.getClass(), jw);
        return writer.toString();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return gson.toJson(obj);
  }
  
  public static <E> PageableList<E> toPageableList(String str, String listKey, Class<E> clazz) throws BskyException {
    JsonObject json = toJson(str);
    PageableList<E> result = getList(json.getAsJsonArray(listKey), clazz);
    result.setCursor(getStr(json.get("cursor")));
    return result;
  }
  
  public static byte[] toJsonByte(Map<?, ?> map) {
    return gson.toJson(map).getBytes(StandardCharsets.UTF_8);
  }
  
  public static String getStr(JsonElement element) {
    return element != null ? element.getAsString() : null;
  }
  
  public static Integer getInt(JsonElement element) {
    return element != null ? element.getAsInt() : null;
  }
  
  public static Boolean getBoolean(JsonElement element) {
    return element != null ? element.getAsBoolean() : null;
  }
  
  public static List<String> getList(JsonArray array) {
    List<String> list = new ArrayList<>();
    if(array != null) {
      array.forEach(a -> list.add(a.getAsString()));
    }
    return list;
  }
  
  public static JsonElement getPrimitive(String val) {
    return val != null ? new JsonPrimitive(val) : JsonNull.INSTANCE;
  }
  
  public static JsonElement getPrimitive(Number val) {
    return val != null ? new JsonPrimitive(val) : JsonNull.INSTANCE;
  }
  
  public static JsonElement getPrimitive(Boolean val) {
    return val != null ? new JsonPrimitive(val) : JsonNull.INSTANCE;
  }
  
  public static <E> PageableList<E> getList(JsonArray array, Class<E> clazz) throws BskyException {
    PageableList<E> list = new PageableList<>();
    if(array != null) {
      for (JsonElement e : array) {
        list.add(newInstance(e.getAsJsonObject(), clazz));
      }
    }
    return list;
  }
  
  public static <E> E newInstance(JsonObject json, Class<E> clazz) throws BskyException {
    try {
      return clazz.getDeclaredConstructor(JsonObject.class).newInstance(json);
    } catch (IllegalAccessException e) {
      throw new BskyException(GsonSupport.class.getSimpleName() + ".newInstance("+ clazz.getSimpleName() + ")", e);
    } catch (InstantiationException | NoSuchMethodException | IllegalArgumentException e) {
      throw new BskyException(GsonSupport.class.getSimpleName() + ".newInstance("+ clazz.getSimpleName() + ")", e);
    } catch (InvocationTargetException e) {
      throw new BskyException(GsonSupport.class.getSimpleName() + ".newInstance("+ clazz.getSimpleName() + ")", e);
    }
  }
}
