'use strict';
/**
 * @ngdoc function
 * @name crewLinkApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the crewLinkApp
 */
angular.module('crewLinkApp')
  .controller('MainCtrl', function($scope,$position) {
      $scope.login = function(username, password){
        alert(username+password);
      };
  });
