/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.accessor;

import static yamane.hekiraku.Hekiraku.*;
import static yamane.hekiraku.util.GsonSupport.*;
import static yamane.hekiraku.util.HttpSupport.*;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.Session;
import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.UserList;
import yamane.hekiraku.util.MapEx;

public class GraphAccessor extends AbstractAccessor {

  public GraphAccessor(Session session) {
    super(session);
  }

  /**
   * Enumerates accounts which follow a specified account (actor).
   * @param actor at-identifier.
   * @param limit Possible values: >= 1 and <= 100. Default value: 50.
   * @param cursor Cursor.
   * @return Followers.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Actor> getFollowers(String actor, Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("actor", actor);
    return toPageableList(httpGet("app.bsky.graph.getFollowers", getAccessJwt(), map), "followers", Actor.class);
  }

  /**
   * Enumerates accounts which a specified account (actor) follows.
   * @param actor at-identifier.
   * @param limit Possible values: >= 1 and <= 100. Default value: 50.
   * @param cursor Cursor.
   * @return Follows.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Actor> getFollows(String actor, Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("actor", actor);
    return toPageableList(httpGet("app.bsky.graph.getFollows", getAccessJwt(), map), "follows", Actor.class);
  }

  /**
   * Enumerates follows similar to a given account (actor).
   * Expected use is to recommend additional accounts immediately after following one account.
   * @param actor at-identifier
   * @return Actors.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Actor> getSuggestedFollowsByActor(String actor) throws BskyException {
    MapEx map = p("actor", actor);
    return toPageableList(httpGet("app.bsky.graph.getSuggestedFollowsByActor", getAccessJwt(), map),
        "suggestions", Actor.class);
  }

  /**
   * Creates a mute relationship for the specified account.
   * Mutes are private in Bluesky. Requires auth.
   * @param actor at-identifier
   * @throws BskyException If the API reports an exception.
   */
  public void muteActor(String actor) throws BskyException {
    MapEx map = p("actor", actor);
    httpPost("app.bsky.graph.muteActor", getAccessJwt(), map);
  }
  
  /**
   * Unmutes the specified account. Requires auth.
   * @param actor at-identifier
   * @throws BskyException If the API reports an exception.
   */
  public void unmuteActor(String actor) throws BskyException {
    MapEx map = p("actor", actor);
    httpPost("app.bsky.graph.unmuteActor", getAccessJwt(), map);
  }

  /**
   * Enumerates which accounts the requesting account is currently blocking. Requires auth.
   * @param limit Possible values: >= 1 and <= 100. Default value: 50.
   * @param cursor Cursor.
   * @return Accounts currently blocking.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Actor> getBlocks(Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor);
    return toPageableList(httpGet("app.bsky.graph.getBlocks", getAccessJwt(), map), "blocks", Actor.class);
  }

  /**
   * Enumerates accounts that the requesting account (actor) currently has muted. Requires auth.
   * @param limit Possible values: >= 1 and <= 100. Default value: 50.
   * @param cursor Cursor.
   * @return Accounts currently blocking.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Actor> getMutes(Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor);
    return toPageableList(httpGet("app.bsky.graph.getMutes", getAccessJwt(), map), "mutes", Actor.class);
  }

  /**
   * Get mod lists that the requesting account (actor) is blocking. Requires auth.
   * @param limit Possible values: >= 1 and <= 100. Default value: 50.
   * @param cursor Cursor.
   * @return Lists.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<UserList> getListBlocks(Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor);
    return toPageableList(httpGet("app.bsky.graph.getListBlocks", getAccessJwt(), map), "lists", UserList.class);
  }

  /**
   * Enumerates mod lists that the requesting account (actor) currently has muted. Requires auth.
   * @param limit Possible values: >= 1 and <= 100. Default value: 50.
   * @param cursor Cursor.
   * @return Lists.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<UserList> getListMutes(Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor);
    return toPageableList(httpGet("app.bsky.graph.getListMutes", getAccessJwt(), map), "lists", UserList.class);
  }
}
