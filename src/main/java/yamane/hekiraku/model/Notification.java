/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

import yamane.hekiraku.model.record.PostRecord;

public class Notification extends Identifer {

  private Actor author;
  private String reason;
  private String reasonSubject;
  private PostRecord record;
  private Boolean read;
  private String indexedAt;
  
  public Notification(JsonObject json) {
    super(json);
    this.author = new Actor(json.getAsJsonObject("author"));
    this.reason = getStr(json.get("reason"));
    this.reasonSubject = getStr(json.get("reasonSubject"));
    this.record = new PostRecord(json.getAsJsonObject("record"));
    this.read = getBoolean(json.get("isRead"));
    this.indexedAt = getStr(json.get("indexedAt"));
  }
  
  public Actor getAuthor() {
    return author;
  }

  public String getReason() {
    return reason;
  }

  public String getReasonSubject() {
    return reasonSubject;
  }

  public PostRecord getRecord() {
    return record;
  }

  public Boolean isRead() {
    return read;
  }

  public String getIndexedAt() {
    return indexedAt;
  }
  
  public String jsonString() {
    return toJson(this);
  }
}
