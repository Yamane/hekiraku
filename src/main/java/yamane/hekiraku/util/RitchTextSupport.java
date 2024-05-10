/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yamane.hekiraku.model.ritchtext.Facet;
import yamane.hekiraku.model.ritchtext.Facet.FacetType;

/**
 * ポストするテキストからリンク、メンション、タグを検知して必要なデータを構築する処理です.
 * 以下のTypeScriptのコードを参考に記述しました.
 * https://github.com/bluesky-social/atproto/blob/main/packages/api/src/rich-text/detection.ts
 * https://github.com/bluesky-social/atproto/blob/main/packages/api/src/rich-text/util.ts
 * ※リンクに関してはWebの挙動を参考にして、記述内容を省略する処理を追加しています.
 */
public class RitchTextSupport {

  private static Charset utf8 = Charset.forName("UTF8");
  
  private static Pattern MENTION = Pattern.compile("(^|\\s|\\()(@)([a-zA-Z0-9.-]+)(\\b)");
  private static Pattern URL = Pattern.compile("(?i)(?m)(^|\\s|\\()((https?:\\/\\/[\\S]+)|((?<domain>[a-z][a-z0-9]*(\\.[a-z0-9]+)+)[\\S]*))");
  private static Pattern TAG = Pattern.compile("(?u)(^|\\s)[#＃]((?!\\ufe0f)[^\\s\\u00AD\\u2060\\u200A\\u200B\\u200C\\u200D\\u20e2]*[^\\d\\s\\p{Punct}\\u00AD\\u2060\\u200A\\u200B\\u200C\\u200D\\u20e2]+[^\\s\\u00AD\\u2060\\u200A\\u200B\\u200C\\u200D\\u20e2]*)?");
  private static String TRAILING_PUNCTUATION = "(?u)\\p{Punct}+$";
  
  public static FacetList parse(String text) {
    FacetList facets = new FacetList();

    // リンク
    int current = 0;
    StringBuilder b = new StringBuilder();
    Matcher matcher = URL.matcher(text);
    while(matcher.find()) {
      String uri = matcher.group(2);
      // 末尾の不要な文字を削除
      if(Pattern.matches(".*[.,;:!?]$", uri)) {
        uri = uri.substring(current, uri.length() - 1);
      }
      // 閉じカッコ
      boolean closeDeleteFlag = false;
      if(Pattern.matches(".*[)]$", uri) && uri.indexOf('(') == -1) {
        uri = uri.substring(0, uri.length() - 1);
        closeDeleteFlag = true;
      }
      // いちど有効なURIを退避
      Facet facet = new Facet(FacetType.LINK, uri);
      // プロトコル部分の削除
      uri = uri.replaceAll("(https?://)", "");
      // 30文字以上は省略
      if(uri.length() > 30) {
        uri = uri.substring(0, 27) + "...";
      }
      b.append(text.substring(current, matcher.start(2)));
      //開始位置と終了位置
      int start = b.toString().getBytes(utf8).length;
      int end = start + uri.getBytes(utf8).length;
      facet.getIndex().put("byteStart", start);
      facet.getIndex().put("byteEnd", end);
      facets.add(facet);
      b.append(uri);
      // 閉じカッコを復元
      if(closeDeleteFlag) {
        b.append(")");
      }
      current = matcher.end(2);
    }
    if(current > 0) {
      b.append(text.substring(current));
      text = b.toString();
    }
    
    // メンション
    matcher = MENTION.matcher(text);
    while(matcher.find()) {
      String mention = matcher.group(3);
      // Handleを格納（あとでDIDにする）
      Facet facet = new Facet(FacetType.MENTION, mention);
      //開始位置と終了位置
      int start = text.substring(0, matcher.start(3)).getBytes(utf8).length - 1;
      facet.getIndex().put("byteStart", start);
      facet.getIndex().put("byteEnd", start + mention.getBytes(utf8).length + 1);
      facets.add(facet);
    }
    
    // タグ
    matcher = TAG.matcher(text);
    while(matcher.find()) {
      if(matcher.group(2) == null) {
        continue;
      }
      // 末尾の不要な文字を削除
      String tag = matcher.group(2).trim().replaceAll(TRAILING_PUNCTUATION, "");
      if(tag.length() == 0 || tag.length() > 64) {
        continue;
      }
      Facet facet = new Facet(FacetType.TAG, tag);
      //開始位置と終了位置
      int start = text.substring(0, matcher.start(2)).getBytes(utf8).length - 1;
      facet.getIndex().put("byteStart", start);
      facet.getIndex().put("byteEnd", start + tag.getBytes(utf8).length + 1);
      facets.add(facet);
    }
    
    facets.deformdText = text;
    return facets;
  }
  
  public static class FacetList extends ArrayList<Facet> {

    private String deformdText;
    
    public FacetList() {
      super();
    }

    public String getDeformdText() {
      return deformdText;
    }
  }
}
