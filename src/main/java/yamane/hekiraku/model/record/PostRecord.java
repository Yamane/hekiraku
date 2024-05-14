/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.model.record;

import static yamane.hekiraku.Hekiraku.*;
import static yamane.hekiraku.util.GsonSupport.*;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import yamane.hekiraku.model.Reply;
import yamane.hekiraku.model.embed.AbstractEmbed;
import yamane.hekiraku.model.ritchtext.Facet;
import yamane.hekiraku.model.ritchtext.RitchText;

public class PostRecord extends AbstractRecord {

  public static JsonSerializer<PostRecord> SERIALIZER = (in, type, context) -> {
    JsonObject obj = new JsonObject();
    obj.add("$type", getPrimitive(in.getType()));
    obj.add("text", getPrimitive(in.getText()));
    obj.add("facets", context.serialize(in.getFacets()));
    obj.add("embed", context.serialize(in.getEmbed()));
    obj.add("reply", context.serialize(in.getReply()));
    obj.add("langs", context.serialize(in.getLangs()));
    obj.add("createdAt", getPrimitive(in.getCreatedAt()));
    return obj;
  };
  
  private RitchText ritchText;
  private AbstractEmbed embed;
  private Reply reply;
  private List<String> langs;
  
  @Override
  protected String typeName() {
    return "app.bsky.feed.post";
  }
  
  public PostRecord(String text) {
    super();
    this.ritchText = RitchText.create(text);
    this.langs = Arrays.asList(get("langs").split(","));
  }
  
  public PostRecord(JsonObject json) {
    super(json);
    this.ritchText = new RitchText(json);
    if(json.get("embed") != null) {
      this.embed = AbstractEmbed.create(json.getAsJsonObject("embed"));
    }
    if(json.get("reply") != null) {
      this.reply = new Reply(json.getAsJsonObject("reply"));
    }
    this.langs = getList(json.getAsJsonArray("langs"));
  }
  
  public String getText() {
    return ritchText.getText();
  }
  
  public List<Facet> getFacets() {
    return ritchText.getFacets();
  }
  
  public RitchText getRitchText() {
    return ritchText;
  }

  public void setRitchText(RitchText ritchText) {
    this.ritchText = ritchText;
  }

  public AbstractEmbed getEmbed() {
    return embed;
  }
  
  public void setEmbed(AbstractEmbed embed) {
    this.embed = embed;
  }
  
  public Reply getReply() {
    return reply;
  }

  public void setReply(Reply reply) {
    this.reply = reply;
  }
  
  public List<String> getLangs() {
    return langs;
  }
}
