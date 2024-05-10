package yamane.hekiraku;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.UserList;

public class GraphTest extends AbstractTest {
  
  @Test
  public void getFollowersTest() throws Exception {
    PageableList<Actor> actors =  session.graph().getFollowers("koyanee.bsky.social", 10, null);
    System.out.println(actors.jsonString());
  }
  
  @Test
  public void getFollowsTest() throws Exception {
    PageableList<Actor> actors =  session.graph().getFollows("koyanee.bsky.social", 10, null);
    System.out.println(actors.jsonString());
  }
  
  @Test
  public void getSuggestedFollowsByActorTest() throws Exception {
    PageableList<Actor> actors =  session.graph().getSuggestedFollowsByActor("koyanee.bsky.social");
    System.out.println(actors.jsonString());
  }
  
  @Test
  public void muteActorTest() throws Exception {
    session.graph().muteActor("yamanee.bsky.social");
    session.graph().unmuteActor("yamanee.bsky.social");
  }
  
  @Test
  public void getBlocksTest() throws Exception {
    PageableList<Actor> actors =  session.graph().getBlocks(10, null);
    actors.forEach(a ->  System.out.println(a.jsonString()));
  }
  
  @Test
  public void getMutesTest() throws Exception {
    session.graph().muteActor("yamanee.bsky.social");
  }

  @Test
  public void getListBlocksTest() throws Exception {
    PageableList<UserList> lists =  session.graph().getListBlocks(10, null);
    System.out.println(lists.jsonString());
  }
  
  @Test
  public void getListMutesTest() throws Exception {
    PageableList<UserList> lists =  session.graph().getListMutes(10, null);
    System.out.println(lists.jsonString());
  }
}
