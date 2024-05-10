/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.accessor;

import java.io.Serializable;

import yamane.hekiraku.Session;

public abstract class AbstractAccessor implements Serializable {

  private Session session;
  
  protected AbstractAccessor(Session session) {
    this.session = session;
  }
  
  public ActorAccessor actor() {
    return session.actor();
  }
  
  public FeedAccessor feed() {
    return session.feed();
  }
  
  public NotificationAccessor notification() {
    return session.notification();
  }

  public RepositoryAccessor repo() {
    return session.repo();
  }
  
  public String getDid() {
    return session.getDid();
  }
  
  protected String getAccessJwt() {
     return session.getAccessJwt();
  }

  public String getRefreshJwt() {
    return session.getRefreshJwt();
  }
}
