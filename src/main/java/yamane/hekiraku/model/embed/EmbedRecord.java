/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.embed;

import com.google.gson.JsonObject;

import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.PostView;

public class EmbedRecord extends AbstractEmbed {

  private Identifer record;
  
  @Override
  protected String typeName() {
    return "app.bsky.embed.record";
  }
  
  public EmbedRecord(PostView post) {
    super();
    this.record = post;
  }
  
  public EmbedRecord(JsonObject json) {
    super();
    if(json.get("record") != null) {
      this.record = new Identifer(json.getAsJsonObject("record"));
    }
  }
  
  public Identifer getRecord() {
    return record;
  }
}
