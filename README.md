# Hekiraku (碧落)

This is a Java client for Bluesky. I tried to keep the dependencies to a minimum, with only Gson required. It works with Java 17 and above, although the code might run on Java 11 as well.

Originally, this was built to make a Twitter bot work on Bluesky, so not all APIs are implemented at the moment. However, the basic functionalities needed to run a bot, such as posting, replying, and fetching notifications, have been implemented. That's why I decided to release it in its current state.

The testing is also minimal, just enough to avoid API errors. I'm hoping someone else can take it from here.

## How to Use

Generate a session:
```
String identifier = "account (like yamanee.bsky.social)";
String password = "app password (can be generated from 'Settings -> Advanced' in the web client)";
Session session = Hekiraku.createSession(identifier, password);
```

Try posting:
```
session.post(new PostRecord("Hello, bluesky!!"));
```

Reply to a post:
```
Identifer id = session.post(new PostRecord("Hello, bluesky!!"));
session.reply("It's cloudy today...", id);
```

Like a post:
```
Identifer id = session.post(new PostRecord("Hello, bluesky!!"));
session.like(id);
```

Post with an attached image:
```
EmbedImages embed = new EmbedImages();
BlobLink blob = session.repo().uploadBlob(new File("01.jpg"));
embed.addImage(blob, "Image");
PostRecord record = new PostRecord("Image test");
record.setEmbed(embed);
session.post(record);
```

Fetch notifications:
```
PageableList<Notification> notifications = session.notification().list(10, null);
notifications.forEach(n -> System.out.println(n.jsonString()));
```

Mark all notifications as read:
```
session.notification().updateSeen(new Date());
```

Delete a session:
```
session.delete();
```

There are a few other things you can do, so please check the (minimal) test code.

## Testing

Skipping tests is recommended to avoid unnecessary posts. To generate the JAR file:

```
mvn install -DskipTests=true
```

If you want to run the tests, create a /src/test/resources/bluesky.properties file with the following content:

```
identifier=account (like yamanee.bsky.social, without @)
password=app password (can be generated from 'Settings -> Advanced' in the web client)
```