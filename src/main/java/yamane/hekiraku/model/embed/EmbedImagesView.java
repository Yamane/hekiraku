/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.embed;

import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

public class EmbedImagesView extends AbstractEmbed {

  private List<Image> images;
  
  @Override
  protected String typeName() {
    return "app.bsky.embed.images#view";
  }
  
  public EmbedImagesView() {
    super();
    this.images = new ArrayList<Image>();
  }
  
  public EmbedImagesView(JsonObject json) {
    super();
    if(json.get("images") != null) {
      this.images = getList(json.getAsJsonArray("images"), Image.class);
    }
  }
  
  public List<Image> getImages() {
    return images;
  }
  
  public static class Image implements Serializable {
    
    private String alt;
    private String thumb;
    private String fullsize;
    private Map<String, Integer> aspectRatio;
    
    public Image(JsonObject json) {
      this.alt = getStr(json.get("alt"));
      this.thumb = getStr(json.get("thumb"));
      this.fullsize = getStr(json.get("fullsize"));
      if(json.get("aspectRatio") != null) {
        JsonObject ratio = json.getAsJsonObject("aspectRatio");
        aspectRatio = new HashMap<>();
        aspectRatio.put("height", getInt(ratio.get("height")));
        aspectRatio.put("width", getInt(ratio.get("width")));
      }
    }

    public String getAlt() {
      return alt;
    }
    
    public String getThumb() {
      return thumb;
    }

    public String getFullsize() {
      return fullsize;
    }

    public Map<String, Integer> getAspectRatio() {
      return aspectRatio;
    }

  }
}
