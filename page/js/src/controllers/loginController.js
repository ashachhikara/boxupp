var ngBoxuppApp = angular.module('boxuppApp');

ngBoxuppApp.controller('loginController',function($scope,$http,$location,User){

	/*$scope.loginCredentials = {
			loginId : '',
			password : ''
	};*/

	$scope.checkUserLogin = function(){

		$scope.checkLogin = true;
		$scope.authError = false;
		
		User.login($scope.loginCredentials.loginID,$scope.loginCredentials.password).then(function(data){

			if(data.statusCode === 0){
				$scope.checkLogin = false;
				$location.path("/" + data.userID + "/projects/");
			}else{
				$scope.authError = true;
				$scope.checkLogin = false;
			}
		});
	}

	$scope.registerNewUser = function(){
		$scope.startRegistration = true;
		User.signup($scope.newUserDetail).then(function(data){
			if(data.statusCode !== 0){
				$scope.showRegError = true;
			}else{
				$scope.startRegistration = false;
				$scope.showRegInfo = true;	
			}
			
		});
	}


});