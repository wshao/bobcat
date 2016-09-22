/* 
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function ($) {
  var Bobcat = {};

  Bobcat.Quarantine = (function (url) {
    var api = {};
    var serviceUrl = url;

    api.redrawWithQuarantineContext = function () {
      var features = $('.feature-line');
      $.each(features, function (index, feature) {
        var featureName = $(feature).text().replace('Feature: ', '').trim();
        ;
        var scenarios = $(feature).parent().parent().find('.scenario-name');
        $.each(scenarios, function (index, scenario) {

          var scenarioName = $(scenario).text().trim();
          var havePassed = $(scenario).closest('div').hasClass('passed');

          var testInfo = {
            featureName: featureName,
            scenarioName: scenarioName
          };

          $(scenario).parent().find('button').remove();
          if (!api.isTestQuarantined(testInfo) && !havePassed) {
            $(scenario).parent().append('<button quarantine-action="add"> Add to quarantine </button>').click(function (event) {
              api.putTestIntoQuarantine(testInfo);
            });
          } else if (api.isTestQuarantined(testInfo)) {
            $(scenario).parent().append('<button quarantine-action="delete"> Remove from quarantine </button>').click(function (event) {
              api.removeFromQuarantine(testInfo);
            });
          }
        });
      });
    };

    api.loadQuarantinedTests = function () {
      $.ajax
        ({
          type: 'GET',
          url: serviceUrl,
          success: function (data)
          {
            api.quarantinedTests = data;
            api.redrawWithQuarantineContext();
          }
        });
    };

    api.isTestQuarantined = function (testInfo) {
      for (var i = 0; i < api.quarantinedTests.length; i++) {
        if (api.quarantinedTests[i].featureName === testInfo.featureName
          && api.quarantinedTests[i].scenarioName === testInfo.scenarioName) {
          return true;
        }
      }
      return false;
    };

    api.putTestIntoQuarantine = function (testInfo) {
      $.ajax
        ({
          type: 'POST',
          url: serviceUrl,
          data: JSON.stringify(testInfo),
          success: function () {
            api.loadQuarantinedTests();
          }
        });
    };
    api.removeFromQuarantine = function (testInfo) {
      $.ajax
        ({
          type: 'DELETE',
          url: serviceUrl + '?' +
            $.param({'featureName': testInfo.featureName, 'scenarioName': testInfo.scenarioName}),
          success: function () {
            api.loadQuarantinedTests();
          }
        });
    };
    return api;
  });

  $(document).ready(function () {
    Bobcat.Quarantine('<#serviceUrl>').loadQuarantinedTests();
  });
})(jQuery);