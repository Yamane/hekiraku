/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku;

import static yamane.hekiraku.Hekiraku.*;
import static yamane.hekiraku.util.GsonSupport.*;
import static yamane.hekiraku.util.HttpSupport.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.Date;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import yamane.hekiraku.accessor.ActorAccessor;
import yamane.hekiraku.accessor.FeedAccessor;
import yamane.hekiraku.accessor.GraphAccessor;
import yamane.hekiraku.accessor.NotificationAccessor;
import yamane.hekiraku.accessor.RepositoryAccessor;
import yamane.hekiraku.model.DidDoc;
import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.Reply;
import yamane.hekiraku.model.record.BlockRecord;
import yamane.hekiraku.model.record.FollowRecord;
import yamane.hekiraku.model.record.IdentiferRecord;
import yamane.hekiraku.model.record.LikeRecord;
import yamane.hekiraku.model.record.PostRecord;
import yamane.hekiraku.model.record.RepostRecord;

public class Session implements Serializable {

  private static CharsetDecoder dec8 = Charset.forName("UTF-8").newDecoder();
  private static Decoder dec64 = Base64.getDecoder();

  public static JsonSerializer<Session> SERIALIZER = (in, type, context) -> {
    JsonObject obj = new JsonObject();
    obj.add("did", getPrimitive(in.getDid()));
    obj.add("didDoc", context.serialize(in.getDidDoc()));
    obj.add("email", getPrimitive(in.getEmail()));
    obj.add("emailConfirmed", getPrimitive(in.getEmailConfirmed()));
    obj.add("emailAuthFactor", getPrimitive(in.getEmailAuthFactor()));
    obj.add("accessJwt", getPrimitive(in.getAccessJwt()));
    obj.add("refreshJwt", getPrimitive(in.getRefreshJwt()));
    obj.add("handle", getPrimitive(in.getHandle()));
    return obj;
  };

  private ActorAccessor actor;
  private FeedAccessor feed;
  private GraphAccessor graph;
  private NotificationAccessor notification;
  private RepositoryAccessor repository;

  private String did;
  private DidDoc didDoc;
  private String email;
  private Boolean emailConfirmed;
  private Boolean emailAuthFactor;
  private String accessJwt;
  private String refreshJwt;
  private String handle;
  private Date expiration;
  private Date refreshExpiration;

  /**
   * constructor.
   * @param json Json文字列
   */
  public Session(String jsonStr) {
    JsonObject json = toJson(jsonStr);
    this.did = getStr(json.get("did"));
    this.didDoc = new DidDoc(json.getAsJsonObject("didDoc"));
    this.email = getStr(json.get("email"));
    this.emailConfirmed = getBoolean(json.get("emailConfirmed"));
    this.emailAuthFactor = getBoolean(json.get("emailAuthFactor"));
    this.accessJwt = getStr(json.get("accessJwt"));
    this.refreshJwt = getStr(json.get("refreshJwt"));
    this.handle = getStr(json.get("handle"));

    this.actor = new ActorAccessor(this);
    this.feed = new FeedAccessor(this);
    this.graph = new GraphAccessor(this);
    this.notification = new NotificationAccessor(this);
    this.repository = new RepositoryAccessor(this);

    String[] array = accessJwt.split("\\.");
    JsonObject jwt = toJson(new String(dec64.decode(array[1])));
    this.expiration = new Date(getLong(jwt.get("exp")) * 1000);
    array = refreshJwt.split("\\.");
    jwt = toJson(new String(dec64.decode(array[1])));
    this.refreshExpiration = new Date(getLong(jwt.get("exp")) * 1000);
  }

  /**
   * セッションをJsonファイルから生成します.
   * @param file 読み込み元ファイル
   * @return セッション
   * @throws IOException ファイルの読み込みに失敗した場合
   */
  public static Session readFromFile(File file) throws IOException {
    try (FileInputStream fis = new FileInputStream(file); FileChannel ch = fis.getChannel()) {
      ByteBuffer buffer = ByteBuffer.allocate((int) ch.size() * 2);
      ch.read(buffer);
      buffer.flip();
      return new Session(dec8.decode(buffer).toString());
    }
  }

