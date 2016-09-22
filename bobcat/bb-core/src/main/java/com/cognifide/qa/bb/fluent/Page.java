package com.cognifide.qa.bb.fluent;

public interface Page {

  Page on(String address);

  Page path(String path);

  Page protocol(String protocol);

  Page named(String name);

  Page query(String key, String value);

  void open();

  void finish();
}
