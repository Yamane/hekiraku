package yamane.hekiraku;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.Identifer;
import yamane.hekiraku.model.PageableList;
import yamane.hekiraku.model.record.BlockRecord;
import yamane.hekiraku.model.record.IdentiferRecord;

public class RepositoryBlockTest extends AbstractTest {
  
  @Test
  public void getLikesTest() throws Exception {
    Actor actor = session.actor().getProfile("coppelia016.bsky.social");
    Identifer id = session.block(actor.getDid());
    
    PageableList<IdentiferRecord<BlockRecord>> list = session.getBlocks(10, null);
    list.forEach(r -> System.out.println(r.jsonString()));
    
    session.unblock(id.getUri());
  }
}
