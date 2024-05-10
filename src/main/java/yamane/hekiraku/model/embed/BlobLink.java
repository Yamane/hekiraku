/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.embed;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class BlobLink implements Serializable {

  @SerializedName("$type")
  private String type;
  private String mimeType;
  private Integer size;
  private Ref ref;
  
  public BlobLink(JsonObject json) {
    this.type = getStr(json.get("$type"));
    this.mimeType = getStr(json.get("mimeType"));
    this.size = getInt(json.get("size"));
    this.ref = new Ref(json.getAsJsonObject("ref"));
  }
  
  public String getType() {
    return type;
  }

  public String getMimeType() {
    return mimeType;
  }

  public Integer getSize() {
    return size;
  }

  public Ref getRef() {
    return ref;
  }

  public static class Ref implements Serializable {
    @SerializedName("$link")
    private String link;
    
    public Ref(JsonObject json) {
      this.link = getStr(json.get("$link"));
    }
    
    public String getLink() {
      return link;
    }
  }
  
  public String jsonString() {
    return toJson(this);
  }
}
