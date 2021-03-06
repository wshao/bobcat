/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.aem.touch.siteadmin.aem61.list;

import java.util.List;

import javax.annotation.Nullable;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;

@PageObject
public class ChildPageWindow {

  @Inject
  private WebElementUtils webElementUtils;

  @Inject
  private CurrentScopeHelper currentScopeHelper;

  @FindBy(css = "article.foundation-collection-item.card-page")
  private List<ChildPageRow> childPages;

  @FindBy(css = "header.card-page.selectable i.select")
  @Global
  private WebElement selectAllPagesButton;

  public ChildPageRow getChildPageRow(String pageTitle) {
    return childPages.stream().filter(t -> t.getTitle().equals(pageTitle)).findFirst().
        orElseThrow(() -> new IllegalStateException("Page not found in the current context"));
  }

  public ChildPageRow selectPage(String pageTitle) {
    return getChildPageRow(pageTitle).select();
  }

  public void pressSelectAllPages() {
    selectAllPagesButton.click();
  }

  public boolean isLoaded() {
    return currentScopeHelper.isCurrentScopeVisible(this);
  }

  public boolean containsPage(String title) {
    return webElementUtils.isConditionMet(new ExpectedCondition<Object>() {
      @Nullable @Override public Object apply(@Nullable WebDriver weDriver) {
        try {
          return childPages.stream().filter(t -> t.getTitle().equals(title)).findFirst()
              .isPresent();
        } catch (StaleElementReferenceException e) {
          weDriver.navigate().refresh();
          return false;
        }
      }
    }, Timeouts.SMALL);
  }

  public boolean hasSubPages() {
    return !childPages.isEmpty();
  }

  public int getPageCount() {
    return childPages.size();
  }
}
