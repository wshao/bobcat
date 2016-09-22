/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.bobcat.quarantine.support;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.logging.BrowserLogEntryCollector;
import com.cognifide.qa.bb.logging.entries.BrowserLogEntry;
import com.cognifide.qa.bb.logging.entries.LogEntry;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cucumber.api.Scenario;
import cucumber.api.java.After;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Container for methods fired up before and after Cucumber scenarios.
 */
public class GlobalHooks {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalHooks.class);

  @Inject
  private WebDriver webDriver;

  private final AtomicBoolean quarantineAlreadyInjected;

  @Inject
  @Named("quarantine.location")
  private String quarantineLocation;

  @Inject
  @Named("quarantine.location.prefix")
  private String quarantineLocationPrefix;

  @Inject
  @Named("quarantine.eanbled")
  private boolean quarantineEnabled;

  @Inject
  private BrowserLogEntryCollector browserLogEntryCollector;

  public GlobalHooks() {
    this.quarantineAlreadyInjected = new AtomicBoolean(false);
  }

  @After
  public void afterScenario(Scenario scenario) {
    if (scenario.isFailed()) {
      if (webDriver instanceof TakesScreenshot) {
        addScreenshot(scenario);
      }
      addPageLink(scenario);
      addJSConsoleErrors(scenario);
    }
    if (quarantineEnabled && quarantineAlreadyInjected.compareAndSet(false, true)) {
      includeQuarantineLogic(scenario);
    }
    webDriver.quit();

  }

  private void includeQuarantineLogic(Scenario scenario) {
    try {
      includeJsScript(scenario, getScriptBody("jquery-1.8.2.min.js"));
      String quarantineJs = getScriptBody("quarantine.js").
        replace("<#serviceUrl>", quarantineLocation + "/" + quarantineLocationPrefix);
      includeJsScript(scenario, quarantineJs);
    } catch (IOException ex) {
      LOG.error(ex.getMessage(), ex);
    }
  }

  private void includeJsScript(Scenario scenario, String scriptBody) throws IOException {
    scenario.write("<script type='text/javascript'>");
    scenario.write(scriptBody);
    scenario.write("</script>");
  }

  private String getScriptBody(String filePath) throws IOException {
    String result;
    try (StringWriter writer = new StringWriter()) {
      IOUtils.copy(ClassLoader.getSystemResourceAsStream(filePath), writer, "utf-8");
      result = writer.toString();
    }
    return result;
  }

  private void addJSConsoleErrors(Scenario scenario) {
    List<LogEntry> browserLogEntries = browserLogEntryCollector.getBrowserLogEntries();
    for (LogEntry browserLogEntry : browserLogEntries) {
      scenario.write("Console Error: " + ((BrowserLogEntry) browserLogEntry).getMessage());
    }
  }

  private void addPageLink(Scenario scenario) {
    scenario.write("Test page: " + "<a href=" + webDriver.getCurrentUrl() + ">link</a>");
  }

  private void addScreenshot(Scenario scenario) {
    byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
    scenario.embed(screenshot, "image/png");
  }

}
