/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.embed;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class EmbedExternal extends AbstractEmbed {

  private External external = new External();

  @Override
  protected String typeName() {
    return "app.bsky.embed.external";
  }
  
  public void setTitle(String title) {
    this.external.title = title;
  }
  
  public void setUri(String uri) {
    this.external.uri = uri;
  }
  
  public void setDescription(String description) {
    this.external.description = description;
  }
  
  public void setThumb(BlobLink thumb) {
    this.external.thumb = thumb;
  }
  
  public EmbedExternal() {
    super();
  }

  public EmbedExternal(JsonObject json) {
    super();
    if (json.get("external") != null) {
      this.external = new External(json.getAsJsonObject("external"));
    }
  }
  
  public External getExternal() {
    return external;
  }

  public class External implements Serializable {
    private String title;
    private String uri;
    private String description;
    private BlobLink thumb;

    public External() {
    }

    public External(JsonObject json) {
      this.title = getStr(json.get("title"));
      this.uri = getStr(json.get("uri"));
      this.description = getStr(json.get("description"));
      if(json.get("thumb") != null) {
        this.thumb = new BlobLink(json.getAsJsonObject("thumb"));
      }
    }

    public String getTitle() {
      return title;
    }

    public External getExternal() {
      return external;
    }

    public String getUri() {
      return uri;
    }

    public String getDescription() {
      return description;
    }

    public BlobLink getThumb() {
      return thumb;
    }
  }
}
