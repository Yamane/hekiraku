/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.preference;

import static yamane.hekiraku.util.GsonSupport.*;

import java.util.List;

import com.google.gson.JsonObject;

public class IntersetsPreference extends AbstractPreference {

  private List<String> tags;
  
  @Override
  protected String typeName() {
    return "app.bsky.actor.defs#interestsPref";
  }

  public IntersetsPreference(JsonObject json) {
    super();
    this.tags = getList(json.getAsJsonArray("tags"));
  }

  public List<String> getTags() {
    return tags;
  }
}
