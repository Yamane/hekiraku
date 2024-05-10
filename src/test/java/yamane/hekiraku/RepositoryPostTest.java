package yamane.hekiraku;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.PostFeed;
import yamane.hekiraku.model.PostView;
import yamane.hekiraku.model.embed.EmbedExternal;
import yamane.hekiraku.model.embed.EmbedRecord;
import yamane.hekiraku.model.record.IdentiferRecord;
import yamane.hekiraku.model.record.PostRecord;

public class RepositoryPostTest extends AbstractTest {
  
  @Test
  public void postTest() throws Exception {
    Identifer id = session.post(new PostRecord("はろーあおいそら！！"));
    session.reply("きょうはくもりぞら・・・", id);
  }
  
  @Test
  public void retchTextTest1() throws Exception {
    PostRecord record = new PostRecord(
        """
        @yamanee.bsky.social
        メンションテスト
        """);
    session.post(record);
  }
  
  @Test
  public void retchTextTest2() throws Exception {
    PostRecord record = new PostRecord(
        """
        タグテスト
        #TESTTAG
        """);
    session.post(record);
  }
  
  @Test
  public void retchTextTest3() throws Exception {
    PostRecord record = new PostRecord(
        """
        リンクテスト
        https://docs.bsky.app/docs/api/com-atproto-repo-create-record
        """);
    session.post(record);
  }
  
  @Test
  public void embedTest1() throws Exception {
    PostRecord record = new PostRecord("外部参照テスト");
    record.setEmbed(new EmbedRecord(getFirstPost()));
    session.post(record);
  }
  
  @Test
  public void embedTest2() throws Exception {
    EmbedExternal embed = new EmbedExternal();
    embed.setTitle("Get Started | Bluesky");
    embed.setUri("https://docs.bsky.app/docs/get-started");
    embed.setDescription("");
    PostRecord record = new PostRecord("外部参照テスト");
    record.setEmbed(embed);
    session.post(record);
  }
  
  @Test
  public void getPostTest() throws Exception {
    Identifer id = session.getPost("https://bsky.app/profile/koyanee.bsky.social/post/3kqyxh7gt6a2t");
    System.out.println(id.jsonString());
  }
  
  @Test
  public void getPostsTest() throws Exception {
    PageableList<IdentiferRecord<PostRecord>> list = session.getPosts(10, null);
    list.forEach(r -> System.out.println(r.jsonString()));
  }
  
  @Test
  public void deleteTest() throws Exception {
    Identifer id = session.post(new PostRecord("すぐ消す！！"));
    session.deletePost(id.getUri());
  }
  
  private PostView getFirstPost() throws Exception {
    PageableList<PostFeed> feeds = session.feed().getTimeline(10, null);
    return feeds.get(0).getPost();
  }
}
