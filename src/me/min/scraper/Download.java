package me.min.scraper;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.Queue;

class Download implements Runnable {
   private Thread t;
   private final String URL;
   private final String SAVE_PATH;
   private static final Queue<Thread> queue = new LinkedList<>();
   private static int running = 0;

   Download(String url, String savePath) {
      this.URL = url;
      this.SAVE_PATH = savePath;
   }

   public void run() {
      try {
         java.net.URL content = new URL(URL);
         ReadableByteChannel rbc = Channels.newChannel(content.openStream());
         FileOutputStream fos = new FileOutputStream(SAVE_PATH);
         fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
         if (!queue.isEmpty()) {
            queue.remove().start();
            return;
         }
         running--;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void start() {
      if (t == null) {
         t = new Thread(this);
         queue.add(t);
         int MAX_CONCURRENT_THREADS = 10;
         if (running <= MAX_CONCURRENT_THREADS) {
            queue.remove().start();
            running++;
         }
      }
   }
}
