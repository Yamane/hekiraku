/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.record;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

import yamane.hekiraku.model.Identifer;

public class IdentiferRecord<T extends AbstractRecord> extends Identifer {

  private T value;
  
  public IdentiferRecord(JsonObject json, Class<T> clazz) {
    super(json);
    this.value = newInstance(json.getAsJsonObject("value"), clazz);
  }

  public T getValue() {
    return value;
  }
}
