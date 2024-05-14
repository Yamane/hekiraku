/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class Reply implements Serializable {

  private Identifer root;
  private Identifer parent;
  
  public Reply(Identifer id) {
    setParent(id);
    setRoot(id);
    if(id instanceof PostView post) {
      if(post.getRecord().getReply() != null) {
        setRoot(post.getRecord().getReply().getRoot());
      }
    }
    if(id instanceof Notification noti) {
      if(noti.getRecord().getReply() != null) {
        setRoot(noti.getRecord().getReply().getRoot());
      }
    }
  }
  
  public Reply(JsonObject json) {
    this.root = new PostView(json.getAsJsonObject("root"));
    this.parent = new PostView(json.getAsJsonObject("parent"));
  }
  
  public Identifer getRoot() {
    return root;
  }
  
  public void setRoot(Identifer root) {
    this.root = root;
  }
  
  public Identifer getParent() {
    return parent;
  }
  
  public void setParent(Identifer parent) {
    this.parent = parent;
  }
}
