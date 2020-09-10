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
      super(userId, "Naverbot");
   }

   @Override
   void scrape() {
      loading();
      Long viewHeight = jsGet("window.innerHeight");
      createSaveDir();
      List<WebElement> videos = driver.findElements(By.className("video-card"));
      System.out.println(videos.size());

      for (int i = 0, currentYScroll = 0; i < videos.size(); ) {
         try {
            actions.moveToElement(videos.get(i)).perform();
            ++i;
            download(getSrcs(), this::getFileName);
            wait(200);
         }
         catch (MoveTargetOutOfBoundsException e) {
            currentYScroll += viewHeight;
            jsScroll(currentYScroll);
         }
      }

      closeFirefox();
   }

   @Override
   void checkIfPageNotFound() {
      while (driver.findElements(By.className("video-card")).isEmpty()) {
         if (!driver.findElements(By.className("error-page")).isEmpty()) {
            System.out.println("Page not found: re-check the user id.");
            closeFirefox();
            System.exit(0);
         }
         wait(100);
      }
   }

   @Override
   HashSet<String> getSrcs() {
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

   private void loading() {
      driver.get("https://www.tiktok.com/@" + userId);
      checkIfPageNotFound();
      loadTheWholePage();
   }

   private void loadTheWholePage() {
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

   private String getFileName(String src) {
      return System.currentTimeMillis() + ".mp4";
   }
}
