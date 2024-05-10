/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.accessor;

import static yamane.hekiraku.Hekiraku.*;
import static yamane.hekiraku.util.GsonSupport.*;
import static yamane.hekiraku.util.HttpSupport.*;

import java.io.File;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;
import yamane.hekiraku.Session;
import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.RepositoryInfo;
import yamane.hekiraku.model.embed.BlobLink;
import yamane.hekiraku.model.record.AbstractRecord;
import yamane.hekiraku.model.record.IdentiferRecord;
import yamane.hekiraku.model.record.PostRecord;
import yamane.hekiraku.model.ritchtext.Facet;
import yamane.hekiraku.util.MapEx;

public class RepositoryAccessor extends AbstractAccessor {

  public RepositoryAccessor(Session session) {
    super(session);
  }

  /**
   * Create a single new repository record.
   * Requires auth, implemented by PDS.
   * @param record New repository record.
   * @param collection The NSID of the record collection.
   * @return CID and URI
   * @throws BskyException If the API reports an exception.
   */
  public Identifer createRecord(AbstractRecord record, String collection) throws BskyException {
    // Facetからメンションを検出した場合、DIDを検索して登録
    if (record instanceof PostRecord post) {
      for (Facet facet : post.getFacets()) {
        if (facet.getFeatures().get(0).get("$type").toString().endsWith("mention")) {
          Actor actor = actor().getProfile(facet.getFeatures().get(0).get("handle").toString());
          facet.getFeatures().get(0).put("did", actor.getDid());
        }
      }
    }
    record.setCreatedAt(datetime(new Date()));
    MapEx map = p("repo", getDid()).p("collection", collection).p("record", record);
    return new Identifer(toJson(httpPost("com.atproto.repo.createRecord", getAccessJwt(), map)));
  }

  /**
   * Get a single record from a repository.
   * Does not require auth.
   * @param rkey The Record Key.
   * @param collection The NSID of the record collection.
   * @return Record from a repository.
   * @throws BskyException If the API reports an exception.
   */
  public <T extends AbstractRecord> IdentiferRecord<T> getRecord(String rkey, String collection, Class<T> clazz)
      throws BskyException {
    MapEx map = p("repo", getDid()).p("collection", collection).p("rkey", rkey);
    return new IdentiferRecord<T>(toJson(httpGet("com.atproto.repo.getRecord", getAccessJwt(), map)), clazz);
  }

  /**
   * List a range of records in a repository, matching a specific collection.
   * Does not require auth.
   * @param collection The NSID of the record collection.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return Records from a repository.
   * @throws BskyException If the API reports an exception.
   */
  public <T extends AbstractRecord> PageableList<IdentiferRecord<T>> listRecords(String collection, Integer limit,
      String cursor, Class<T> clazz) throws BskyException {
    MapEx map = pageableParams(limit, cursor).p("repo", getDid()).p("collection", collection);
    JsonObject json = toJson(httpGet("com.atproto.repo.listRecords", getAccessJwt(), map));
    JsonArray array = json.getAsJsonArray("records");
    PageableList<IdentiferRecord<T>> list = new PageableList<>();
    if (array != null) {
      for (JsonElement e : array) {
        list.add(new IdentiferRecord<T>(e.getAsJsonObject(), clazz));
      }
    }
    list.setCursor(getStr(json.get("cursor")));
    return list;
  }

  /**
   * Delete a repository record, or ensure it doesn't exist.
   * Requires auth, implemented by PDS.
   * @param rkey The Record Key.
   * @param collection The NSID of the record collection.
   * @throws BskyException If the API reports an exception.
   */
  public void deleteRecord(String rkey, String collection) throws BskyException {
    MapEx map = p("repo", getDid()).p("collection", collection).p("rkey", rkey);
    httpPost("com.atproto.repo.deleteRecord", getAccessJwt(), map);
  }

  /**
   * Get information about an account and repository, including the list of collections. 
   * Does not require auth.
   * @throws BskyException If the API reports an exception.
   */
  public RepositoryInfo describeRepo() throws BskyException {
    MapEx map = p("repo", getDid());
    return new RepositoryInfo(toJson(httpGet("com.atproto.repo.describeRepo", getAccessJwt(), map)));
  }

  /**
   * Upload a new blob, to be referenced from a repository record.
   * The blob will be deleted if it is not referenced within a time window (eg, minutes).
   * Blob restrictions (mimetype, size, etc) are enforced when the reference is created. Requires auth, implemented by PDS.
   * @param file
   * @throws BskyException
   */
  public BlobLink uploadBlob(File file) throws BskyException {
    JsonObject json = toJson(httpPost("com.atproto.repo.uploadBlob", getAccessJwt(), file));
    return new BlobLink(json.getAsJsonObject("blob"));
  }
}
