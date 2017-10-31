'use strict';

angular.module('crewLinkApp')
    .directive('pageSelect', function() {
      return {
        restrict: 'E',
        template: '<input type="text" class="select-page" ng-model="inputPage" st-delay="1000" ng-change="selectPage(inputPage)">',
        link: function(scope, element, attrs) {
          scope.$watch('currentPage', function(c) {
        	  if(scope.inputPage != c){
        		  scope.inputPage = c;
        		  scope.$parent.$parent.currentPage = c;
        	  }
          });
          
          
          scope.$watch('inputPage', function(c) {
        	  if(scope.inputPage <= scope.$parent.numPages && scope.inputPage  >= 1 )
    		  scope.$parent.$parent.currentPage = scope.inputPage;
          });
          
          
          
        }
      }
    }).directive('stRatio',function(){
        return {
            link:function(scope, element, attr){
              var ratio=+(attr.stRatio);
              
              element.css('width',ratio+'%');
              
            }
          };
      })
      .directive('stBindModel',function(){
    	  
    	  return{
    		  restrict: "AE",
              require: '^stTable',
    		  link:function(scope, element, attrs,ctrl){
    			  attrs.$observe('stBindModel', function (val) {
                      ctrl.pipe();
                  });
    		  }
    		  
    	  }
      });