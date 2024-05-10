/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.util;

import static yamane.hekiraku.Hekiraku.*;
import static yamane.hekiraku.util.GsonSupport.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.Hekiraku;

public class HttpSupport {

  private static HttpClient httpClient;
  static {
    httpClient = HttpClient.newHttpClient();
  }

  /**
   * GETリクエストを実施します.
   * @param name 処理名
   * @param jwt 認証トークン
   * @param params URLパラメータ
   * @return HTTP処理結果
   * @throws BskyException
   */
  public static String httpGet(String name, String jwt, MapEx params) throws BskyException {
    URI uri = uri(name, params);
    if (Hekiraku.isDebug()) {
      log(Level.INFO, "Get:" + uri.toString());
    }
    Builder builder = HttpRequest.newBuilder(uri);
    return send(name, setHeaders(builder.GET(), jwt));
  }

  /**
   * POSTリクエストを実施します.
   * @param name 処理名
   * @param jwt 認証トークン
   * @param body リクエストボディ
   * @return HTTP処理結果
   * @throws BskyException BlueskyAPIが例外を通知した場合
   */
  public static String httpPost(String name, String jwt, Map<String, Object> body) throws BskyException {
    URI uri = uri(name, null);
    Builder builder = HttpRequest.newBuilder(uri);
    if (!body.isEmpty()) {
      String json = toJson(body);
      if (Hekiraku.isDebug()) {
        StringBuilder b = new StringBuilder("Post:");
        b.append(uri.toString());
        b.append(System.lineSeparator()).append("Body:");
        b.append(System.lineSeparator()).append(json);
        log(Level.INFO, b.toString());
      }
      builder.POST(HttpRequest.BodyPublishers.ofString(json));
    } else {
      if (Hekiraku.isDebug()) {
        log(Level.INFO, "Post:" + uri.toString());
      }
      builder.POST(HttpRequest.BodyPublishers.noBody());
    }
    return send(name, setHeaders(builder, jwt));
  }

  /**
   * ファイルを添付するPOSTリクエストを実施します.
   * @param name 処理名
   * @param jwt 認証トークン
   * @param file 添付するファイル
   * @return HTTP処理結果
   * @throws BskyException BlueskyAPIが例外を通知した場合
   */
  public static String httpPost(String name, String jwt, File file) throws BskyException {
    String type = null;
    byte[] blob = null;
    try (ImageInputStream is = ImageIO.createImageInputStream(file)) {
      for (Iterator<ImageReader> i = ImageIO.getImageReaders(is); i.hasNext();) {
        ImageReader reader = i.next();
        type = reader.getFormatName().toLowerCase();
      }
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];
      while (true) {
        int len = is.read(buf);
        if (len < 0) {
          break;
        }
        stream.write(buf, 0, len);
      }
      blob = stream.toByteArray();
    } catch (IOException e) {
      throw new BskyException(GsonSupport.class.getSimpleName() + ".httpPost(BLOB ver)", e);
    }
    URI uri = uri(name, null);
    Builder builder = HttpRequest.newBuilder(uri);
    builder.POST(HttpRequest.BodyPublishers.ofByteArray(blob));
    return send(name, setHeaders(builder, jwt, "image/" + type));

  }

  /**
   * HTTPリクエストのURIを生成します.
   * @param name 処理名
   * @param params URLパラメータ
   * @return HTTPリクエストのURI
   */
  private static URI uri(String name, MapEx params) {
    StringBuilder b = new StringBuilder(get("social_url"));
    b.append("xrpc").append("/");
    b.append(name);
    if (params != null) {
      b.append(params.query());
    }
    return URI.create(b.toString());
  }

  /**
   * HTTPリクエストヘッダを設定します.
   * @param builder HTTPリクエストのビルダー
   * @param jwt 認証トークン
   * @return HTTPリクエストのビルダー
   */
  private static Builder setHeaders(Builder builder, String jwt) {
    return setHeaders(builder, jwt, "application/json");
  }

  /**
   * HTTPリクエストヘッダを設定します.
   * @param builder HTTPリクエストのビルダー
   * @param jwt 認証トークン
   * @param contentType Bodyのデータ型
   * @return HTTPリクエストのビルダー
   */
  private static Builder setHeaders(Builder builder, String jwt, String contentType) {
    builder.setHeader("Content-Type", contentType);
    builder.setHeader("Accept", "application/json");
    if (jwt != null) {
      builder.setHeader("Authorization", "Bearer " + jwt);
    }
    return builder;
  }

  /**
   * HTTPリクエストを実施し、結果を取得します.
   * @param name 処理名
   * @param builder HTTPリクエストのビルダー
   * @return HTTP処理結果
   * @throws BskyException HTTPまたはBlueskyAPIが例外を通知した場合
   */
  private static String send(String name, Builder builder) throws BskyException {
    HttpResponse<String> res;
    try {
      res = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
      if (res.statusCode() != 200) {
        throw new BskyException(name, res);
      }
    } catch (IOException | InterruptedException e) {
      throw new BskyException(name, e);
    }
    return res.body();
  }
}
