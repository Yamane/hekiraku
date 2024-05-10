# Hekiraku（碧落）

BluskyのJavaクライアントです。依存はGsonのみの最低限にしてみました。とりあえず17以上。コードてきには11でも動きそうなんですが。  
もとはTwitterで運用していたBOTをBluskyでも動作できるようにしようとして構築したものなので、現状ではすべてのAPIを実装できているわけではありません。  
でも、ポストする、リプライする、通知を取得するなどのBOTを運用するための最低限の機能は実装できたので、とりあえず公開してみたという状態です。  
テストもとりあえずAPIからのエラーは出ない程度のいい加減具合。  
だれかあとはたのむ。

## つかいかた

セッションを生成
```java
String identifier = "アカウント（yamanee.bsky.social みたいなやつ）";
String password ="アプリパスワード";
Session session = Hekiraku.createSession(identifier, password);
```

ポストしてみる
```java
session.post(new PostRecord("はろーあおいそら！！"));
```

リプライとかする
```java
Identifer id = session.post(new PostRecord("はろーあおいそら！！"));
session.reply("きょうはくもりぞら・・・", id);
```

「いいね」とかする
```java
Identifer id = session.post(new PostRecord("はろーあおいそら！！"));
session.like(id);
```

画像とか添付してポストしてみる
```java
EmbedImages embed = new EmbedImages();
BlobLink blob = session.repo().uploadBlob(new File("01.jpg"));
embed.addImage(blob, "画像");
PostRecord record = new PostRecord("画像テスト");
record.setEmbed(embed);
session.post(record);
```

通知を取得してみる
```java
PageableList<Notification> notifications = session.notification().list(10, null);
notifications.forEach(n -> System.out.println(n.jsonString()));
```

通知を全部既読にしてみる
```java
session.notification().updateSeen(new Date());
```

セッションを破棄してみる
```java
session.delete();
```

そのほかもいくつかできることはあるので、（いい加減な）テストコードとか見てみてください。

