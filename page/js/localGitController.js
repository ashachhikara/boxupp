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
angular.module("boxuppApp").controller('localGitController', [ '$scope', '$routeParams','remoteRepoFunctionality',function($scope, $routeParams, remoteRepoFunctionality){
	$scope.localGitConfig = {
		GitURI:"",
		password:"",
		repoBranch:"",
		path:"local/VagrantFile",
		comment:"Vagrantfile commit"
	};

	$scope.buttonText = "Get Branches";
	$scope.loaderValid = false;
	$scope.statusText = "";
	//$scope.fetchRepoListSuccess = false;
	$scope.branchValid = false;
	
	$scope.resetFlags = function(){
		//$scope.fetchRepoListSuccess = false;
		$scope.fetchBranchListSuccess = false;
		$scope.branchValid = false;
		// $scope.githubConfig.username = "";
		// $scope.githubConfig.password = "";
	}	

	
	$scope.getRemoteBranchList = function(){
		console.log("*************"+$scope.localGitConfig);
		remoteRepoFunctionality.branchList($scope.localGitConfig.gitURI, $scope.localGitConfig.password).then(function(error, response){
			if(resonse.statusCode == 0){
				$scope.localGitConfig.gitBranchList = response.beanData;
			}
		});

		

	}
		
	$scope.commitToGit = function(){
		/*var gC = $scope.githubConfig;
		var github = new Github({
			username: gC.username,
			password:gC.password,
			auth : 'basic'
		});
	
		var gitPath = gC.path;
		var repo = github.getRepo(gC.username, gC.repoName);
		var gitBranch = gC.repoBranch;
		var gitCommitMessage = gC.comment;*/
		/*GitRepo.save($scope.githubConfig,function(data){
				$scope.projects.push(angular.copy(data.beanData));
				//Reset New Project Modal Data
				$scope.newProject = {};
				//Reset form pristine state
				$scope.newProjectData.$setPristine();
			});*/
		
			remoteRepoFunctionality.commitOnRemoteRepo($scope.localGitConfig).then(function(error, response){
				if(err !== null){
					var responseObj = JSON.parse(err.request.response);
					alert(responseObj.message);
				}else{
					alert('Vagrant File has been successfully committed to your repository');
				}
			});
		}
}]).factory('remoteRepoFunctionality', function($http, $q){
	return{
		branchList: function(gitURI, password){
			var repoURL = '/boxupp/resources/localGitRepo/getBranches';
			var parameters = {"gitURI" : gitURI, "password" : password};
			var deferred = $q.defer();
			$http({
				method: 'GET',
				headers: {'Content-Type':'application/json; charset=UTF-8'},
				url : repoURL
			}).
			success(function (response,status, headers, config) {
				deferred.resolve(response);
				console.log(response);
			}).error(function(data, status, headers, config){
				console.log(":Error geting Repo from github for a user");
			});
			return deferred.promise;
		},
		commitOnRemoteRepo: function(localGitConfig){
			var repoURL = '/boxupp/resources/localGitRepo/commmit';
			
			var deferred = $q.defer();
			$http({
				method: 'POST',
				headers: {'Content-Type':'application/json; charset=UTF-8'},
				data : localGitConfig
			}).
			success(function (response,status, headers, config) {
				deferred.resolve(response);
				console.log(response);
			}).error(function(data, status, headers, config){
				console.log(":Error geting Repo from github for a user");
			});
			return deferred.promise;
		}

	}
});
