/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.accessor;

import static yamane.hekiraku.Hekiraku.*;
import static yamane.hekiraku.util.GsonSupport.*;
import static yamane.hekiraku.util.HttpSupport.*;

import java.util.Date;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.Session;
import yamane.hekiraku.model.Notification;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.util.MapEx;

public class NotificationAccessor extends AbstractAccessor {

  public NotificationAccessor(Session session) {
    super(session);
  }
  
  /**
   * Count the number of unread notifications for the requesting account. Requires auth.
   * @param seenAt Target date-time
   * @return Number of unread notifications.
   * @throws BskyException If the API reports an exception.
   */
  public Integer getUnreadCount(Date seenAt) throws BskyException {
    MapEx map = seenAt != null ? p("seenAt", datetime(seenAt)) : null;
    JsonObject json = toJson(httpGet("app.bsky.notification.getUnreadCount", getAccessJwt(), map));
    return json.get("count").getAsInt();
  }
  
  /**
   * Enumerate notifications for the requesting account. Requires auth.
   * @param limit Possible values: >= 1 and <= 100. Default value: 50.
   * @param cursor date-time cursor.
   * @return Enumerate notifications for the requesting account.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Notification> list(Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor);
    JsonObject json = toJson(httpGet("app.bsky.notification.listNotifications", getAccessJwt(), map));
    PageableList<Notification> result = getList(json.getAsJsonArray("notifications"), Notification.class);
    result.setCursor(getStr(json.get("cursor")));
    result.setSeenAt(getStr(json.get("seenAt")));
    return result;
  }
  
  /**
   * Notify server that the requesting account has seen notifications. Requires auth.
   * @param seenAt Target date-time
   * @throws BskyException If the API reports an exception.
   */
  public void updateSeen(Date seenAt) throws BskyException {
    httpPost("app.bsky.notification.updateSeen", getAccessJwt(), p("seenAt", datetime(seenAt)));
  }
}
