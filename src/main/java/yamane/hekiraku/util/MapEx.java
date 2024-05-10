/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.util;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class MapEx extends HashMap<String, Object> {
  
  public MapEx p(String key, Object value) {
    super.put(key, value);
    return this;
  }

  public String query() {
    StringBuilder b = new StringBuilder("?");
    for (Entry<String, Object> entry : entrySet()) {
      if(entry.getValue() != null) {
        if(b.length() >1) {
          b.append("&");
        }
        Object value = entry.getValue();
        if(value instanceof String[] array) {
          StringJoiner join = new StringJoiner("&");
          for (String str : array) {
            join.add(entry.getKey() + "=" + str);
          }
          b.append(join.toString());
        } else if(value instanceof List<?> list) {
          StringJoiner join = new StringJoiner("&");
          list.forEach(e -> join.add(entry.getKey() + "=" + e.toString()));
          b.append(join.toString());
        } else {
          b.append(entry.getKey()).append("=").append(entry.getValue());
        }
      }
    }
    return b.toString();
  }
}
