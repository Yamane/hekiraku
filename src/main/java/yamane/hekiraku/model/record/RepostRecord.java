/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.record;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.model.Identifer;

public class RepostRecord extends AbstractRecord {

  private Identifer subject;

  @Override
  protected String typeName() {
    return "app.bsky.feed.repost";
  }
  
  public RepostRecord(Identifer id) {
    this.subject = id;
  }
  
  public RepostRecord(JsonObject json) throws BskyException {
    super(json);
    this.subject = new Identifer(json.getAsJsonObject("subject"));
  }

  public Identifer getSubject() {
    return subject;
  }


}
