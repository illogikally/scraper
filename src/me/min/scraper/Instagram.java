package me.min.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashSet;
import java.util.List;

public class Instagram extends Scraper{

   public Instagram(String userId) {
      super(userId, "Mozilla");
   }
   public Instagram() {
      super("Mozilla");
   }

   @Override
   void scrape() {
      driver.get("https://www.instagram.com/" + userId);
      addCookies("instagram_cookies");
      driver.navigate().refresh();
      checkIfPageNotFound(By::cssSelector, ".v1Nh3.kIKUG._bz0w", "._07DZ3");
      WebElement firstPost = driver.findElement(By.cssSelector(".v1Nh3.kIKUG._bz0w"));
      actions.moveToElement(firstPost).click().perform();
      createSaveDir();
      //while (true) {
      for (int i = 0; i < 3; ++i) {
         waitPostToLoad();
//         download(getSrcs(), this::getFileName);
         System.out.println(getSrcs());
         if (!nextPost()) {
            break;
         }
      }
      getCookies("instagram_cookies");
      closeDriver();
   }

   private boolean nextPost() {
      List<WebElement> nextPost = driver.findElements(By.className("coreSpriteRightPaginationArrow"));
      if (nextPost.isEmpty()) {
         return false;
      }
      actions.moveToElement(nextPost.get(0)).click().perform();
      return true;
   }

   @Override
   HashSet<String> getSrcs() {
      var srcs = new HashSet<String>();
      do {
         List<WebElement> imgs = driver.findElements(By.cssSelector(".ZyFrc .eLAPa img"));
         for (var img : imgs) {
            srcs.add(img.getAttribute("src").replace(";", "&"));
         }
      } while (nextImg());
//      Pattern p = Pattern.compile("class=\"(ZyFrc|tWeCl).*?</");
//      Matcher m = p.matcher(driver.getPageSource());
//      do {
//         while (m.find()) {
//            p = Pattern.compile("(?<=src=\")[^\"]+");
//            Matcher src = p.matcher(m.group());
//            if (src.find()) {
//               srcs.add(src.group().replace(";", "&"));
//            }
//         }
//      } while (nextImg());
      return srcs;
   }

   boolean nextImg() {
      try {
         WebElement nextImg = driver.findElement(By.className("_6CZji"));
         actions.moveToElement(nextImg).click().perform();
         wait(500);
         return true;
      }
      catch (Exception e) {
         return false;
      }
   }

   private void waitPostToLoad() {
      while (!driver.findElements(By.className("jdnLC")).isEmpty()) {
         wait(100);
      }
   }

   private String getFileName(String src) {
      String[] s = src.split("/");
      return s[s.length-1].split("\\?")[0];
   }
}
