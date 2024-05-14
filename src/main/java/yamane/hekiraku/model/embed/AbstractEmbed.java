/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.embed;

import static yamane.hekiraku.util.GsonSupport.*;

import com.google.gson.JsonObject;

import yamane.hekiraku.model.TypedElement;

public abstract class AbstractEmbed extends TypedElement {

  protected AbstractEmbed() {
    super();
  }
  
  public static AbstractEmbed create(JsonObject json) {
    String type = getStr(json.get("$type"));
    if(type.equals("app.bsky.embed.external")) {
      return new EmbedExternal(json);
    }
    if(type.equals("app.bsky.embed.record")) {
      return new EmbedRecord(json);
    }
    if(type.equals("app.bsky.embed.images")) {
      return new EmbedImages(json);
    }
    if(type.equals("app.bsky.embed.images#view")) {
      return new EmbedImagesView(json);
    }
    if(type.equals("app.bsky.embed.record#view")) {
      return new EmbedRecordView(json);
    }
    return new AbstractEmbed() {
      @Override
      protected String typeName() {
        return type;
      }
    };
  }
}
