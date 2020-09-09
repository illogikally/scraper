package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tiktok extends Scraper {
   public Tiktok(String userId) {
      super(userId);
   }

   @Override
   void scrape() {
      driver.get("https://www.tiktok.com/@" + userId);
      if (!isPageLoaded()) {
         return;
      }

      loadEntirePage();
      Long viewHeight = jsGet("window.innerHeight");
      createSaveDir();
      List<WebElement> videos = driver.findElements(By.className("video-card"));
      System.out.println(videos.size());

      for (int i = 0, currentYScroll = 0; i < videos.size(); ) {
         try {
            actions.moveToElement(videos.get(i)).perform();
            download(getSrc());
            ++i;
            wait(200);
         }
         catch (MoveTargetOutOfBoundsException e) {
            currentYScroll += viewHeight;
            jsScroll(currentYScroll);
         }
      }
   }

   @Override
   boolean isPageLoaded() {
      while (driver.findElements(By.className("video-card")).isEmpty()) {
         if (!driver.findElements(By.className("error-page")).isEmpty()) {
            return false;
         }
         wait(100);
      }
      return true;
   }

   @Override
   void download(HashSet<String> srcs) {
      System.out.println(srcs);
      long filename = System.currentTimeMillis();
      String savePath = String.format("%s/%s.mp4", userId, filename);
      for (var src : srcs) {
         new Download(src, savePath).start();
      }
   }

   @Override
   HashSet<String> getSrc() {
      HashSet<String> srcs = new HashSet<>();
      Pattern p = Pattern.compile("(?<=video src=\")[^\"]+");
      Matcher src;
      while (!((src = p.matcher(driver.getPageSource())).find())) {
         wait(100);
      };
      srcs.add(src.group());
      return srcs;
   }

   private void jsScroll(Object Y) {
      js.executeScript(String.format("window.scroll(0, %s);", Y));
   }

   private Long jsGet(Object cmd) {
      return (Long) js.executeScript(String.format("return %s;", cmd));
   }

   private void loadEntirePage() {
      Long currScrollHeight = jsGet("document.body.scrollHeight");
      while (true) {
         jsScroll(currScrollHeight);
         wait(200);
         Long newScrollHeight = jsGet("document.body.scrollHeight");
         if (currScrollHeight.equals(newScrollHeight)) {
            break;
         }
         currScrollHeight = newScrollHeight;
      }
      wait(500);
      jsScroll(0);
   }
}