  /**
   * セッションをJsonファイルに書き出します.
   * @param file 出力先ファイル
   * @throws IOException ファイル読み出しに失敗した場合
   */
  public void writeToFile(File file) throws IOException {
    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
      out.print(toJson(this));
    }
  }

  /**
   * メッセージをポストします.
   * @param text ポスト内容
   * @return 生成されたポストレコードのID
   * @throws BskyException If the API reports an exception.
   */
  public Identifer post(String text) throws BskyException {
    return post(new PostRecord(text));
  }

  /**
   * 既存メッセージにリプライします.
   * @param text ポスト内容
   * @param target リプライ元
   * @return 生成されたポストレコードのID
   * @throws BskyException If the API reports an exception.
   */
  public Identifer reply(String text, Identifer target) throws BskyException {
    PostRecord record = new PostRecord(text);
    record.setReply(new Reply(target));
    return post(record);
  }

  /**
   * メッセージをポストします.
   * @param record ポスト内容
   * @return 生成されたレコードのID
   * @throws BskyException If the API reports an exception.
   */
  public Identifer post(PostRecord record) throws BskyException {
    return repo().createRecord(record, "app.bsky.feed.post");
  }

  /**
   * URIで指定されたポストを取得します.
   * @param uri URI
   * @throws BskyException If the API reports an exception.
   */
  public IdentiferRecord<PostRecord> getPost(String uri) throws BskyException {
    return repo().getRecord(uri2rkey(uri), "app.bsky.feed.post", PostRecord.class);
  }

  /**
   * ポスト一覧を取得します.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<IdentiferRecord<PostRecord>> getPosts(Integer limit, String cursor) throws BskyException {
    return repo().listRecords("app.bsky.feed.post", limit, cursor, PostRecord.class);
  }

  /**
   * ポストを削除します.
   * @param uri URI
   * @throws BskyException If the API reports an exception.
   */
  public void deletePost(String uri) throws BskyException {
    repo().deleteRecord(uri2rkey(uri), "app.bsky.feed.post");
  }

  /**
   * 対象に「いいね」します.
   * @param target 「いいね」する対象
   * @return 生成されたレコードのID
   * @throws BskyException If the API reports an exception.
   */
  public Identifer like(Identifer target) throws BskyException {
    return repo().createRecord(new LikeRecord(target), "app.bsky.feed.like");
  }

  /**
   * URIで指定された「いいね」を取得します.
   * @param uri URI
   * @throws BskyException If the API reports an exception.
   */
  public IdentiferRecord<LikeRecord> getLike(String uri) throws BskyException {
    return repo().getRecord(uri2rkey(uri), "app.bsky.feed.like", LikeRecord.class);
  }

  /**
   * 「いいね」一覧を取得します.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<IdentiferRecord<LikeRecord>> getLikes(Integer limit, String cursor) throws BskyException {
    return repo().listRecords("app.bsky.feed.like", limit, cursor, LikeRecord.class);
  }

  /**
   * 「いいね」を削除します.
   * @param uri URI
   * @throws BskyException If the API reports an exception.
   */
  public void deleteLike(String uri) throws BskyException {
    repo().deleteRecord(uri2rkey(uri), "app.bsky.feed.like");
  }

  /**
   * 対象をリポストします.
   * @param target リポストする対象
   * @return 生成されたレコードのID
   * @throws BskyException If the API reports an exception.
   */
  public Identifer repost(Identifer target) throws BskyException {
    return repo().createRecord(new RepostRecord(target), "app.bsky.feed.repost");
  }

  /**
   * URIで指定されたリポストを取得します.
   * @param uri URI
   * @throws BskyException If the API reports an exception.
   */
  public IdentiferRecord<RepostRecord> getRepost(String uri) throws BskyException {
    return repo().getRecord(uri2rkey(uri), "app.bsky.feed.repost", RepostRecord.class);
  }

  /**
   * リポスト一覧を取得します.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<IdentiferRecord<RepostRecord>> getReposts(Integer limit, String cursor) throws BskyException {
    return repo().listRecords("app.bsky.feed.repost", limit, cursor, RepostRecord.class);
  }

  /**
   * リポストを削除します.
   * @param uri URI
   * @throws BskyException If the API reports an exception.
   */
  public void deleteRepost(String uri) throws BskyException {
    repo().deleteRecord(uri2rkey(uri), "app.bsky.feed.repost");
  }

  /**
   * 対象をフォローします.
   * @param did フォローする対象のDID
   * @return 生成されたレコードのID
   * @throws BskyException If the API reports an exception.
   */
  public Identifer follow(String did) throws BskyException {
    return repo().createRecord(new FollowRecord(did), "app.bsky.graph.follow");
  }

  /**
   * フォローしているユーザー一覧を取得します.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<IdentiferRecord<FollowRecord>> getFollws(Integer limit, String cursor) throws BskyException {
    return repo().listRecords("app.bsky.graph.follow", limit, cursor, FollowRecord.class);
  }

  /**
   * フォローを解除します.
   * @param uri URI
   * @throws BskyException If the API reports an exception.
   */
  public void unfollow(String uri) throws BskyException {
    repo().deleteRecord(uri2rkey(uri), "app.bsky.graph.follow");
  }

  /**
   * 対象をブロックします.
   * @param did フォローする対象のDID
   * @return 生成されたレコードのID
   * @throws BskyException If the API reports an exception.
   */
  public Identifer block(String did) throws BskyException {
    return repo().createRecord(new BlockRecord(did), "app.bsky.graph.block");
  }

  /**
   * ブロックしているユーザー一覧を取得します.
   * @param limit Possible values: >= 1 and <= 100.Default value: 50.
   * @param cursor cursor.
   * @return
   * @throws BskyException If the API reports an exception.
   */
  public PageableList<IdentiferRecord<BlockRecord>> getBlocks(Integer limit, String cursor) throws BskyException {
    return repo().listRecords("app.bsky.graph.block", limit, cursor, BlockRecord.class);
  }

  /**
   * ブロックを解除します.
   * @param uri URI
   * @throws BskyException If the API reports an exception.
   */
  public void unblock(String uri) throws BskyException {
    repo().deleteRecord(uri2rkey(uri), "app.bsky.graph.block");
  }

  /**
   * Refresh an authentication session. Requires auth using the 'refreshJwt' (not the 'accessJwt').
   * @return Refreshed session.
   * @throws BskyException If the API reports an exception.
   */
  public Session refresh() throws BskyException {
    Session newSession = new Session(
        httpPost("com.atproto.server.refreshSession", getRefreshJwt(), Collections.emptyMap()));
    newSession.email = email;
    newSession.emailConfirmed = emailConfirmed;
    newSession.emailAuthFactor = emailAuthFactor;
    return newSession;
  }

  /**
   * Delete the current session. Requires auth.
   * @throws BskyException If the API reports an exception.
   */
  public void delete() throws BskyException {
    httpPost("com.atproto.server.deleteSession", getRefreshJwt(), Collections.emptyMap());
  }

  public ActorAccessor actor() {
    return actor;
  }

  public FeedAccessor feed() {
    return feed;
  }

  public GraphAccessor graph() {
    return graph;
  }

  public NotificationAccessor notification() {
    return notification;
  }

  public RepositoryAccessor repo() {
    return repository;
  }

  public String getDid() {
    return did;
  }

  public DidDoc getDidDoc() {
    return didDoc;
  }

  public String getEmail() {
    return email;
  }

  public Boolean getEmailConfirmed() {
    return emailConfirmed;
  }

  public Boolean getEmailAuthFactor() {
    return emailAuthFactor;
  }

  public String getAccessJwt() {
    return accessJwt;
  }

  public String getRefreshJwt() {
    return refreshJwt;
  }

  public String getHandle() {
    return handle;
  }

  public static Decoder getDec64() {
    return dec64;
  }

  public ActorAccessor getActor() {
    return actor;
  }

  public FeedAccessor getFeed() {
    return feed;
  }

  public GraphAccessor getGraph() {
    return graph;
  }

  public NotificationAccessor getNotification() {
    return notification;
  }

  public RepositoryAccessor getRepository() {
    return repository;
  }

  public Date getExpiration() {
    return expiration;
  }

  public Date getRefreshExpiration() {
    return refreshExpiration;
  }

}
