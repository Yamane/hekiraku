/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.preference;

import static yamane.hekiraku.util.GsonSupport.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import yamane.hekiraku.model.TypedElement;

public abstract class AbstractPreference extends TypedElement {

  protected AbstractPreference() {
    super();
  }
  
  public static List<AbstractPreference> createList(JsonArray array) {
    List<AbstractPreference> list = new ArrayList<>();
    for (JsonElement p : array) {
      AbstractPreference pref = create(p.getAsJsonObject());
      if(pref != null) {
        list.add(pref);
      }
    }
    return list;
  }

  public static AbstractPreference create(JsonObject json) {
    String type = getStr(json.get("$type"));
    if(type.equals("app.bsky.actor.defs#adultContentPref")) {
      return new AdultContentPreference(json);
    }
    if(type.equals("app.bsky.actor.defs#contentLabelPref")) {
      return new ContentLabelPreference(json);
    }
    if(type.equals("app.bsky.actor.defs#interestsPref")) {
      return new IntersetsPreference(json);
    }
    if(type.equals("app.bsky.actor.defs#savedFeedsPref")) {
      return new SavedFeedsPreference(json);
    }
    return new AbstractPreference() {
      @Override
      protected String typeName() {
        return type;
      }
    };
  }
}
