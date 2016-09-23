package com.cognifide.qa.bb.fluent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class SiteNames {

  private static final Map<String, PageObject> names;

  static {
    PageObject wikipediaPage = new PageObject("pl.wikipedia.org", "https", StringUtils.EMPTY, null);
    PageObject onetPage = new PageObject("onet.pl", "http", StringUtils.EMPTY, null);
    PageObject bobcatPage = new PageObject("github.com", "https", "Cognifide/bobcat", null);

    Map<String, String> absaQueries = new HashMap<>();
    absaQueries.put("q", "absa");
    PageObject absaSearchPage = new PageObject("absa.co.za", "https", "search-results/", absaQueries);

    names = new HashMap<>();
    names.put("wikipedia", wikipediaPage);
    names.put("onet", onetPage);
    names.put("bobcat", bobcatPage);
    names.put("absa", absaSearchPage);
  }

  public static PageObject named(String name) {
    return names.get(name);
  }
}
