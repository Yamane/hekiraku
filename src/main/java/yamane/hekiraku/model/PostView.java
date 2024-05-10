/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.model.embed.AbstractEmbed;
import yamane.hekiraku.model.record.PostRecord;

public class PostView extends LabeledElement {

  @SerializedName("$type")
  private String type;
  
  private Actor author;
  private PostRecord record;
  private AbstractEmbed embed;
  private Integer replyCount;
  private Integer repostCount;
  private Integer likeCount;
  private String indexedAt;
  
  public PostView(JsonObject json) throws BskyException {
    super(json);
    this.type = getStr(json.get("$type"));
    if(json.get("author") != null) {
      this.author = new Actor(json.getAsJsonObject("author"));
    }
    if(json.get("record") != null) {
      this.record = new PostRecord(json.getAsJsonObject("record"));
    }
    if(json.get("embed") != null) {
      this.embed = AbstractEmbed.create(json.getAsJsonObject("embed"));
    }
    this.likeCount = getInt(json.get("likeCount"));
    this.replyCount = getInt(json.get("replyCount"));
    this.repostCount = getInt(json.get("repostCount"));
    this.indexedAt = getStr(json.get("indexedAt"));
  }
  
  public String getType() {
    return type;
  }
  
  public Actor getAuthor() {
    return author;
  }
  
  public PostRecord getRecord() {
    return record;
  }
  
  public AbstractEmbed getEmbed() {
    return embed;
  }

  public Integer getLikeCount() {
    return likeCount;
  }
  
  public Integer getReplyCount() {
    return replyCount;
  }
  
  public Integer getRepostCount() {
    return repostCount;
  }
  
  public String getIndexedAt() {
    return indexedAt;
  }

  public String jsonString() {
    return toJson(this);
  }
}
