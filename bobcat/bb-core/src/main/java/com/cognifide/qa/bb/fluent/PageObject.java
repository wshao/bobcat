package com.cognifide.qa.bb.fluent;

import java.util.HashMap;
import java.util.Map;

public class PageObject {

  private String address = "";

  private String protocol = "";

  private String path = "";

  private Map<String, String> queries = new HashMap<>();

  PageObject() {
  }

  PageObject(String address, String protocol, String path, Map<String, String> queries) {
    this.address = address;
    this.protocol = protocol;
    this.path = path;
    this.queries = queries;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  //TODO - should check if key already exists and add that query
  public void addQuery(String key, String value) {
    if (queries.containsKey(key)) {
      String currentValue = queries.get(key);
      queries.put(key, currentValue + "," + value);
    } else {
      queries.put(key, value);
    }
  }

  @Override
  public String toString() {
    String path = this.path.startsWith("/") ? this.path : "/" + this.path;
    String queries = anyQuery() ? "?" + queriesAsString() : "";
    return protocol + "://" + address + path + queries;
  }

  private boolean anyQuery() {
    return queries != null && !queries.isEmpty();
  }

  private String queriesAsString() {
    StringBuilder stringBuilder = new StringBuilder();
    queries.forEach((k, v) -> {
      stringBuilder.append(k);
      stringBuilder.append("=");
      stringBuilder.append(v);
      stringBuilder.append("&");
    });
    return stringBuilder.substring(0, stringBuilder.length() - 1);
  }
}
