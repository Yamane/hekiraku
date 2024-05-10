package yamane.hekiraku;

import java.util.List;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.PostFeed;
import yamane.hekiraku.model.record.IdentiferRecord;
import yamane.hekiraku.model.record.LikeRecord;

public class RepositoryLikeTest extends AbstractTest {
  
  @Test
  public void getLikesTest() throws Exception {
    List<PostFeed> posts =  session.feed().getTimeline(10, null);
    
    Identifer id = session.like(posts.get(0).getPost());
    System.out.println(id.jsonString());
    
    PageableList<IdentiferRecord<LikeRecord>> list = session.getLikes(10, null);
    list.forEach(r -> System.out.println(r.jsonString()));
    System.out.println(list.size());
    
    session.deleteLike(id.getUri());
    System.out.println(list.size());
  }

}
