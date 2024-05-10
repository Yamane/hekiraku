/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public abstract class TypedElement implements Serializable {

  @SerializedName("$type")
  private String type;
  
  protected abstract String typeName();
  
  protected TypedElement() {
    this.type = typeName();
  }
  
  protected TypedElement(JsonObject json) {
    this();
  }

  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public String jsonString() {
    return toJson(this);
  }
}
