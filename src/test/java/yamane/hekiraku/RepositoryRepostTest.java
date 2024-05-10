package yamane.hekiraku;

import java.util.List;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.PostFeed;
import yamane.hekiraku.model.record.IdentiferRecord;
import yamane.hekiraku.model.record.RepostRecord;

public class RepositoryRepostTest extends AbstractTest {
  
  @Test
  public void getRepostsTest() throws Exception {
    List<PostFeed> posts =  session.feed().getTimeline(10, null);
    
    Identifer id = session.repost(posts.get(0).getPost());
    System.out.println(id.jsonString());
    
    
    PageableList<IdentiferRecord<RepostRecord>> list = session.getReposts(10, null);
    list.forEach(r -> System.out.println(r.jsonString()));
    System.out.println(list.size());
    
    session.deleteRepost(id.getUri());
    System.out.println(list.size());
  }

}
