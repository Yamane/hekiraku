/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.preference;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

public class ContentLabelPreference extends AbstractPreference {

  private String label;
  private String visibility;
  
  @Override
  protected String typeName() {
    return "app.bsky.actor.defs#contentLabelPref";
  }

  public ContentLabelPreference(JsonObject json) {
    super();
    this.label = getStr(json.get("label"));
    this.visibility = getStr(json.get("visibility"));
  }

  public String getLabel() {
    return label;
  }

  public String getVisibility() {
    return visibility;
  }
}
