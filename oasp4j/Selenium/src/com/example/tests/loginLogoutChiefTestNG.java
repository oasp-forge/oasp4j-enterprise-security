package com.example.tests;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

//import com.thoughtworks.selenium.SeleneseTestNgHelper;

//@SuppressWarnings("javadoc")
public class loginLogoutChiefTestNG
// extends SeleneseTestNgHelper
{
  private WebDriver driver;

  // @SuppressWarnings("deprecation")
  @Test
  public void testLoginLogoutChiefTestNG() throws Exception {

    System.out.println("Test wird gestartet...");
    this.driver = new FirefoxDriver();
    this.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    this.driver.get("http://www.1und1.de");
    this.driver.findElement(By.id("button-hd-nav-webmailer")).click();
    this.driver.findElement(By.name("emaillogin.Username")).sendKeys("dustin@sb-becker.de");
    this.driver.findElement(By.name("emaillogin.Password")).sendKeys("schule");
    this.driver.findElement(By.id("ct-btn-submitbutton-lead")).click();
    this.driver.findElement(By.className("logout")).click();
    // this.driver.get("http://vm3.rbg.informatik.tu-darmstadt.de:8081/oasp4j-sample-server/");
    // this.driver.findElement(By.name("userName")).sendKeys("chief");
    // this.driver.findElement(By.name("password")).sendKeys("chief");
    // this.driver.findElement(By.cssSelector("button.btn.btn-success")).click();
    // this.driver.findElement(By.cssSelector("button.btn.navbar-btn.btn-sm.btn-info.ng-scope")).click();
    System.out.println("Test war erfolgreich. :D");
    this.driver.quit();
  }
}
