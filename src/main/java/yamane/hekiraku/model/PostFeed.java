/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class PostFeed implements Serializable {

  private PostView post;
  private Reply reply;
  private Reason reason;
  private String feedContext;
  
  public PostFeed(JsonObject json) {
    this.post = new PostView(json.getAsJsonObject("post"));
    if(json.get("reply") != null) {
      this.reply = new Reply(json.getAsJsonObject("reply"));
    }
    if(json.get("reason") != null) {
      this.reason = new Reason(json.getAsJsonObject("reason"));
    }
    this.feedContext = getStr(json.get("feedContext"));
  }
  
  public PostView getPost() {
    return post;
  }

  public Reply getReply() {
    return reply;
  }

  public Reason getReason() {
    return reason;
  }

  public String getFeedContext() {
    return feedContext;
  }

  public String jsonString() {
    return toJson(this);
  }
  
  public static class Reason extends TypedElement {

    private Actor by;
    private String indexedAt;
    
    @Override
    protected String typeName() {
      return "app.bsky.feed.defs#reasonRepost";
    }
    
    public Reason(JsonObject json) {
      this.by = new Actor(json.getAsJsonObject("by"));
      this.indexedAt = getStr(json.get("indexedAt"));
    }

    public Actor getBy() {
      return by;
    }
    
    public String getIndexedAt() {
      return indexedAt;
    }
  }
}
