package yamane.hekiraku;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.Notification;
import yamane.hekiraku.model.PageableList;

public class NotificationTest extends AbstractTest {

  @Test
  public void listNotificationsTest() throws Exception {
    PageableList<Notification> notifications = session.notification().list(10, null);
    notifications.forEach(n -> System.out.println(toString(n)));
  }
  
  @Test
  public void updateSeenTest() throws Exception {
    session.notification().updateSeen(new Date());
  }
  
  @Test
  public void getUnreadCountTest() throws Exception {
    Integer count = session.notification().getUnreadCount(null);
    assertNotNull(count);
  }
  
  private String toString(Notification n) {
    StringBuilder b = new StringBuilder();
    b.append(n.getAuthor().getDisplayName());
    b.append("\n");
    b.append(n.getReason());
    return b.toString();
  }
}
