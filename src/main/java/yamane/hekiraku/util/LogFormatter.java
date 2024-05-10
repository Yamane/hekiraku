/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
package yamane.hekiraku.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogFormatter extends Formatter {

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS ");

  public static void addRoot() {
    addRoot(new ConsoleHandler());
  }

  public static void addRoot(Handler handler) {
    handler.setFormatter(new LogFormatter());
    Logger root = Logger.getLogger("");
    root.setUseParentHandlers(false);
    for (Handler h : root.getHandlers()) {
      if (h instanceof ConsoleHandler) {
        root.removeHandler(h);
      }
    }
    root.addHandler(handler);
  }

  @Override
  public String format(LogRecord record) {
    StringBuilder b = new StringBuilder();
    LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(record.getMillis()), ZoneId.systemDefault());
    b.append(dtf.format(dt));
    b.append(record.getLevel().getName()).append(" ");
    b.append("[").append(record.getLoggerName()).append("] ");
    b.append(System.lineSeparator());
    b.append(formatMessage(record));
    b.append(System.lineSeparator());
    if (record.getThrown() != null) {
      StringWriter sw = new StringWriter();
      try (PrintWriter pw = new PrintWriter(sw)) {
        record.getThrown().printStackTrace(pw);
      }
      b.append(sw.toString());
    }
    return b.toString();
  }

}
