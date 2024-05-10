/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.util.ArrayList;

public class PageableList<E> extends ArrayList<E> {

  private String uri;
  private String cid;
  private String cursor;
  private String filter;
  private String seenAt;
  private Integer hitsTotal;

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public String getCursor() {
    return cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public String getSeenAt() {
    return seenAt;
  }

  public void setSeenAt(String seenAt) {
    this.seenAt = seenAt;
  }
  
  public Integer getHitsTotal() {
    return hitsTotal;
  }

  public void setHitsTotal(Integer hitsTotal) {
    this.hitsTotal = hitsTotal;
  }

  public String jsonString() {
    return toJson(this);
  }
}
