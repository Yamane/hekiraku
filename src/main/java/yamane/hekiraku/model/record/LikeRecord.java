/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.record;

import com.google.gson.JsonObject;

import yamane.hekiraku.model.Identifer;

public class LikeRecord extends AbstractRecord {

  private Identifer subject;

  @Override
  protected String typeName() {
    return "app.bsky.feed.like";
  }
  
  public LikeRecord(Identifer id) {
    this.subject = id;
  }
  
  public LikeRecord(JsonObject json) {
    super(json);
    this.subject = new Identifer(json.getAsJsonObject("subject"));
  }

  public Identifer getSubject() {
    return subject;
  }


}
