package com.example.tests;

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

    this.driver = new FirefoxDriver();
    this.driver.get("http://localhost:8081/oasp4j-sample-server/");
    this.driver.findElement(By.name("userName")).sendKeys("chief");
    this.driver.findElement(By.name("password")).sendKeys("chief");
    this.driver.findElement(By.cssSelector("button.btn.btn-success")).click();
    this.driver.findElement(By.cssSelector("button.btn.navbar-btn.btn-sm.btn-info.ng-scope")).click();
    System.out.println("Test war erfolgreich. :D");
    this.driver.quit();
  }
}