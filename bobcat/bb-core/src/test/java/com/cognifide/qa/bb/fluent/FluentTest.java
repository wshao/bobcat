package com.cognifide.qa.bb.fluent;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class FluentTest {

  @BeforeClass
  public static void setUp() {
    Properties props = System.getProperties();
    props.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
  }

  @Test
  public void customPageTest() {
    Page wpPagePoc = new PagePoc();
    wpPagePoc
        .protocol("https")
        .on("absa.co.za")
        .path("asda")
        .query("key", "value")
        .query("key", "second")
        .open();
    wpPagePoc.finish();
  }

  @Test
  public void namedPageTest() {
    Page page = new PagePoc();
    page.named("absa").open();
    page.finish();
  }

  @Test
  public void namedPageTest2() {
    Page page = new PagePoc();
    page.named("bobcat").open();
    page.finish();
  }
}
