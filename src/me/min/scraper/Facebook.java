package me.min.scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashSet;

public class Facebook extends Scraper {
   public Facebook(String userId) {
      super(userId, "Mozilla");
   }

   @Override
   HashSet<String> getSrcs() {
      return null;
   }

   @Override
   void scrape() {
      driver.get("https://www.facebook.com/login/?next=https%3A%2F%2Fwww.facebook.com%2Fpeople%2FKim-%25C4%2590an%2F100039918071293");
      addCookies("facebook_cookies");
      driver.navigate().refresh();
      clickOnTheFirstImg();
      getCookies("facebook_cookies");
      createSaveDir();
      var srcs = new HashSet<String>();
      var wait = new WebDriverWait(driver, 5, 100);
      while (true) {
         By imgLocator = By.cssSelector("img.ji94ytn4.r9f5tntg.d2edcug0");
         wait.until(ExpectedConditions.presenceOfElementLocated(imgLocator));
         WebElement img = driver.findElement(imgLocator);
         srcs.add(img.getAttribute("src"));
//         download(srcs, this::getFilename);
         if (!nextImg()) {
            break;
         }
         wait.until(ExpectedConditions.stalenessOf(img));
      }
      System.out.println(srcs.size());
      closeDriver();
   }

   private void clickOnTheFirstImg() {
      var wait = new WebDriverWait(driver, 5, 100);
      By firstPhotoLocator = By.cssSelector(".rq0escxv.rj1gh0hx.buofh1pr.ni8dbmo4.stjgntxs.l9j0dhe7");
      wait.until(ExpectedConditions.presenceOfElementLocated(firstPhotoLocator));
      actions.moveToElement(driver.findElement(firstPhotoLocator)).click().perform();
   }
   private String getFilename(String src) {
      String[] s = src.split("/");
      return s[s.length-1].split("\\?")[0];
   }

   private boolean nextImg() {
      var wait = new WebDriverWait(driver, 5, 100);
      By nextPhotoBy;
      try {
         nextPhotoBy = By.cssSelector("div[aria-label=\"Next photo\"]");
         wait.until(ExpectedConditions.elementToBeClickable(nextPhotoBy));
      }
      catch (Exception e) {
         return false;
      }
      actions.moveToElement(driver.findElement(nextPhotoBy)).click().perform();
      wait(300);
      return true;
   }
}
