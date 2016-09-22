package com.cognifide.qa.bb.fluent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageObject {

  private String address = "";

  private String protocol = "";

  private String path = "";

  private List<Query> queryList = new ArrayList<>();

  public List<Query> getQueryList() {
    return queryList;
  }

  public void setQueryList(List<Query> queryList) {
    this.queryList = queryList;
  }

  PageObject() {
  }

  PageObject(String address, String protocol, String path, Query... queryList) {
    this.address = address;
    this.protocol = protocol;
    this.path = path;
    Arrays.asList(queryList).forEach(e -> this.queryList.add(e));
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
    queryList.add(Query.of(key, value));
  }

  @Override
  public String toString() {
    String path = this.path.startsWith("/") ? this.path : "/" + this.path;
    String queries = anyQuery(queryList) ? "?" + QueryUtils.toString(queryList) : "";
    return protocol + "://" + address + path + queries;
  }

  private boolean anyQuery(List<Query> queries) {
    return queries != null && !queries.isEmpty();
  }
}
