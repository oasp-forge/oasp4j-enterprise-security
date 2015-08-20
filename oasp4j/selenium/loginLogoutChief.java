package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class loginLogoutChief {
	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://localhost:8081/");
		selenium.start();
	}

	@Test
	public void testLoginLogoutChief() throws Exception {
		selenium.open("/oasp4j-example-application/");
		selenium.type("name=userName", "chief");
		selenium.type("name=password", "chief");
		selenium.click("css=button.btn.btn-success");
		selenium.click("//div[2]/div/button");
		selenium.waitForPageToLoad("3000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
