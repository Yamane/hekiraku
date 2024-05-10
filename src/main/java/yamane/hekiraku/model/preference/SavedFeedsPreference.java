/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.preference;

import static yamane.hekiraku.util.GsonSupport.*;

import java.util.List;

import com.google.gson.JsonObject;

public class SavedFeedsPreference extends AbstractPreference {

  private List<String> saved;
  private List<String> pinned;
  
  @Override
  protected String typeName() {
    return "app.bsky.actor.defs#savedFeedsPref";
  }

  public SavedFeedsPreference(JsonObject json) {
    super();
    this.saved = getList(json.getAsJsonArray("saved"));
    this.pinned = getList(json.getAsJsonArray("pinned"));
  }

  public List<String> getSaved() {
    return saved;
  }

  public List<String> getPinned() {
    return pinned;
  }
}
