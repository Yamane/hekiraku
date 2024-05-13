/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku;

import static yamane.hekiraku.util.HttpSupport.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import yamane.hekiraku.util.LogFormatter;
import yamane.hekiraku.util.MapEx;

public class Hekiraku {

  private static Logger logger;
  private static SimpleDateFormat df;
  private static ResourceBundle bundle;
  
  static {
    bundle = ResourceBundle.getBundle("hekiraku");
    df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    logger = Logger.getLogger(Hekiraku.class.getSimpleName());
    LogFormatter.addRoot();
  }

  /**
   * Create an authentication session.
   * @param identifier Handle or other identifier supported by the server for the authenticating user.
   * @param password Application password.
   * @return Authentication session.
   * @throws BskyException If the API reports an exception.
   */
  public static Session createSession(String identifier, String password) throws BskyException {
    MapEx map = p("identifier", identifier).p("password", password);
    return new Session(httpPost("com.atproto.server.createSession", null, map));
  }
  
  /**
   * リソースバンドルから設定値を取得します.
   * @param key 参照キー
   * @return 設定値
   */
  public static String get(String key) {
    return bundle.getString(key);
  }

  /**
   * 値を設定した状態でMapを生成します.
   * @param key キー
   * @param value 値
   * @return 生成されたMap
   */
  public static MapEx p(String key, Object value) {
    return new MapEx().p(key, value);
  }
  
  /**
   * ページネーション機能を持つ処理を呼び出す際のクエリパラメータを生成します.
   * @param limit リミット
   * @param cursor カーソル（date-time string or integer）
   * @return クエリパラメータ
   */
  public static MapEx pageableParams(Integer limit, Object cursor) {
    if(limit == null) {
      limit = 50;
    } else if(limit > 100) {
      limit = 100;
    }
    return cursor != null ? p("limit", limit).p("cursor", cursor) : p("limit", limit);
  }
  
  /**
   * URIからRKEYを抽出します.
   * @param uri URI
   * @return RKEY
   */
  public static String uri2rkey(String uri) {
    if(uri.indexOf('/') != -1) {
      return  uri.substring(uri.lastIndexOf('/') + 1, uri.length());
    }
    return uri;
  }
  
  /**
   * UTC基準の現在日時文字列を生成します.
   * @return UTC基準の現在日時
   */
  public static String datetime(Date date) {
    return df.format(date);
  }
  
  /**
   * デバッグモードかどうかを判断します.
   * @return デバッグモードかどうか
   */
  public static boolean isDebug() {
    return get("debug").equals("true");
  }
  
  /**
   * ログを出力します.
   * @param logLevel ログレベル
   * @param message 出力内容
   */
  public static void log(Level logLevel, String message) {
    logger.log(logLevel, message);
  }
}
