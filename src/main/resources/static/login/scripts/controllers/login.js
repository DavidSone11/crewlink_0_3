'use strict';
/**
 * @ngdoc function
 * @name crewLinkApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the crewLinkApp
 */
angular.module('crewLinkApp')
    .controller('loginCtrl', function($scope,$position,$http, $base64,$window) {
        $scope.login = function(username, password){
            $http({
                method:'GET',
                url:'/',
                headers:{
                     'Authorization':'Basic '+ $base64.encode(username+":"+password)
                	 //'Authorization':'Basic '+ username+":"+password
                }
            }).then(function successCallback(response) {
                $window.location.href='/';
            }, function errorCallback(response) {
                alert("User not authorized.");
            });
        };
    });
