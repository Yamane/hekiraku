package yamane.hekiraku;

import java.util.List;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.Feed;
import yamane.hekiraku.model.Like;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.PostFeed;
import yamane.hekiraku.model.PostView;

public class FeedTest extends AbstractTest {

  //@Test
  public void getActorFeedsTest() throws Exception {
    List<Feed> feeds =  session.feed().getActorFeeds("bsky.app", 10, null);
    feeds.forEach(f -> System.out.println(f.jsonString()));
  }
  
  //@Test
  public void getFeedTest() throws Exception {
    List<PostFeed> feeds = session.feed().getFeed("at://did:plc:z72i7hdynmk6r22z27h6tvur/app.bsky.feed.generator/whats-hot", 20, null);
    feeds.forEach(f -> System.out.println(f.jsonString()));
  }
  
  //@Test
  public void getListFeedTest() throws Exception {
    List<PostFeed> feeds = session.feed().getListFeed(20, null, "at://did:plc:z72i7hdynmk6r22z27h6tvur/app.bsky.feed.generator/with-friends");
    feeds.forEach(f -> System.out.println(f.jsonString()));
  }
  
  @Test
  public void getTimelineTest() throws Exception {
    PageableList<PostFeed> feeds = session.feed().getTimeline(10, null);
    feeds.forEach(f -> System.out.println(f.jsonString()));
  }
  
  //@Test
  public void getActorLikesTest() throws Exception {
    List<PostFeed> feeds = session.feed().getActorLikes("koyanee.bsky.social", 20, null);
    feeds.forEach(f -> System.out.println(f.jsonString()));
  }
  
  //@Test
  public void getAuthorFeedTest() throws Exception {
    List<PostFeed> feeds = session.feed().getAuthorFeed("yamanee.bsky.social", 20, null);
    feeds.forEach(f -> System.out.println(f.jsonString()));
  }
  
  //@Test
  public void getLikesTest() throws Exception {
    List<Like> feeds = session.feed().getLikes("at://did:plc:wimmazercwuup7z2bsbmg6za/app.bsky.feed.post/3ko3z3bkdyx27", null, null, null);
    feeds.forEach(l -> System.out.println(l.jsonString()));
  }
  
  //@Test
  public void getPostsTest() throws Exception {
    List<PostView> posts = session.feed().getPosts("at://did:plc:wimmazercwuup7z2bsbmg6za/app.bsky.feed.post/3krsgmhlqxc2y");
    posts.forEach(p -> System.out.println(p.jsonString()));
  }
  
  //@Test
  public void getRepostedByTest() throws Exception {
    List<Actor> actors = session.feed().getRepostedBy("at://did:plc:z72i7hdynmk6r22z27h6tvur/app.bsky.feed.post/3ks36ksgapc2o", null, null, null);
    actors.forEach(a -> System.out.println(a.jsonString()));
  }
  
  //@Test
  public void getSuggestedFeedsTest() throws Exception {
    List<Feed> feeds = session.feed().getSuggestedFeeds(null, null);
    feeds.forEach(f -> System.out.println(f.jsonString()));
  }
  
  //@Test
  public void searchPostsTest() throws Exception {
    List<PostView> posts = session.feed().searchPosts("ログホラ", null, null, null, null, null, null, null, 5, null);
    posts.forEach(p -> System.out.println(p.jsonString()));
  }
  
}
