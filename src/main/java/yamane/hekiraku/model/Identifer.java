/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class Identifer implements Serializable {

  private String uri;
  private String cid;
  
  public Identifer(JsonObject json) {
    this.uri = getStr(json.get("uri"));
    this.cid = getStr(json.get("cid"));
  }
  
  public String getUri() {
    return uri;
  }
  
  public String getCid() {
    return cid;
  }
  
  public String jsonString() {
    return toJson(this);
  }
}
