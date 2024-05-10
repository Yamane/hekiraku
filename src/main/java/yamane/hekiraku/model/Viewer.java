/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class Viewer implements Serializable {
  
  private Boolean muted;
  private Boolean blockedBy;
  private String blocking;
  private String following;
  private String followedBy;
  private String like;

  public Viewer(JsonObject json) {
    this.muted = getBoolean(json.get("muted"));
    this.blockedBy = getBoolean(json.get("blockedBy"));
    this.blocking = getStr(json.get("blocking"));
    this.following = getStr(json.get("following"));
    this.followedBy = getStr(json.get("followedBy"));
    this.like = getStr(json.get("like"));
  }

  public Boolean isMuted() {
    return muted;
  }

  public Boolean isBlockedBy() {
    return blockedBy;
  }
  
  public String getBlocking() {
    return blocking;
  }

  public String getFollowing() {
    return following;
  }

  public String getFollowedBy() {
    return followedBy;
  }
  
  public String getLike() {
    return like;
  }
}
