var ngBoxuppApp = angular.module('boxuppApp');

ngBoxuppApp.controller('loginController',function($scope,$http,$location,User){

	/*$scope.loginCredentials = {
			loginId : '',
			password : ''
	};*/

	$scope.bodyStyle.applyDashBoardStyling = false;


	$scope.checkIfUserExists = function(mailID){
		if(angular.isDefined(mailID)){
			User.checkIfExists(mailID).then(function(response){
				if(response.statusCode === 0){
					$scope.disableUserReg = true;	
				}else{
					$scope.disableUserReg = false;	
				}
			});	
		}
	}

	$scope.checkEmailID = function(registrationForm){
		if(!registrationForm.emailID.$valid && registrationForm.emailID.$dirty){
			return true;
		}
	}

	$scope.checkUserLogin = function(){

		$scope.checkLogin = true;
		$scope.authError = false;
		try{
			var lc = $scope.loginCredentials;

			if( angular.isUndefined(lc && lc.loginID && lc.password)){
				throw "Empty Username/Password !";	
			} 
			User.login($scope.loginCredentials.loginID,$scope.loginCredentials.password).then(function(data){
				try{
					if(data.statusCode === 0){
						$scope.checkLogin = false;
						$location.path("/" + data.userID + "/projects/");
					}else{
						$scope.authError = true;
						$scope.checkLogin = false;
						throw "Invalid user credentials";
					}
				}catch(err){
					$scope.errorMessage = err;
					$scope.authError = true;
					$scope.checkLogin = false;	
				}
			});
		}
		catch(err){
			$scope.errorMessage = err;
			$scope.authError = true;
			$scope.checkLogin = false;	
		}
	}

	$scope.registerNewUser = function(){
		$scope.startRegistration = true;
		try{
			var nu = $scope.newUserDetail;
			if( angular.isUndefined(nu && nu.mailID && nu.password && nu.firstName && nu.lastName)){
				throw "All fields should be filled";
			}
			User.signup($scope.newUserDetail).then(function(data){
				if(data.statusCode !== 0){
					$scope.showRegError = true;
					$scope.startRegistration = false;
				}else{
					$scope.startRegistration = false;
					$scope.showRegInfo = true;	
					$scope.regError = false;
				}
			});	
			ga('send', 'event', 'v0.0.4 Download', 'click');
		}catch(err){
			$scope.startRegistration = false;
			$scope.regErrorMessage = err;
			$scope.regError = true;
		}	
		
	}

	$scope.proceedToLogin = function(){
		$location.path('#/login/');
	}


});