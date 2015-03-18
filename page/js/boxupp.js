/*******************************************************************************
 *  Copyright 2014 Paxcel Technologies
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
<<<<<<< HEAD
angular.module('boxuppApp',['ui.codemirror','app','ngAnimate', 'ngLoadScript','ngRoute','ngResource','ui.ace','ngMessages', 'ui.bootstrap']).

	controller('boxuppAppController',function($scope,$http,$rootScope,$timeout,vagrantStatus,executeCommand,$q,$location,User){
		
	$scope.vagrantOutput = [{"type":"normal","output":"C:\\Users\\Paxcel Techn…second","dataEnd":false,"vagrantFileExists":true}];
	}).config(['$routeProvider','$httpProvider',
	           function($routeProvider,$httpProvider) {
=======
angular.module(
		'boxuppApp',
		[ 'ui.codemirror', 'app', 'ngAnimate', 'ngLoadScript', 'ngRoute',
				'ngResource', 'ui.ace', 'ngMessages' ]).controller(
		'boxuppAppController',
		function($scope, $http, $rootScope, $timeout, vagrantStatus,
				executeCommand) {
>>>>>>> 6f531bc934e3b2d6681b64c4509d519436275e91

			$scope.vagrantOutput = [ {
				"type" : "normal",
				"output" : "C:\\Users\\Paxcel Techn…second",
				"dataEnd" : false,
				"vagrantFileExists" : true
			} ];

		}).config([ '$routeProvider', function($routeProvider) {

	$routeProvider.when('/login/', {
		templateUrl : 'templates/login.html',
		controller : 'loginController'
	}).when('/:userID/projects/', {
		templateUrl : 'templates/projects.html',
		controller : 'projectController'
	}).when('/projects/:userID/:projectID/:providerType/', {
		templateUrl : 'templates/projectInit.html',
		controller : 'projectInitController'
	}).when('/projects/:userID/:projectID/:providerType/docker/', {
		templateUrl : 'templates/dockerDashboard.html',
		controller : 'vboxController',
		resolve : {
			provider : function() {
				return 'docker';
			}
		}
	}).when('/projects/:userID/:projectID/:providerType/virtualbox/', {
		templateUrl : 'templates/vboxDashboard.html',
		controller : 'vboxController',
		resolve : {
			provider : function() {
				return 'virtualbox';
			}
		}
	}).when('/projects/:userID/:projectID/:providerType/AWS/', {
		templateUrl : 'templates/awsWorkspace.html',
		controller : 'vboxController',
		resolve : {
			provider : function() {
				return 'AWS';
			}
<<<<<<< HEAD
		}).otherwise({
			redirectTo : '/login/'
		});

		$httpProvider.interceptors.push(function ($q,$location) {
			return {
				'responseError': function (rejection) {
					if (rejection.status === 401) {
						$location.path('/login/');
					}
					return $q.reject(rejection);
				}
			};
		});

	}
	])
=======
		}
	}).otherwise({
		redirectTo : '/login/'
	});
} ]);
>>>>>>> 6f531bc934e3b2d6681b64c4509d519436275e91
