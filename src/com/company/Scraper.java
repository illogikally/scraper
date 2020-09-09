package com.company;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public abstract class Scraper {
   String userId;
   WebDriver driver;
   JavascriptExecutor js;
   Actions actions;

   public Scraper(String userId) {
      this.userId = userId;
      FirefoxOptions options = firefoxOptions();
      setDriverPath();
      driver = new FirefoxDriver(options);
      actions = new Actions(driver);
      js = (JavascriptExecutor) driver;
   }

   private FirefoxProfile getFirefoxProfile() {
      FirefoxProfile p = new ProfilesIni().getProfile("default");
      p.setAcceptUntrustedCertificates(true);
      return p;
   }

   private FirefoxOptions firefoxOptions() {
      FirefoxProfile profile = getFirefoxProfile();
      return new FirefoxOptions().setProfile(profile)
                                 .setHeadless(false)
                                 .addPreference("general.useragent.override", "Naverbot");
   }

   void createSaveDir() {
      new File(userId).mkdir();
   }

   void wait(int milliseconds) {
      try {
         TimeUnit.MILLISECONDS.sleep(milliseconds);
      }
      catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   void setDriverPath() {
      System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
   }

   abstract HashSet<String> getSrc();
   abstract void download(HashSet<String> srcs);
   abstract boolean isPageLoaded();
   abstract void scrape();
}
