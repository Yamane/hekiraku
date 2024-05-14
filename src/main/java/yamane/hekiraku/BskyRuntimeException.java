/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku;

public class BskyRuntimeException extends RuntimeException {

  private String jsonError;
  private String jsonMessage;
  
  /**
   * Constructor.
   * @param name
   * @param cause
   */
  public BskyRuntimeException(String name, Throwable cause) {
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
