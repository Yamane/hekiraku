/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku;

import static yamane.hekiraku.util.GsonSupport.*;

import java.net.http.HttpResponse;

import com.google.gson.JsonObject;

public class BskyException extends Exception {

  private String jsonError;
  private String jsonMessage;
  
  /**
   * Constructor.
   * @param name
   * @param res
   */
  public BskyException(String name, HttpResponse<String> res) {
    super(String.format("%d : An error occurred in [%s].", res.statusCode(), name));
    if(res.body() != null) {
      JsonObject obj = toJson(res.body());
      jsonError = obj.get("error").getAsString();
      jsonMessage = obj.get("message").getAsString();
    }
  }
  
  /**
   * Constructor.
   * @param name
   * @param cause
   */
  public BskyException(String name, Throwable cause) {
    super(String.format("An error occurred in [%s].", name), cause);
  }

  public String getJsonError() {
    return jsonError;
  }

  public String getJsonMessage() {
    return jsonMessage;
  }
  
  @Override
  public String getLocalizedMessage() {
    StringBuilder b = new StringBuilder(super.getLocalizedMessage());
    if(jsonError != null) {
      b.append('\n').append(jsonError).append(":").append(jsonMessage);
    }
    return b.toString();
  }
  
}
