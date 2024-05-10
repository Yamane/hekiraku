/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.accessor;

import static yamane.hekiraku.Hekiraku.*;
import static yamane.hekiraku.util.GsonSupport.*;
import static yamane.hekiraku.util.HttpSupport.*;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.Session;
import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.preference.AbstractPreference;
import yamane.hekiraku.util.MapEx;

public class ActorAccessor extends AbstractAccessor {

  public ActorAccessor(Session session) {
    super(session);
  }
  
  /**
   * Get private preferences attached to the current account. 
   * Expected use is synchronization between multiple devices, 
   * and import/export during account migration. Requires auth.
   * @return Preferences.
   * @throws BskyException If the API reports an exception.
   */
  public List<AbstractPreference> getPreferences() throws BskyException {
    JsonObject json = toJson(httpGet("app.bsky.actor.getPreferences", getAccessJwt(), null));
    return AbstractPreference.createList(json.getAsJsonArray("preferences"));
  }
  
  /**
   * Get detailed profile view of an actor. Does not require auth,
   * but contains relevant metadata with auth.
   * @param actor Handle or DID of account to fetch profile of.
   * @return Detailed profile view of an actor.
   * @throws BskyException If the API reports an exception.
   */
  public Actor getProfile(String actor) throws BskyException {
    return new Actor(toJson(httpGet("app.bsky.actor.getProfile", getAccessJwt(), p("actor", actor))));
  }
  
  /**
   * Get detailed profile views of multiple actors.
   * @param actors Handle or DID of account to fetch profile of. Possible values: <= 25.
   * @return Actors (profiles) .
   * @throws BskyException If the API reports an exception.
   */
  public List<Actor> getProfiles(String... actors) throws BskyException {
    JsonObject json = toJson(
        httpGet("app.bsky.actor.getProfiles", getAccessJwt(), p("actors", Arrays.asList(actors))));
    return getList(json.getAsJsonArray("profiles"), Actor.class);
  }
  
  /**
   *  Find actors (profiles) matching search criteria. Does not require auth.
   * @param query Search query string. Syntax, phrase, boolean, and faceting is unspecified, but Lucene query syntax is recommended.
   * @param limit Possible values: >= 1 and <= 100 Default value: 25.
   * @param cursor cursor.
   * @return  Actors (profiles) .
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Actor> searchActors(String query,  Integer limit, Integer cursor) throws BskyException {
    MapEx map = pageableParams(limit  != null ? limit: 25, cursor).p("q", query);        
    return toPageableList(httpGet("app.bsky.actor.searchActors", getAccessJwt(), map), "actors", Actor.class);
  }
  
  /**
   * Find actor suggestions for a prefix search term.
   * Expected use is for auto-completion during text field entry. Does not require auth.
   * @param query Search query prefix; not a full query string.
   * @param limit Possible values: >= 1 and <= 100 Default value: 25.
   * @return
   * @throws BskyException
   */
  public PageableList<Actor> searchActorsTypeahead(String query,  Integer limit) throws BskyException {
    MapEx map = pageableParams(limit  != null ? limit: 25, null).p("q", query);        
    return toPageableList(httpGet("app.bsky.actor.searchActorsTypeahead", getAccessJwt(), map), "actors", Actor.class);
  }

  /**
   * Get a list of suggested actors.
   * Expected use is discovery of accounts to follow during new account onboarding.
   * @param limit Possible values: >= 1 and <= 100 Default value: 50.
   * @param cursor cursor.
   * @return Suggested actors.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Actor> getSuggestions(Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor);        
    return toPageableList(httpGet("app.bsky.actor.getSuggestions", getAccessJwt(), map), "actors", Actor.class);
  }
}
