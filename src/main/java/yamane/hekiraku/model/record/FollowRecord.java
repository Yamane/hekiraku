/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.record;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

public class FollowRecord extends AbstractRecord {

  private String subject;

  @Override
  protected String typeName() {
    return "app.bsky.graph.follow";
  }
  
  public FollowRecord(String did) {
    this.subject = did;
  }
  
  public FollowRecord(JsonObject json) {
    super(json);
    this.subject = getStr(json.get("subject"));
  }

  public String getSubject() {
    return subject;
  }


}
