package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Instagram extends Scraper{

   public Instagram(String userId) {
      super(userId, "Mozilla");
   }
   public Instagram() {
      super("Mozilla");
   }

   void get1post(String id) {
      driver.get("https://www.instagram.com");
      try {
         FileReader fr = new FileReader(new File("cookies"));
         BufferedReader br = new BufferedReader(fr);
         String s;
         while ((s = br.readLine()) != null) {
            StringTokenizer token = new StringTokenizer(s, ";");
            while (token.hasMoreTokens()) {
               String name = token.nextToken();
               String value = token.nextToken();
               String domain = token.nextToken();
               String path = token.nextToken();
               Date expiry = null;
               String val;
               if (!(val = token.nextToken()).equals("null")) {
                  System.out.println(val);
                  expiry = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(val);
               }
               boolean isSecure = Boolean.parseBoolean(token.nextToken());
               Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
               System.out.println(ck);
               driver.manage().addCookie(ck);
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      driver.get("https://www.instagram.com/veradijkmans/?hl=en");
//      try {
//         FileWriter fw = new FileWriter(new File("cookies"));
//         BufferedWriter bw = new BufferedWriter(fw);
//         for (Cookie cookie : driver.manage().getCookies()) {
//
//            bw.write((cookie.getName()+";"+cookie.getValue()+";"+cookie.getDomain()+";"+cookie.getPath()+";"+cookie.getExpiry()+";"+cookie.isSecure()));
//            bw.newLine();
//         }
//         bw.close();
//         fw.close();
//      }
//      catch (Exception e) {
//         e.printStackTrace();
//      }
//      HashSet<String> srcs = getSrcs();
//      System.out.println(srcs);

   }

   @Override
   void scrape() {
      driver.get("https://www.instagram.com/" + userId);
      checkIfPageNotFound();
      WebElement firstPost = driver.findElement(By.cssSelector(".v1Nh3.kIKUG._bz0w"));
      actions.moveToElement(firstPost).click().perform();
      //while (true) {
      createSaveDir();
      for (int i = 0; i <= 5; ++i) {
         waitPostToLoad();
         download(getSrcs(), this::getFileName);
         if (!nextPost()) {
            break;
         }
      }
      closeFirefox();
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
      HashSet<String> srcs = new HashSet<>();
      Pattern p = Pattern.compile("class=\"(ZyFrc|tWeCl).*?</");
      Matcher m = p.matcher(driver.getPageSource());
      do {
         while (m.find()) {
            p = Pattern.compile("(?<=src=\")[^\"]+");
            Matcher src = p.matcher(m.group());
            if (src.find()) {
               srcs.add(src.group().replace(";", "&"));
            }
         }
      } while (nextImg());
      return srcs;
   }

   boolean nextImg() {
      List<WebElement> nextImg = driver.findElements(By.className("_6CZji"));
      if (nextImg.isEmpty()) {
         return false;
      }
      actions.moveToElement(nextImg.get(0)).click().perform();
      return true;
   }

   @Override
   void checkIfPageNotFound() {
      while (driver.findElements(By.cssSelector(".v1Nh3.kIKUG._bz0w")).isEmpty()) {
         if (!driver.findElements(By.cssSelector("._07DZ3")).isEmpty()) {
            System.out.println("Page not found: re-check the userId.");
            closeFirefox();
            System.exit(0);
         }
         wait(100);
      }
   }

   private void waitPostToLoad() {
      while (!driver.findElements(By.className("jdnLC")).isEmpty()) {
         wait(100);
      }
   }

   private String getFileName(String src) {
      Matcher nameMatcher = Pattern.compile("[^/]+?(.jpg|.mp4)",
                                             Pattern.CASE_INSENSITIVE).matcher(src);
      nameMatcher.find();
      return nameMatcher.group();
   }
}
