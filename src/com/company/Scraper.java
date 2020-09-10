package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
      FirefoxOptions options = firefoxOptions(userAgent);
      setDriverPath();
      driver = new FirefoxDriver(options);
      actions = new Actions(driver);
      js = (JavascriptExecutor) driver;
   }

   private FirefoxProfile getFirefoxProfile() {
      ProfilesIni pi = new ProfilesIni();
      FirefoxProfile fp = pi.getProfile("default");
      fp.setAcceptUntrustedCertificates(true);
      return fp;
   }

   private FirefoxOptions firefoxOptions(String userAgent) {
      FirefoxProfile p = getFirefoxProfile();
      return new FirefoxOptions().setProfile(p)
                                 .setHeadless(false)
                                 .addPreference("general.useragent.override", userAgent);
   }

   void createSaveDir() {
      new File(userId).mkdir();
   }

   void closeFirefox() {
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
   }

   void download(HashSet<String> srcs, Function<String, String> getFileName) {
      System.out.println(srcs);
      for (var src : srcs) {
         String filename =  getFileName.apply(src);
         String savePath = String.format("%s/%s", userId, filename);
         new Download(src, savePath).start();
      }
   }

   abstract HashSet<String> getSrcs();
   abstract void scrape();
   abstract void checkIfPageNotFound();
}
