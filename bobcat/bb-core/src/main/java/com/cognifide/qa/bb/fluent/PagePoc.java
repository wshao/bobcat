package com.cognifide.qa.bb.fluent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PagePoc implements Page {

  private WebDriver webDriver;

  private PageObject pageObject;

  public PagePoc() {
    this.webDriver = new ChromeDriver();
    this.pageObject = new PageObject();
  }

  @Override
  public Page on(String address) {
    pageObject.setAddress(address);
    return this;
  }

  @Override
  public Page path(String path) {
    pageObject.setPath(path);
    return this;
  }

  @Override
  public Page protocol(String protocol) {
    pageObject.setProtocol(protocol);
    return this;
  }

  @Override
  public Page named(String name) {
    pageObject = SiteNames.named(name);
    return this;
  }

  @Override
  public Page query(String key, String value) {
    pageObject.addQuery(key, value);
    return this;
  }

  @Override
  public void open() {
    webDriver.get(pageObject.toString());
  }

  @Override public void finish() {
    webDriver.close();
  }
}
