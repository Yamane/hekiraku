/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.ritchtext;

import static yamane.hekiraku.util.GsonSupport.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import yamane.hekiraku.model.TypedElement;
import yamane.hekiraku.util.MapEx;

public class Facet extends TypedElement {

  public static enum FacetType {
    MENTION, LINK, TAG
  };
  
  @Override
  protected String typeName() {
    return "app.bsky.richtext.facet";
  }

  private Map<String, Integer> index;
  private List<Map<String, Object>> features;
  
  public Facet() {
    this.index = new HashMap<>();
    this.features = new ArrayList<>();
  }
  
  public Facet(JsonObject json) {
    this();
    JsonObject index = json.getAsJsonObject("index");
    this.index.put("byteStart", getInt(index.get("byteStart")));
    this.index.put("byteEnd", getInt(index.get("byteEnd")));
    JsonArray features = json.getAsJsonArray("features");
    features.forEach(e -> {
      String type = getStr(e.getAsJsonObject().get("$type"));
      if(type != null) {
        MapEx feature = new MapEx();
        feature.p("$type", type);
        if(type.endsWith("link")) {
          feature.p("uri", e.getAsJsonObject().get("uri"));
        }
        if(type.endsWith("mention")) {
          feature.p("did", e.getAsJsonObject().get("did"));
        }
        if(type.endsWith("tag")) {
          feature.p("tag", e.getAsJsonObject().get("tag"));
        }
        this.features.add(feature);
      }
    });
  }
  
  public Facet(FacetType type, String value) {
    this();
    MapEx feature = new MapEx();
    getFeatures().add(feature.p("$type", getType() + "#" + type.name().toLowerCase()));
    if(type == FacetType.LINK) {
      feature.p("uri", !value.startsWith("http") ? "https://" + value : value);
    }
    if(type == FacetType.MENTION) {
      feature.p("handle", value);
    }
    if(type == FacetType.TAG) {
      feature.p("tag", value);
    }
  }

  public Map<String, Integer> getIndex() {
    return index;
  }

  public List<Map<String, Object>> getFeatures() {
    return features;
  }
}
