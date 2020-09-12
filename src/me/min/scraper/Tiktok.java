package me.min.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;

import java.util.HashSet;
import java.util.List;

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
//            download(getSrcs(), this::getFileName);
            System.out.println(getSrcs());
            wait(200);
         }
         catch (MoveTargetOutOfBoundsException e) {
            currentYScroll += viewHeight;
            jsScroll(currentYScroll);
         }
      }

      closeDriver();
   }

   @Override
   HashSet<String> getSrcs() {
      var srcs = new HashSet<String>();
      List<WebElement> video;
      while ((video = driver.findElements(By.cssSelector("video[src]"))).isEmpty()) {
         wait(100);
      }
      srcs.add(video.get(0).getAttribute("src"));
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
      checkIfPageNotFound(By::className, "video-card", "error-page");
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
