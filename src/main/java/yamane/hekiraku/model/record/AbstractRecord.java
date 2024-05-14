/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.record;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

import yamane.hekiraku.model.TypedElement;

public abstract class AbstractRecord extends TypedElement {
  
  private String createdAt;
  
  protected AbstractRecord() {
    super();
  }
  
  protected AbstractRecord(JsonObject json) {
    super();
    this.createdAt = getStr(json.get("createdAt"));
  }
  
  public static AbstractRecord create(JsonObject json) {
    String type = getStr(json.get("$type"));
    if(type.equals("app.bsky.feed.post")) {
      return new PostRecord(json);
    }
    if(type.equals("app.bsky.feed.repost")) {
      return new RepostRecord(json);
    }
    return new AbstractRecord() {
      @Override
      protected String typeName() {
        return type;
      }
    };
  }
  
  public String getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }
}
