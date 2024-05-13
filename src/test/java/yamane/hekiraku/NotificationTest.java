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
    notifications.forEach(n -> System.out.println(n.jsonString()));
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

}
