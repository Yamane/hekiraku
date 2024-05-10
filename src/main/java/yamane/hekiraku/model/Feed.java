/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.model.ritchtext.Facet;
import yamane.hekiraku.util.RitchTextSupport.FacetList;

public class Feed extends LabeledElement {

  private String did;
  private Actor creator;
  private String displayName;
  private String description;
  private FacetList descriptionFacets;
  private String avatar;
  private Integer likeCount;
  private Boolean acceptsInteractions;
  private String indexedAt;

  public Feed(JsonObject json) throws BskyException {
    super(json);
    this.did = getStr(json.get("did"));
    this.creator = new Actor(json.getAsJsonObject("creator"));
    this.displayName = getStr(json.get("displayName"));
    this.description = getStr(json.get("description"));
    if(json.get("descriptionFacets") != null) {
      this.descriptionFacets = new FacetList();
      json.getAsJsonArray("descriptionFacets").forEach(e -> {
        this.descriptionFacets.add(new Facet(e.getAsJsonObject()));
      });
    }
    this.avatar = getStr(json.get("avatar"));
    this.likeCount = getInt(json.get("likeCount"));
    this.acceptsInteractions = getBoolean(json.get("acceptsInteractions"));
    this.indexedAt = getStr(json.get("indexedAt"));
  }

  public String getDid() {
    return did;
  }

  public Actor getCreator() {
    return creator;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getDescription() {
    return description;
  }

  public FacetList getDescriptionFacets() {
    return descriptionFacets;
  }

  public String getAvatar() {
    return avatar;
  }

  public Integer getLikeCount() {
    return likeCount;
  }

  public Boolean getAcceptsInteractions() {
    return acceptsInteractions;
  }

  public String getIndexedAt() {
    return indexedAt;
  }
  
  public String jsonString() {
    return toJson(this);
  }
}
