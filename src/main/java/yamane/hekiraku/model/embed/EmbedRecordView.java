/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.embed;

import com.google.gson.JsonObject;

import yamane.hekiraku.model.Identifer;

public class EmbedRecordView extends AbstractEmbed {

  private IdentiferEmbedRecordView record;
  
  @Override
  protected String typeName() {
    return "app.bsky.embed.record#view";
  }
  
  public EmbedRecordView(JsonObject json) {
    super();
    if(json.get("record") != null) {
      this.record = new IdentiferEmbedRecordView(json.getAsJsonObject("record"));
    }
  }
  
  public Identifer getRecord() {
    return record;
  }
}
