package yamane.hekiraku;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.record.FollowRecord;
import yamane.hekiraku.model.record.IdentiferRecord;

public class RepositoryFollowTest extends AbstractTest {
  
  @Test
  public void getLikesTest() throws Exception {
    Actor actor = session.actor().getProfile("coppelia016.bsky.social");
    Identifer id = session.follow(actor.getDid());
    
    PageableList<IdentiferRecord<FollowRecord>> list = session.getFollws(10, null);
    list.forEach(r -> System.out.println(r.jsonString()));
    
    session.unfollow(id.getUri());
  }
}
