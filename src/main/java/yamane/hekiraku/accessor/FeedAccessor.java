/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.accessor;

import static yamane.hekiraku.Hekiraku.*;
import static yamane.hekiraku.util.GsonSupport.*;
import static yamane.hekiraku.util.HttpSupport.*;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.Session;
import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.Feed;
import yamane.hekiraku.model.Like;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.PostFeed;
import yamane.hekiraku.model.PostView;
import yamane.hekiraku.util.MapEx;

public class FeedAccessor extends AbstractAccessor {

  public FeedAccessor(Session session) {
    super(session);
  }

  /**
   * Get a view of the requesting account's home timeline.
   * This is expected to be some form of reverse-chronological feed.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return Account's home timeline.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<PostFeed> getTimeline(Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor);
    return toPageableList(httpGet("app.bsky.feed.getTimeline", getAccessJwt(), map), "feed", PostFeed.class);
  }

  /**
   * Get a list of feeds (feed generator records) created by the actor (in the actor's repo).
   * @param actor at-identifier
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return List of feeds.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Feed> getActorFeeds(String actor, Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("actor", actor);
    return toPageableList(httpGet("app.bsky.feed.getActorFeeds", getAccessJwt(), map), "feeds", Feed.class);
  }

  /**
   * Get a list of posts liked by an actor. Does not require auth.
   * @param actor at-identifier
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return Posts liked by an actor.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<PostFeed> getActorLikes(String actor, Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("actor", actor);
    return toPageableList(httpGet("app.bsky.feed.getActorLikes", getAccessJwt(), map), "feed", PostFeed.class);
  }

  /**
   * Get a view of an actor's 'author feed' (post and reposts by the author).
   * Does not require auth.
   * @param actor at-identifier
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return Actor's 'author feed'.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<PostFeed> getAuthorFeed(String actor, Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("actor", actor);
    JsonObject json = toJson(httpGet("app.bsky.feed.getAuthorFeed", getAccessJwt(), map));
    PageableList<PostFeed> result = getList(json.getAsJsonArray("feed"), PostFeed.class);
    result.setCursor(getStr(json.get("cursor")));
    result.setFilter(getStr(json.get("filter")));
    return result;
  }

  /**
   * Get a hydrated feed from an actor's selected feed generator.
   * Implemented by App View.
   * @param uid at-uri
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return Feeds.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<PostFeed> getFeed(String uid, Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("feed", uid);
    return toPageableList(httpGet("app.bsky.feed.getFeed", getAccessJwt(), map), "feed", PostFeed.class);
  }

  /**
   * Get a feed of recent posts from a list (posts and reposts from any actors on the list).
   * Does not require auth.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @param uids Reference (AT-URI) to the list record.
   * @return Feeds.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<PostFeed> getListFeed(Integer limit, String cursor, String... uids) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("list", uids);
    return toPageableList(httpGet("app.bsky.feed.getListFeed", getAccessJwt(), map), "feed", PostFeed.class);
  }

  /**
   * Get like records which reference a subject (by AT-URI and CID).
   * Does not require auth.
   * @param uri AT-URI of the subject (eg, a post record).
   * @param cid CID of the subject record (aka, specific version of record), to filter likes.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return Like records which reference a subject.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Like> getLikes(String uri, String cid, Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("uri", uri).p("cid", cid);
    JsonObject json = toJson(httpGet("app.bsky.feed.getLikes", getAccessJwt(), map));
    PageableList<Like> result = getList(json.getAsJsonArray("likes"), Like.class);
    result.setUri(getStr(json.get("uri")));
    result.setCid(getStr(json.get("cid")));
    result.setCursor(getStr(json.get("cursor")));
    return result;
  }

  /**
   * Gets post views for a specified list of posts (by AT-URI).
   * This is sometimes referred to as 'hydrating' a 'feed skeleton'.
   * @param uris List of post AT-URIs to return hydrated views for.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<PostView> getPosts(String... uris) throws BskyException {
    MapEx map = p("uris", uris);
    return toPageableList(httpGet("app.bsky.feed.getPosts", getAccessJwt(), map), "posts", PostView.class);
  }

  /**
   * Get a list of reposts for a given post.
   * @param uri Reference (AT-URI) of post record
   * @param cid If supplied, filters to reposts of specific version (by CID) of the post record.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return List of reposts for a given post.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Actor> getRepostedBy(String uri, String cid, Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("uri", uri).p("cid", cid);
    JsonObject json = toJson(httpGet("app.bsky.feed.getRepostedBy", getAccessJwt(), map));
    PageableList<Actor> result = getList(json.getAsJsonArray("repostedBy"), Actor.class);
    result.setUri(getStr(json.get("uri")));
    result.setCid(getStr(json.get("cid")));
    result.setCursor(getStr(json.get("cursor")));
    return result;
  }

  /**
   * Get a list of suggested feeds (feed generators) for the requesting account.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return List of suggested feeds.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<Feed> getSuggestedFeeds(Integer limit, String cursor) throws BskyException {
    MapEx map = pageableParams(limit, cursor);
    return toPageableList(httpGet("app.bsky.feed.getSuggestedFeeds", getAccessJwt(), map), "feeds", Feed.class);
  }

  /**
   * Find posts matching search criteria, returning views of those posts.
   * @param q Search query string; syntax, phrase, boolean, and faceting is unspecified, but Lucene query syntax is recommended.
   * @param sort Possible values: [top, latest] Default value: latest.
   * @param since Filter results for posts after the indicated datetime (inclusive). Expected to use 'sortAt' timestamp, which may not match 'createdAt'. Can be a datetime, or just an ISO date (YYYY-MM-DD).
   * @param until Filter results for posts before the indicated datetime (not inclusive). Expected to use 'sortAt' timestamp, which may not match 'createdAt'. Can be a datetime, or just an ISO date (YYY-MM-DD).
   * @param mentions Filter to posts which mention the given account. Handles are resolved to DID before query-time. Only matches rich-text facet mentions.
   * @param author Filter to posts by the given account. Handles are resolved to DID before query-time.
   * @param lang Filter to posts in the given language. Expected to be based on post language field, though server may override language detection.
   * @param domain Filter to posts with URLs (facet links or embeds) linking to the given domain (hostname). Server may apply hostname normalization.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @param tag Filter to posts with the given tag (hashtag), based on rich-text facet or tag field. Do not include the hash (#) prefix. Multiple tags can be specified, with 'AND' matching.
   * @return Posts matching search criteria.
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<PostView> searchPosts(String q, String sort, String since, String until, String mentions,
      String author, String lang, String domain, Integer limit, String cursor, String... tag) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("q", q).p("sort", sort).p("since", since).p("until", until)
        .p("mentions", mentions).p("author", author).p("lang", lang).p("domain", domain).p("tag", tag);
    JsonObject json = toJson(httpGet("app.bsky.feed.searchPosts", getAccessJwt(), map));
    PageableList<PostView> result = getList(json.getAsJsonArray("posts"), PostView.class);
    result.setCursor(getStr(json.get("cursor")));
    result.setHitsTotal(getInt(json.get("hitsTotal")));
    return result;
  }

}
