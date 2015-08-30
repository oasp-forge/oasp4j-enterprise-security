package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.Selenium;

//import com.thoughtworks.selenium.SeleneseTestNgHelper;

//@SuppressWarnings("javadoc")
public class loginLogoutChiefTestNG
// extends SeleneseTestNgHelper
{
  private Selenium selenium;

  private WebDriver driver;

  @Test
  public void testLoginLogoutChiefTestNG() throws Exception {
try {
    this.driver = new FirefoxDriver();
    this.driver.get("http://localhost:8081/oasp4j-sample-server/");
    this.driver.findElement(By.name("name=userName")).sendKeys("chief");
    this.driver.findElement(By.name("name=password")).sendKeys("chief");
    this.driver.findElement(By.cssSelector("button.btn.btn-success")).click();
    this.driver.findElement(By.xpath("//div[2]/div/button")).click();
    this.driver.wait(1000);
    System.out.println("Test war erfolgreich. :D");
} catch (Exception e) {

}
finally {

    this.driver.quit();
}
  }
}
