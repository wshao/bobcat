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
package com.cognifide.qa.bobcumber;

import java.util.List;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.logging.BrowserLogEntryCollector;
import com.cognifide.qa.bb.logging.entries.BrowserLogEntry;
import com.cognifide.qa.bb.logging.entries.LogEntry;
import com.google.inject.Inject;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * Container for methods fired up before and after Cucumber scenarios.
 */
public class GlobalHooks {

  @Inject
  private WebDriver webDriver;

  private volatile boolean jsInjected;

  @Inject
  private BrowserLogEntryCollector browserLogEntryCollector;

  @Before
  public void before(Scenario scenario) {
  }

  @After
  public void afterScenario(Scenario scenario) {
    if (scenario.isFailed()) {
      if (webDriver instanceof TakesScreenshot) {
        addScreenshot(scenario);
      }
      addPageLink(scenario);
      addJSConsoleErrors(scenario);
      if (!jsInjected) {
        synchronized (this) {
          try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(ClassLoader.getSystemResourceAsStream("jquery-1.8.2.min.js"), writer, "utf-8");
            String jQuery = writer.toString();
            scenario.write("<script type='text/javascript'>");
            scenario.write(jQuery);
            scenario.write("</script>");
            writer.flush();
            IOUtils.copy(ClassLoader.getSystemResourceAsStream("quarantine.js"), writer, "utf-8");
            String js = writer.toString();
            scenario.write("<script type='text/javascript'>");
            scenario.write(js);
            scenario.write("</script>");
            jsInjected = true;
          } catch (IOException ex) {
            Logger.getLogger(GlobalHooks.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
    }

    webDriver.quit();

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
