package yamane.hekiraku;

import java.io.File;

import org.junit.jupiter.api.Test;

import yamane.hekiraku.model.embed.BlobLink;
import yamane.hekiraku.model.embed.EmbedExternal;
import yamane.hekiraku.model.embed.EmbedImages;
import yamane.hekiraku.model.record.PostRecord;

public class RepositoryTest extends AbstractTest {
  
  @Test
  public void describeRepoTest() throws Exception {
    System.out.println(session.repo().describeRepo().jsonString());
  }

  //@Test
  public void uploadBlobTest1() throws Exception {
    BlobLink blob1 = session.repo().uploadBlob(new File("01.jpg"));
    BlobLink blob2 = session.repo().uploadBlob(new File("02.jpg"));
    EmbedImages embed = new EmbedImages();
    embed.addImage(blob1, "画像1");
    embed.addImage(blob2, "画像2");
    PostRecord record = new PostRecord("画像テスト");
    record.setEmbed(embed);
    session.post(record);
  }
  
  //@Test
  public void uploadBlobTest2() throws Exception {
    BlobLink blob = session.repo().uploadBlob(new File("02.jpg"));
    EmbedExternal external = new EmbedExternal();
    external.setTitle("カードに画像");
    external.setUri("https://yamanee.info/index.html");
    external.setDescription("てすと");
    external.setThumb(blob);
    PostRecord record = new PostRecord("画像テスト");
    record.setEmbed(external);
    session.post(record);
  }
}
