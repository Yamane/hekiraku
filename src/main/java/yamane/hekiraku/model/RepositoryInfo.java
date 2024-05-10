/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;

public class RepositoryInfo implements Serializable {

  private String handle;
  private String did;
  private DidDoc didDoc;
  private List<String> collections;
  private Boolean handleIsCorrect;
  
  public RepositoryInfo(JsonObject json) throws BskyException {
    this.handle = getStr(json.get("handle"));
    this.did = getStr(json.get("did"));
    this.didDoc = new DidDoc(json.getAsJsonObject("didDoc"));
    this.collections = getList(json.getAsJsonArray("collections"));
    this.handleIsCorrect = getBoolean(json.get("handleIsCorrect"));
  }

  public String getHandle() {
    return handle;
  }

  public String getDid() {
    return did;
  }

  public DidDoc getDidDoc() {
    return didDoc;
  }

  public List<String> getCollections() {
    return collections;
  }

  public Boolean getHandleIsCorrect() {
    return handleIsCorrect;
  }
  
  public String jsonString() {
    return toJson(this);
  }
}
