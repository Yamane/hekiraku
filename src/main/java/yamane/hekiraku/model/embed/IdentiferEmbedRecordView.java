/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.embed;

import static yamane.hekiraku.util.GsonSupport.*;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.LabeledElement.Label;
import yamane.hekiraku.model.record.AbstractRecord;

public class IdentiferEmbedRecordView extends Identifer {

  @SerializedName("$type")
  private String type;
  private Actor author;
  private AbstractRecord value;
  private List<Label> labels;
  private Integer likeCount;
  private Integer replyCount;
  private Integer repostCount;
  private String indexedAt;

  
  public IdentiferEmbedRecordView(JsonObject json) throws BskyException {
    super(json);
    this.type = "app.bsky.embed.record#viewRecord";
    this.author = new Actor(json.getAsJsonObject("author"));
    this.value = AbstractRecord.create(json.getAsJsonObject("value"));
    this.labels = getList(json.getAsJsonArray("labels"), Label.class);
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

  public AbstractRecord getValue() {
    return value;
  }
  
  public List<Label> getLabels() {
    return labels;
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
}
