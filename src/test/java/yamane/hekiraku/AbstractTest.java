package yamane.hekiraku;

import java.util.ResourceBundle;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest {

  protected static Session session;

  @BeforeAll
  private static void before() throws Exception {
    ResourceBundle setting = ResourceBundle.getBundle("bluesky");
    String identifier = setting.getString("identifier");
    String password = setting.getString("password");
    session = Hekiraku.createSession(identifier, password);
  }
  
  @AfterAll
  private static void after() throws Exception {
    session.delete();
  }
}
