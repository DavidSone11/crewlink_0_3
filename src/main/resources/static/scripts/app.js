'use strict';
/**
 * @ngdoc overview
 * @name crewLinkApp
 * @description
 * # crewLinkApp
 *
 * Main module of the application.
 */

var RestApi = {};
RestApi.base = "/api";

RestApi.roles = RestApi.base + '/roles';
RestApi.crewLinks = RestApi.base + '/crewLinks';
RestApi.crewTypes = RestApi.base + '/crewTypes';
RestApi.divisions = RestApi.base + '/divisions';
RestApi.drivingDuties = RestApi.base + '/drivingDuties';
RestApi.drivingDutyElements = RestApi.base + '/drivingDutyElements';
RestApi.drivingSections = RestApi.base + '/drivingSections';
RestApi.headStations = RestApi.base + '/headStations';
RestApi.outStations = RestApi.base + '/outStations';
RestApi.pilotTrips = RestApi.base + '/pilotTrips';
RestApi.roles = RestApi.base + '/roles';
RestApi.roundTrips = RestApi.base + '/roundTrips';
RestApi.stations = RestApi.base + '/stations';
RestApi.subRoles = RestApi.base + '/subRoles';
RestApi.trains = RestApi.base + '/trains';
RestApi.trainStations = RestApi.base + '/trainStations';
RestApi.trainTypes = RestApi.base +'/trainTypes';
RestApi.users = RestApi.base + '/users';
RestApi.userPlans = RestApi.base + '/userPlans';

var APIobj = {
		  "_links" : {
			    "drivingSections" : {
			      "href" : "/api/drivingSections{?page,size,sort}",
			      "templated" : true
			    },
			    "roundTrips" : {
			      "href" : "/api/roundTrips{?page,size,sort}",
			      "templated" : true
			    },
			    "trainTypes" : {
			      "href" : "/api/trainTypes{?page,size,sort}",
			      "templated" : true
			    },
			    "headStations" : {
			      "href" : "/api/headStations{?page,size,sort}",
			      "templated" : true
			    },
			    "divisions" : {
			      "href" : "/api/divisions{?page,size,sort}",
			      "templated" : true
			    },
			    "drivingDutyElements" : {
			      "href" : "/api/drivingDutyElements{?page,size,sort}",
			      "templated" : true
			    },
			    "stations" : {
			      "href" : "/api/stations{?page,size,sort}",
			      "templated" : true
			    },
			    "pilotTypes" : {
			      "href" : "/api/pilotTypes{?page,size,sort}",
			      "templated" : true
			    },
			    "subRoles" : {
			      "href" : "/api/subRoles{?page,size,sort}",
			      "templated" : true
			    },
			    "drivingDuties" : {
			      "href" : "/api/drivingDuties{?page,size,sort}",
			      "templated" : true
			    },
			    "roles" : {
			      "href" : "/api/roles{?page,size,sort}",
			      "templated" : true
			    },
			    "crewTypes" : {
			      "href" : "/api/crewTypes{?page,size,sort}",
			      "templated" : true
			    },
			    "outStations" : {
			      "href" : "/api/outStations{?page,size,sort}",
			      "templated" : true
			    },
			    "trainStations" : {
			      "href" : "/api/trainStations{?page,size,sort}",
			      "templated" : true
			    },
			    "users" : {
			      "href" : "/api/users{?page,size,sort}",
			      "templated" : true
			    },
			    "pilotTrips" : {
			      "href" : "/api/pilotTrips{?page,size,sort}",
			      "templated" : true
			    },
			    "crewLinks" : {
			      "href" : "/api/crewLinks{?page,size,sort}",
			      "templated" : true
			    },
			    "trains" : {
			      "href" : "/api/trains{?page,size,sort}",
			      "templated" : true
			    },
			    "userPlans" : {
			      "href" : "/api/userPlans{?page,size,sort}",
			      "templated" : true
			    },
			    "profile" : {
			      "href" : "/api/profile"
			    }
			  }
			};

