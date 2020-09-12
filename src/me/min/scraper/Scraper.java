package me.min.scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.interactions.Actions;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class Scraper {
   String userId;
   WebDriver driver;
   JavascriptExecutor js;
   Actions actions;

   public Scraper(String userId, String userAgent) {
      this(userAgent);
      this.userId = userId;
   }

   public Scraper(String userAgent) {
//      FirefoxOptions options = firefoxOptions(userAgent);
      setDriverPath();
//      driver = new FirefoxDriver(options);
      var co = setChromeOptions(userAgent);
      driver = new ChromeDriver(co);
      actions = new Actions(driver);
      js = (JavascriptExecutor) driver;
   }

   private ChromeOptions setChromeOptions(String userAgent) {
      return new ChromeOptions().setHeadless(false)
                                .addArguments("user-agent=" + userAgent)
                                .addArguments("window-size=1300,1000");
   }

   void addCookies(String filename) {
      try {
         var fr = new FileReader(new File(filename));
         var br = new BufferedReader(fr);
         String cookie;
         while ((cookie = br.readLine()) != null) {
            String[] tokens = cookie.split(";");
            String name   = tokens[0];
            String value  = tokens[1];
            String domain = tokens[2];
            String path   = tokens[3];
            var sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date expiry   = tokens[4].equals("null") ? null : sdf.parse(tokens[4]);
            boolean isSecure = Boolean.parseBoolean(tokens[5]);
            Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
            driver.manage().addCookie(ck);
         }
         fr.close();
         br.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   void getCookies(String filename) {
      Runnable r = () -> {
         try {
            var fw = new FileWriter(new File(filename));
            var bw = new BufferedWriter(fw);
            for (Cookie c : driver.manage().getCookies()) {
               bw.write(String.format("%s;%s;%s;%s;%s;%s",
                       c.getName(), c.getValue(), c.getDomain(),
                       c.getPath(), c.getExpiry(), c.isSecure()));
               bw.newLine();
            }
            bw.close();
            fw.close();
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      };
      new Thread(r).start();
   }

   private FirefoxProfile getFirefoxProfile() {
      ProfilesIni pi = new ProfilesIni();
      FirefoxProfile fp = pi.getProfile("default");
      fp.setAcceptUntrustedCertificates(true);
      return fp;
   }

   private FirefoxOptions firefoxOptions(String userAgent) {
      FirefoxProfile p = getFirefoxProfile();
      return new FirefoxOptions()//.setProfile(p)
                                 .setHeadless(false)
                                 .addPreference("general.useragent.override", userAgent);
   }

   void createSaveDir() {
      new File(userId).mkdir();
   }

   void closeDriver() {
      driver.close();
      driver.quit();
   }

   void wait(int milliseconds) {
      try {
         TimeUnit.MILLISECONDS.sleep(milliseconds);
      }
      catch (InterruptedException e) {
         e.printStackTrace();
      }
   }


   private void setDriverPath() {
      System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
      System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
   }

   void checkIfPageNotFound(Function<String, By> by, String cssContent, String cssError) {
      while (driver.findElements(by.apply(cssContent)).isEmpty()) {
         if (!driver.findElements(by.apply(cssError)).isEmpty()) {
            System.out.println("Page not found: re-check the userId.");
            closeDriver();
            System.exit(0);
         }
         wait(100);
      }
   }

   void download(HashSet<String> srcs, Function<String, String> getFileName) {
      for (var src : srcs) {
         String filename = getFileName.apply(src);
         String savePath = String.format("%s/%s", userId, filename);
         new Download(src, savePath).start();
      }
   }

   abstract HashSet<String> getSrcs();
   abstract void scrape();
}
