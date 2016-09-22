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
(function ($) {
  var Bobcat = {};

  Bobcat.Quarantine = (function () {
    var api = {};

    //api.quarantinedTests = getQuarantinedTests('http://localhost:8080/quarantines');

    api.redrawWithQuarantineContext = function () {
      var features = $('.feature-line');
      $.each(features, function (index, feature) {
        var featureName = $(feature).text().replace('Feature: ', '').trim();
        ;
        var scenarios = $(feature).parent().parent().find('.scenario-name');
        $.each(scenarios, function (index, scenario) {
          var scenarioName = $(scenario).text().trim();
          var testInfo = {
            testClassName: featureName,
            testMethodName: scenarioName
          };
          $(scenario).parent().find('button').remove();
          if (!api.isTestQuarantined(testInfo)) {
            $(scenario).parent().append('<button quarantine-action="add"> Add to quarantine </button>').click(function (event) {
              api.putTestIntoQuarantine(testInfo, 'https://192.168.37.49:8080/quarantines');
            });
          } else {
            $(scenario).parent().append('<button quarantine-action="delete"> Remove from quarantine </button>').click(function (event) {
              api.removeFromQuarantine(testInfo, 'https://192.168.37.49:8080/quarantines');
            });
          }
        })
      });

    };

    api.loadQuarantinedTests = function (url) {
      $.ajax
        ({
          type: "GET",
          url: url,
          success: function (data)
          {
            api.quarantinedTests = data;
            api.redrawWithQuarantineContext();
          }
        });
    };

    api.isTestQuarantined = function (testInfo) {
      for (var i = 0; i < api.quarantinedTests.length; i++) {
        if (api.quarantinedTests[i].testClassName === testInfo.testClassName
          && api.quarantinedTests[i].testMethodName === testInfo.testMethodName) {
          return true;
        }
      }
      return false;
    };

    api.putTestIntoQuarantine = function (testInfo, url) {
      $.ajax
        ({
          type: "POST",
          url: url,
          data: JSON.stringify(testInfo),
          success: function () {
            api.loadQuarantinedTests('https://192.168.37.49:8080/quarantines');
          }
        });
    };
    api.removeFromQuarantine = function (testInfo, url) {
      $.ajax
        ({
          type: "DELETE",
          url: url + '?' + $.param({"testClass": testInfo.testClassName, "testMethod": testInfo.testMethodName}),
          success: function () {
            api.loadQuarantinedTests('https://192.168.37.49:8080/quarantines');
          }
        });
    };

    var registerEventHandlers = function () {
      $('button[quarantine-action="delete"]').click(function (event) {
        var testInfo = {
          testClassName: this.closest('div').getAttribute('test-class'),
          testMethodName: this.closest('div').getAttribute('test-method')
        };
        quarantine($).removeFromQuarantine(testInfo);
      });
      $('button[quarantine-action="add"]').click(function (event) {
        var testInfo = {
          testClassName: this.closest('div').getAttribute('test-class'),
          testMethodName: this.closest('div').getAttribute('test-method')
        };
        quarantine($).putTestIntoQuarantine(testInfo);
      });
    };

    return api;


  })

  $(document).ready(function () {
    Bobcat.Quarantine().loadQuarantinedTests('https://192.168.37.49:8080/quarantines');
  });

})(jQuery);





