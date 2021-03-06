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
package com.cognifide.qa.bb.aem.touch.siteadmin.aem62.calendar;

import java.time.Month;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class YearMonthPicker {

  @FindBy(css = "div.coral-Heading[handle='heading']")
  private WebElement yearMonthHeader;

  @FindBy(css = ".coral-Calendar-nextMonth")
  private WebElement nextMonthButton;

  public void selectDate(int year, Month month) {
    while (!isDesiredDateSelected(year, month)) {
      nextMonthButton.click();
    }
  }

  private boolean isDesiredDateSelected(int year, Month month) {
    String[] headerParts = yearMonthHeader.getText().toUpperCase().split(" ");
    Month selectedMonth = Month.valueOf(headerParts[0]);
    int selectedYear = Integer.valueOf(headerParts[1].toUpperCase());
    return year == selectedYear && selectedMonth.equals(month);
  }

}
