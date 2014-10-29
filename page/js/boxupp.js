angular.module('boxuppApp',['ui.codemirror','app','ngAnimate', 'ngLoadScript','ngRoute','ngResource','ui.ace']).
	controller('boxuppAppController',function($scope,$http,$rootScope,$timeout,vagrantStatus,executeCommand){
	
	
	}).config(['$routeProvider',
  		function($routeProvider) {
  		
		    $routeProvider.when('/login/',{
		    	templateUrl: 'templates/login.html',
		    	controller: 'loginController'
		      }).when('/:userID/projects/', {
		        templateUrl: 'templates/projects.html',
		        controller: 'projectController'
		      }).when('/projects/:userID/:projectID/:providerType/',{
		      	templateUrl: 'templates/projectInit.html',
		      	controller: 'projectInitController'
		      }).when('/projects/:userID/:projectID/:providerType/docker/',{
		      	templateUrl: 'templates/dockerConfiguration.html',
		      	controller: 'dockerController'
		      }).when('/projects/:userID/:projectID/:providerType/virtualbox/',{
		      	templateUrl: 'templates/vboxDashboard.html',
		      	controller: 'vboxController'
		      }).otherwise({
		      	redirectTo : '/login/'
		      });
 	 	}
 	 ]);

	