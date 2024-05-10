/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.record;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;

public class BlockRecord extends AbstractRecord {

  private String subject;

  @Override
  protected String typeName() {
    return "app.bsky.graph.block";
  }
  
  public BlockRecord(String did) {
    this.subject = did;
  }
  
  public BlockRecord(JsonObject json) throws BskyException {
    super(json);
    this.subject = getStr(json.get("subject"));
  }

  public String getSubject() {
    return subject;
  }


}
