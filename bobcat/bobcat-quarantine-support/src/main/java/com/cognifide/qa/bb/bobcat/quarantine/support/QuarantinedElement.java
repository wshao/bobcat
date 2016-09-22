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
package com.cognifide.qa.bb.bobcat.quarantine.support;

public class QuarantinedElement {

  private final String testClass;

  private final String testMethod;

  public QuarantinedElement(String id, String testCase) {
    this.testClass = id;
    this.testMethod = testCase;
  }

  public String getTestClass() {
    return testClass;
  }

  public String getTestMethod() {
    return testMethod;
  }

}
