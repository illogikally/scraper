package com.company;

import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

import java.io.IOException;
import java.util.Scanner;

public class Main {
   public static void main(String[] args) {
      new Instagram().get1post("ksjfl");
   }


//      ProfilesIni profileIni = new ProfilesIni();
//      FirefoxProfile profile = profileIni.getProfile(firefoxProfile);
//      profile.setAcceptUntrustedCertificates(true);
//      FirefoxOptions options = new FirefoxOptions();
//      options.setProfile(profile);
//      options.setHeadless(isHeadless);
//      options.addPreference("general.useragent.override", "Naverbot");
//      return options;
//   }
//
//   static void instagram(String instagramId) throws InterruptedException {
//      String osUser = System.getProperty("user.name");
//      System.setProperty("webdriver.gecko.driver", "C:/Users/" + osUser + "/Desktop/geckodriver/geckodriver.exe");
//      WebDriver driver = new FirefoxDriver(firefoxOptions("selenium", false));
//      long currentTime = System.currentTimeMillis();
//      driver.get("https://www.instagram.com/" + instagramId);
////      TimeUnit.SECONDS.sleep(1);
//      Actions actions = new Actions(driver);
//      while (driver.findElements(By.cssSelector(".v1Nh3.kIKUG._bz0w")).isEmpty()) { }
//      List<WebElement> posts = driver.findElements(By.cssSelector(".v1Nh3.kIKUG._bz0w"));
//      actions.moveToElement(posts.get(0)).click().perform();
//      String downloadDirectory = String.format("C:/Users/%s/Desktop/%s/", osUser, instagramId);
//      new File(downloadDirectory).mkdir();
//      while (true) {
////      for (int i = 0; i < 5; ++i) {
//         //Set<String> srcs = new HashSet<>();
//         var srcs = new HashSet<String>();
//         while (!driver.findElements(By.className("jdnLC")).isEmpty()) {
//            TimeUnit.MILLISECONDS.sleep(20);
//         }
//         while (true) {
//            Pattern p = Pattern.compile("class=\"(ZyFrc|tWeCl).*?</");
//            Matcher content = p.matcher(driver.getPageSource());
//            while (content.find()) {
//               p = Pattern.compile("(?<=src=\")[^\"]+");
//               Matcher src = p.matcher(content.group());
//               if (src.find()) {
//                  srcs.add(src.group().replace(";", "&"));
//               }
//            }
////            List<WebElement> nextImg = driver.findElements(By.className("_6CZji"));
////            if (nextImg.isEmpty()) {
////               break;
////            }
//            try {
//               WebElement nextImg = driver.findElement(By.className("_6CZji"));
//            }
//            actions.moveToElement(nextImg.get(0)).click().perform();
//         }
//         for (String src : srcs) {
//            Matcher filename = Pattern.compile("[^/]+?(.jpg|.mp4)").matcher(src);
//            filename.find();
//            (new Download(src, downloadDirectory + filename.group())).start();
//         }
//         List<WebElement> nextPost = driver.findElements(By.className("coreSpriteRightPaginationArrow"));
//         if (nextPost.isEmpty()) {
//            break;
//         }
//         actions.moveToElement(nextPost.get(0)).click().perform();
//      }
//      System.out.println((System.currentTimeMillis() - currentTime) / 1000.0);
//      driver.close();
//      driver.quit();
//   }
//
//   static void tiktok(String tiktokId) throws InterruptedException {
//      String osUser = System.getProperty("user.name");
//      System.setProperty("webdriver.gecko.driver", "C:/Users/" + osUser + "/Desktop/geckodriver/geckodriver.exe");
//      WebDriver driver = new FirefoxDriver(firefoxOptions("selenium", true));
//      long timer = System.currentTimeMillis();
//      driver.get("https://www.tiktok.com/@" + tiktokId);
//      while (driver.findElements(By.className("video-card")).isEmpty()) {
//
//      }
//      JavascriptExecutor js = (JavascriptExecutor)driver;
//      Long lastScrollHeight = (Long)js.executeScript("return document.body.scrollHeight;");
//      while (true) {
//         js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
//         TimeUnit.MILLISECONDS.sleep(200);
//         Long currentScrollHeight = (Long)js.executeScript("return document.body.scrollHeight;");
//         if (lastScrollHeight.equals(currentScrollHeight)) {
//            js.executeScript("window.scrollTo(0, 0);");
//            break;
//         }
//         lastScrollHeight = currentScrollHeight;
//      }
//      List<WebElement> videos = driver.findElements(By.className("video-card"));
//      Actions actions = new Actions(driver);
//
//      int currentYScroll = 0;
//      Long viewHeight = (Long)js.executeScript("return window.innerHeight;");
//      String downloadDirectory = String.format("C:/Users/%s/Desktop/%s/", osUser, tiktokId);
//      new File(downloadDirectory).mkdir();
//      for (int i = 0; i < videos.size(); ++i) {
//         try {
//            actions.moveToElement(videos.get(i)).perform();
//            Pattern p = Pattern.compile("(?<=video src=\")[^\"]+");
//            Matcher src = p.matcher(driver.getPageSource());
//            while (!src.find()) {
//               src = p.matcher(driver.getPageSource());
//               TimeUnit.MILLISECONDS.sleep(20);
//            }
////            Matcher filename = Pattern.compile("(?<=\\.com/)[^/]+").matcher(src.group());
////            filename.find();
//            long filename = System.currentTimeMillis();
//            String savePath = downloadDirectory + filename +".mp4";
//            new Download(src.group(), savePath).start();
//         }
//         catch (MoveTargetOutOfBoundsException e) {
//            currentYScroll += viewHeight;
//            js.executeScript(String.format("window.scrollTo(0, %s)", currentYScroll));
//            --i;
//         }
//      }
//      System.out.println((System.currentTimeMillis() - timer)/1000.0);
//      driver.close();
//      driver.quit();
//   }
//
}
