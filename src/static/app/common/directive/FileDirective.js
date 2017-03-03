/**
 * fileDirective
 */
angular.module('dbflute-intro').directive('fileInput', function() {
  'use strict';
  return {
    require: '^ngModel',
    restrict: 'A',
    link: function(scope, element, attrs, model) {
      var onChangeHandler = scope.$eval(attrs.ngFileChange);
      element.bind('change', function(event) {
        var files = event.target.files;
        onChangeHandler(files, model.$modelValue);
      });
      // 同一ファイル名で内容が異なる場合に 対応
      element.bind('click', function(event) {
        var files = event.target.files;
        onChangeHandler(files, model.$modelValue);
      });
    }
  };
});
