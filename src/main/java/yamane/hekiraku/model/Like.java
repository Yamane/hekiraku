/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;

public class Like implements Serializable {

  private String indexedAt;
  private String createdAt;
  private Actor actor;
  
  public Like(JsonObject json) throws BskyException {
    this.indexedAt = getStr(json.get("indexedAt"));
    this.createdAt = getStr(json.get("createdAt"));
    this.actor = new Actor(json.getAsJsonObject("actor"));
  }
  
  public String getIndexedAt() {
    return indexedAt;
  }
  
  public String getCreatedAt() {
    return createdAt;
  }
  
  public Actor getActor() {
    return actor;
  }
  
  public String jsonString() {
    return toJson(this);
  }
}