var app = angular
  .module('crewLinkApp', [
    'oc.lazyLoad',
    'ui.router',
    'ui.bootstrap',
    'angular-loading-bar',
    'flow',
    'ngResource',
    'spring-data-rest',
    'spring-data-rest-api',
    'ngSanitize',
    'angucomplete-alt',
    'smart-table',
    'ngAnimate',
    'AxelSoft',
    'ngCookies',
    'angular-confirm',
    'toaster',
    'ngSanitize',
    'ngCsv'
  ]).factory('TokenInterceptor', function($q, $window,$location) {
	  return {
		    request: function(config) {
		      config.headers = config.headers || {};
		      if ($window.sessionStorage.token) {
		        config.headers['X-Access-Token'] = $window.sessionStorage.token;
		        config.headers['X-Key'] = $window.sessionStorage.user;
		        config.headers['Content-Type'] = config.headers['Content-Type'] || "application/json";
		      }
		      return config || $q.when(config);
		    },
		 
		    response: function(response) {
		    	if(response.status === 401 || response.status === 403) {
	                /* I need to resend the same request with token included here 
	                 * if the token exist in local storage
	                 */
	                $location.path('/login');
	            }
		      return response || $q.when(response);
		    }
		  };
		})
  .config(['$stateProvider','$urlRouterProvider','$ocLazyLoadProvider','SpringDataRestApiProvider','$httpProvider',
           function ($stateProvider,$urlRouterProvider,$ocLazyLoadProvider,SpringDataRestApiProvider,$httpProvider) {
	SpringDataRestApiProvider.config({
		baseApiUrl:RestApi.base,
		debug:true,
		putResponseBody:true,
		postResponseBody:true
	});
	SpringDataRestApiProvider.APIs(APIobj);
	
	
    
    $ocLazyLoadProvider.config({
      debug:false,
      events:true,
    });

    $urlRouterProvider.otherwise('/dashboard/userPlans');

    $stateProvider
      .state('dashboard', {
        url:'/dashboard',
        templateUrl: 'views/dashboard/main.html',
        resolve: {
            loadMyDirectives:function($ocLazyLoad){
                return $ocLazyLoad.load(
                {
                    name:'crewLinkApp',
                    files:[
                    'scripts/directives/header/header.js',
                    'scripts/directives/header/header-notification/header-notification.js',
                    'scripts/directives/sidebar/sidebar.js',
                    'scripts/directives/sidebar/sidebar-search/sidebar-search.js',
                    'scripts/utils/customSearch.js',
                    'scripts/utils/timeFilter.js',
                    'scripts/utils/serverTableFetch.js',
                    'scripts/services/userService.js',
                    'scripts/services/chartService.js',
                    'scripts/services/timeCal.js',
                    'scripts/directives/pagination/pageSelect.directive.js',
                    'scripts/directives/mathoChart/mathoChart.js'
                    ]
                }),
                $ocLazyLoad.load(
                {
                   name:'toggle-switch',
                   files:["bower_components/angular-toggle-switch/angular-toggle-switch.min.js",
                          "bower_components/angular-toggle-switch/angular-toggle-switch.css"
                      ]
                }),
                $ocLazyLoad.load(
                {
                  name:'ngAnimate',
                  files:['bower_components/angular-animate/angular-animate.js']
                })
                $ocLazyLoad.load(
                {
                  name:'ngCookies',
                  files:['bower_components/angular-cookies/angular-cookies.js']
                })
                $ocLazyLoad.load(
                {
                  name:'ngResource',
                  files:['bower_components/angular-resource/angular-resource.js']
                })
                $ocLazyLoad.load(
                {
                  name:'ngSanitize',
                  files:['bower_components/angular-sanitize/angular-sanitize.js']
                })
                $ocLazyLoad.load(
                {
                  name:'ngTouch',
                  files:['bower_components/angular-touch/angular-touch.js']
                })
            }
        }
    })
      .state('dashboard.home',{
        url:'/home',
        controller: 'MainCtrl',
        templateUrl:'views/dashboard/home.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/main.js',
              'scripts/directives/timeline/timeline.js',
              'scripts/directives/notifications/notifications.js',
              'scripts/directives/chat/chat.js',
              'scripts/directives/dashboard/stats/stats.js'
              ]
            })
          }
        }
      })
      
      .state('dashboard.trains',{
        url:'/trains',
        controller: 'TrainsCtrl',
        templateUrl:'views/dashboard/trains.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/trains.js',
              'scripts/directives/dashboard/trainForm/trainForm.js',
              ]
            })
          }
        }
      })
      .state('dashboard.trainTimeTable',{
        url:'/trainTimeTable/:trainNo/:startDay',
        controller: 'TrainTimeTableCtrl',
        templateUrl:'views/dashboard/trainTimeTable.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/trainTimeTable.js'
              ]
            })
          }
        }
      })
       .state('dashboard.drivingSection',{
        url:'/drivingSection/:trainNo/:startDay',
        controller: 'DrivingSectionCtrl',
        templateUrl:'views/dashboard/drivingSection.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/drivingSection.js'
              ]
            })
          }
        }
      })
      .state('dashboard.drivingSectionForm',{
        url:'/drivingSectionForm/:trainNo/:startDay',
        controller: 'DrivingSectionFormCtrl',
        templateUrl:'views/dashboard/drivingSectionForm.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/drivingSectionForm.js'
              ]
            })
          }
        }
      })
       .state('dashboard.drivingSectionChart',{
        url:'/drivingSectionChart',
        controller: 'DrivingSectionChartCtrl',
        templateUrl:'views/dashboard/drivingSectionChart.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/drivingSectionChart.js'
              ]
            })
          }
        }
      })
      .state('dashboard.drivingDuty',{
        url:'/drivingDuty',
        controller: 'DrivingDutyCtrl',
        templateUrl:'views/dashboard/drivingDuty.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/drivingDuty.js'
              ]
            })
          }
        }
      })
      .state('dashboard.drivingDutyForm',{
        url:'/drivingDutyForm',
        controller: 'DrivingDutyFormCtrl',
        templateUrl:'views/dashboard/drivingDutyForm.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/drivingDutyForm.js'
              ]
            })
          }
        }
      })
      
      
      .state('dashboard.roundTripForm',{
        url:'/roundTrip/:fromStation',
        controller: 'RoundTripFormCtrl',
        templateUrl:'views/dashboard/roundTripForm.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/roundTripFrom.js',
              'scripts/directives/createRoundTrip/createRoundTripChart.js'
              ]
            })
          }
        }
      })
      .state('dashboard.roundTripAnalysis',{
        url:'/roundTripAnalysis/:isAnalysis',
        controller: 'RoundTripFormCtrl',
        templateUrl:'views/dashboard/roundTripForm.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/roundTripFrom.js',
              'scripts/directives/createRoundTrip/createRoundTripChart.js'
              ]
            })
          }
        }
      })
      .state('dashboard.roundTrip',{
        url:'/roundTripListing',
        controller: 'RoundTripCtrl',
        templateUrl:'views/dashboard/roundTrip.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/roundTrip.js',
              ]
            })
          }
        }
      })
          .state('dashboard.crewLink',{
          url:'/CrewLink',
          controller: 'CrewLinkCtrl',
          templateUrl:'views/dashboard/crewLink.html',
          resolve: {
            loadMyFiles:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                name:'crewLinkApp',
                files:[
                'scripts/controllers/crewLink.js',
                'scripts/directives/crewLinkAnalysisChart/crewLinkAnalysisChart.js'
                ]
              })
            }
          }
        })
       .state('dashboard.crewLinkStationSummary',{
          url:'/crewLinkStationSummary',
          controller: 'CrewLinkStationSummaryCtrl',
          templateUrl:'views/dashboard/crewLinkStationSummary.html',
          resolve: {
            loadMyFiles:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                name:'crewLinkApp',
                files:[
                'scripts/controllers/crewLinkStationSummary.js',
                ]
              })
            }
          }
        })
      .state('dashboard.crewLinkForm',{
          url:'/CreateCrewLink',
          controller: 'CrewLinkFormCtrl',
          templateUrl:'views/dashboard/crewLinkForm.html',
          resolve: {
            loadMyFiles:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                name:'crewLinkApp',
                files:[
                'scripts/controllers/crewLinkFrom.js'
                ]
              })
            }
          }
        })
   
      
      
       .state('dashboard.users',{
        url:'/users',
        controller: 'UsersCtrl',
        templateUrl:'views/dashboard/users.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/users.js',
              'scripts/directives/dashboard/userForm/userForm.js'
              ]
            })
          }
        }
      }).
      state('dashboard.newPilotCreation',{
          url:'/newPilot',
          controller: 'NewPilotCtrl',
          templateUrl:'views/dashboard/newPilot.html',
          resolve: {
            loadMyFiles:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                name:'crewLinkApp',
                files:[
                'scripts/controllers/newPilot.js'
                ]
              })
            }
          }
        }).
        state('dashboard.listPilots',{
            url:'/listPilot',
            controller: 'ListPilotCtrl',
            templateUrl:'views/dashboard/listPilot.html',
            resolve: {
              loadMyFiles:function($ocLazyLoad) {
                return $ocLazyLoad.load({
                  name:'crewLinkApp',
                  files:[
                  'scripts/controllers/listPilot.js'
                  ]
                })
              }
            }
          })
     .state('dashboard.searchItems',{
        url:'/searchItems',
        controller: 'SearchItemsCtrl',
        templateUrl:'views/dashboard/searchItems.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/searchItems.js'
              ]
            })
          }
        }
      })
      .state('dashboard.improveDrivingSection',{
        url:'/improveDrivingSection',
        controller: 'ImproveDrivingSectionCtrl',
        templateUrl:'views/dashboard/improveDrivingSection.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/improveDrivingSection.js'
              ]
            })
          }
        }
      })
      .state('dashboard.improveLongDrivingDuty',{
        url:'/improveLongDrivingDuty',
        controller: 'ImproveLongDrivingDutyCtrl',
        templateUrl:'views/dashboard/improveLongDrivingDuty.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/improveLongDrivingDuty.js'
              ]
            })
          }
        }
      })
      .state('dashboard.improveDrivingDutyWithDailyPilots',{
        url:'/improveDrivingDutyWithDailyPilots',
        controller: 'ImproveDrivingDutyWithDailyPilotsCtrl',
        templateUrl:'views/dashboard/improveDrivingDutyWithDailyPilots.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/improveDrivingDutyWithDailyPilots.js'
              ]
            })
          }
        }
      })
      .state('dashboard.improveRoundTripWithExcessOSR',{
        url:'/improveRoundTripWithExcessOSR',
        controller: 'ImproveRoundTripWithExcessOSRCtrl',
        templateUrl:'views/dashboard/improveRoundTripWithExcessOSR.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/improveRoundTripWithExcessOSR.js'
              ]
            })
          }
        }
      })
      .state('dashboard.stations',{
        url:'/stations',
        controller: 'StationsCtrl',
        templateUrl:'views/dashboard/stations.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/stations.js',
              'scripts/directives/dashboard/stationForm/stationForm.js'
              ]
            })
          }
        }
      })
      .state('dashboard.userPlans',{
        url:'/userPlans',
        controller: 'UserPlansCtrl',
        templateUrl:'views/dashboard/userPlans.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/userPlans.js'
              ]
            })
          }
        }
      })
       .state('dashboard.divisions',{
        url:'/divisions',
        controller: 'DivisionsCtrl',
        templateUrl:'views/dashboard/divisions.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/divisions.js',
              'scripts/directives/dashboard/divisionForm/divisionForm.js'
              ]
            })
          }
        }
      })
      .state('dashboard.pilotTypes',{
        url:'/pilotTypes',
        controller: 'PilotTypesCtrl',
        templateUrl:'views/dashboard/pilotTypes.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/pilotTypes.js',
              'scripts/directives/dashboard/pilotTypeForm/pilotTypeForm.js'
              ]
            })
          }
        }
      })
      .state('dashboard.pilotTrips',{
        url:'/pilotTrips',
        controller: 'PilotTripsCtrl',
        templateUrl:'views/dashboard/pilotTrips.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/pilotTrips.js',
              'scripts/directives/dashboard/pilotTripForm/pilotTripForm.js'
              ]
            })
          }
        }
      })
       .state('dashboard.crewTypes',{
        url:'/crewTypes',
        controller: 'CrewTypesCtrl',
        templateUrl:'views/dashboard/crewTypes.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/crewTypes.js',
              'scripts/directives/dashboard/crewTypeForm/crewTypeForm.js'
              ]
            })
          }
        }
      })
      .state('dashboard.trainTypes',{
        url:'/trainTypes',
        controller: 'TrainTypesCtrl',
        templateUrl:'views/dashboard/trainTypes.html',
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'crewLinkApp',
              files:[
              'scripts/controllers/trainTypes.js',
              'scripts/directives/dashboard/trainTypeForm/trainTypeForm.js'
              ]
            })
          }
        }
      })
    .state('dashboard.upload',{
        templateUrl:'views/pages/fileUpload/trainDetailsUpload.html',
        url:'/upload'
    })
      .state('dashboard.form',{
        templateUrl:'views/form.html',
        url:'/form'
    })
      .state('dashboard.blank',{
        templateUrl:'views/pages/blank.html',
        url:'/blank'
    })
      .state('login',{
        templateUrl:'views/pages/login.html',
        url:'/login'
    })
      .state('dashboard.chart',{
        templateUrl:'views/chart.html',
        url:'/chart',
        controller:'ChartCtrl',
        resolve: {
          loadMyFile:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'chart.js',
              files:[
                'bower_components/angular-chart.js/dist/angular-chart.min.js',
                'bower_components/angular-chart.js/dist/angular-chart.css'
              ]
            }),
            $ocLazyLoad.load({
                name:'crewLinkApp',
                files:['scripts/controllers/chartContoller.js']
            })
          }
        }
    })
      .state('dashboard.table',{
        templateUrl:'views/table.html',
        url:'/table'
    })
      .state('dashboard.panels-wells',{
          templateUrl:'views/ui-elements/panels-wells.html',
          url:'/panels-wells'
      })
      .state('dashboard.buttons',{
        templateUrl:'views/ui-elements/buttons.html',
        url:'/buttons'
    })
      .state('dashboard.notifications',{
        templateUrl:'views/ui-elements/notifications.html',
        url:'/notifications'
    })
      .state('dashboard.typography',{
       templateUrl:'views/ui-elements/typography.html',
       url:'/typography'
   })
      .state('dashboard.icons',{
       templateUrl:'views/ui-elements/icons.html',
       url:'/icons'
   })
      .state('dashboard.grid',{
       templateUrl:'views/ui-elements/grid.html',
       url:'/grid'
   });
   $httpProvider.interceptors.push('TokenInterceptor');
  }]);

app.filter('toArray', function() { return function(obj) {
    if (!(obj instanceof Object)) return obj;
    var result = [];
	for(var i =0; i<Object.keys(obj).length ;i++){
		for(var item in obj){
			if(obj[item] == i){
				result.push(item);
			}
		}
	}
	return result;
}});