package yamane.hekiraku.model.ritchtext;
/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.util.RitchTextSupport;
import yamane.hekiraku.util.RitchTextSupport.FacetList;

public class RitchText implements Serializable {

  private String text;
  private List<Facet> facets;
  
  public static RitchText create(String text) {
    return new RitchText(RitchTextSupport.parse(text));
  }
  
  private RitchText(FacetList facets) {
    this.text = facets.getDeformdText();
    this.facets = facets;
  }
  
  public RitchText(String prefixName, JsonObject json) throws BskyException {
    super();
    this.text = getStr(json.get(prefixName));
    this.facets = getList(json.getAsJsonArray(prefixName + "Facets"), Facet.class);
  }
  
  public RitchText(JsonObject json) throws BskyException {
    super();
    this.text = getStr(json.get("text"));
    this.facets = getList(json.getAsJsonArray("facets"), Facet.class);
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public List<Facet> getFacets() {
    return facets;
  }

  public void setFacets(FacetList facets) {
    this.facets = facets;
  }
  

}
