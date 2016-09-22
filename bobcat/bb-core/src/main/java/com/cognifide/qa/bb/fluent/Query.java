package com.cognifide.qa.bb.fluent;

public class Query {

  private String key;

  private String value;

  private Query(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public static Query of(String key, String value) {
    return new Query(key, value);
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
