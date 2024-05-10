/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.model.ritchtext.Facet;
import yamane.hekiraku.model.ritchtext.RitchText;

public class UserList extends LabeledElement {

  public static JsonSerializer<UserList> SERIALIZER = (in, type, context) -> {
    JsonObject obj = new JsonObject();
    obj.add("uri", getPrimitive(in.getUri()));
    obj.add("cid", getPrimitive(in.getCid()));
    obj.add("creator", context.serialize(in.getCreator()));
    obj.add("name", getPrimitive(in.getName()));
    obj.add("purpose", getPrimitive(in.getPurpose()));
    obj.add("description", getPrimitive(in.getDescription()));
    obj.add("descriptionFacets", context.serialize(in.getDescriptionFacets()));
    obj.add("avater", getPrimitive(in.getAvatar()));
    obj.add("labels", context.serialize(in.getLabels()));
    obj.add("viewer", context.serialize(in.getViewer()));
    obj.add("indexedAt", getPrimitive(in.getIndexedAt()));
    return obj;
  };
  
  private String name;
  private Actor creator;
  private String purpose;
  private RitchText description;
  private String avatar;
  private String indexedAt;

  public UserList(JsonObject json) throws BskyException {
    super(json);
    this.name = getStr(json.get("name"));
    this.creator = new Actor(json.getAsJsonObject("creator"));
    this.purpose = getStr(json.get("purpose"));
    this.description = new RitchText("description", json);
    this.avatar = getStr(json.get("avatar"));
    this.indexedAt = getStr(json.get("indexedAt"));
  }

  public String getName() {
    return name;
  }

  public Actor getCreator() {
    return creator;
  }

  public String getPurpose() {
    return purpose;
  }

  public String getDescription() {
    return description.getText();
  }

  public List<Facet> getDescriptionFacets() {
    return description.getFacets();
  }

  public String getAvatar() {
    return avatar;
  }

  public String getIndexedAt() {
    return indexedAt;
  }
}
