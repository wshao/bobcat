package com.cognifide.qa.bb.fluent;

import java.util.List;

public class QueryUtils {

  private QueryUtils() {
    //util class..
  }

  public static String toString(List<Query> queries) {
    StringBuilder accumulator = new StringBuilder();
    queries.forEach(q -> {
      accumulator.append(q.getKey() + "=" + q.getValue() + "&");
    });
    String query = accumulator.toString();
    return query.substring(0, query.length() - 1);
  }

}
