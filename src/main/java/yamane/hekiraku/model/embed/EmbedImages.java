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

public class EmbedImages extends AbstractEmbed {

  private List<Image> images;
  
  @Override
  protected String typeName() {
    return "app.bsky.embed.images";
  }
  
  public EmbedImages() {
    super();
    this.images = new ArrayList<Image>();
  }
  
  public EmbedImages(JsonObject json) {
    super();
    if(json.get("images") != null) {
      this.images = getList(json.getAsJsonArray("images"), Image.class);
    }
  }
  
  public void addImage(BlobLink blob, String alt) {
    this.images.add(new Image(blob, alt));
  }
  
  public List<Image> getImages() {
    return images;
  }
  
  public static class Image implements Serializable {
    
    private String alt;
    private BlobLink image;
    private Map<String, Integer> aspectRatio;
    
    public Image(BlobLink blob, String alt) {
      this.alt = alt;
      this.image = blob;
    }
    
    public Image(JsonObject json) {
      this.alt = getStr(json.get("alt"));
      this.image = new BlobLink(json.getAsJsonObject("image"));
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
    
    public Map<String, Integer> getAspectRatio() {
      return aspectRatio;
    }

    public BlobLink getImage() {
      return image;
    }
  }
}
