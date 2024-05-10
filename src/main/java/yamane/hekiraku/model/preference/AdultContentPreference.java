/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.preference;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

public class AdultContentPreference extends AbstractPreference {

  private Boolean enabled;
  
  @Override
  protected String typeName() {
    return "app.bsky.actor.defs#adultContentPref";
  }

  public AdultContentPreference(JsonObject json) {
    super();
    this.enabled = getBoolean(json.get("enabled"));
  }

  public Boolean getEnabled() {
    return enabled;
  }
}
