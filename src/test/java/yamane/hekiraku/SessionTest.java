package yamane.hekiraku;

import org.junit.jupiter.api.Test;

public class SessionTest extends AbstractTest {

  @Test
  public void testSession() throws Exception {
    System.out.println(session.getDidDoc().jsonString());
    session = session.refresh();
  }
  
}
