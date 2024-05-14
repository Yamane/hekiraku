/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonObject;

import yamane.hekiraku.model.LabeledElement.Label;

public class Actor implements Serializable {

  private String did;
  private String handle;
  private String displayName;
  private String avatar;
  private String description;
  private String banner;
  private Integer followersCount;
  private Integer followsCount;
  private Integer postsCount;
  private String indexedAt;
  private Associated associated;
  private Viewer viewer;
  private List<Label> labels;
  
  public Actor(JsonObject json) {
    this.did = getStr(json.get("did"));
    this.handle = getStr(json.get("handle"));
    this.displayName = getStr(json.get("displayName"));
    this.avatar = getStr(json.get("avatar"));
    this.description = getStr(json.get("description"));
    this.banner = getStr(json.get("banner"));
    this.followersCount = getInt(json.get("followersCount"));
    this.followsCount = getInt(json.get("followsCount"));
    this.postsCount = getInt(json.get("postsCount"));
    this.indexedAt = getStr(json.get("indexedAt"));
    this.associated = json.get("associated") != null ? new Associated(json.getAsJsonObject("associated")) : null;
    this.viewer = json.get("viewer") != null ? new Viewer(json.getAsJsonObject("viewer")) : null;
    this.labels = getList(json.getAsJsonArray("labels"), Label.class);
  }

  public String getDid() {
    return did;
  }

  public String getHandle() {
    return handle;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getAvatar() {
    return avatar;
  }

  public String getDescription() {
    return description;
  }

  public String getBanner() {
    return banner;
  }

  public Integer getFollowersCount() {
    return followersCount;
  }

  public Integer getFollowsCount() {
    return followsCount;
  }

  public Integer getPostsCount() {
    return postsCount;
  }
  
  public String getIndexedAt() {
    return indexedAt;
  }
  
  public Associated getAssociated() {
    return associated;
  }
  
  public Viewer getViewer() {
    return viewer;
  }
  
  public List<Label> getLabels() {
    return labels;
  }
  
  public String jsonString() {
    return toJson(this);
  }
  
  public static class Associated implements Serializable {

    private Integer lists;
    private Integer feedgens;
    private Boolean labeler;
    
    public Associated(JsonObject json) {
      this.lists = getInt(json.get("lists"));
      this.feedgens = getInt(json.get("feedgens"));
      this.labeler = getBoolean(json.get("labeler"));
    }

    public Integer getLists() {
      return lists;
    }

    public Integer getFeedgens() {
      return feedgens;
    }

    public Boolean getLabeler() {
      return labeler;
    }
  }
}
