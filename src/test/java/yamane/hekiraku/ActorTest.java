package yamane.hekiraku;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Actor;
import yamane.hekiraku.model.preference.AbstractPreference;

public class ActorTest  extends AbstractTest {

  @Test
  public void getPreferencesTest() throws Exception {
    List<AbstractPreference> list = session.actor().getPreferences();
    list.forEach(p -> System.out.println(p.jsonString()));
  }
  
  @Test
  public void testGetProfile() throws Exception {
    Actor actor = session.actor().getProfile("koyanee.bsky.social");
    assertNotNull(actor);
    actor = session.actor().getProfile(actor.getDid());
    assertNotNull(actor);
  }
  
  @Test
  public void testGetProfiles() throws Exception {
    List<Actor> suggestions = session.actor().getProfiles("koyanee.bsky.social", "coppelia016.bsky.social");
    suggestions.forEach(a -> System.out.println(a.getDisplayName() + " @" + a.getHandle()));
  }
  
  @Test
  public void testGetSuggestions() throws Exception {
    List<Actor> suggestions = session.actor().getSuggestions(null, null);
    suggestions.forEach(a -> System.out.println(a.getDisplayName() + " @" + a.getHandle()));
  }
  
  @Test
  public void testSearchActors() throws Exception {
    List<Actor> suggestions = session.actor().searchActors("コッペ", 3, 3);
    suggestions.forEach(a -> System.out.println(a.jsonString()));
  }
  
  @Test
  public void testSearchActorsTypeahead() throws Exception {
    List<Actor> suggestions = session.actor().searchActorsTypeahead("coppe", 3);
    suggestions.forEach(a -> System.out.println(a.jsonString()));
  }

}
