angular.module("boxuppApp").controller('gitController', [ '$scope',  'fetchVagrantFile', 'fetchListOfgitRepo', function($scope, fetchVagrantFile, fetchListOfgitRepo){
	$scope.githubConfig = {
		username:"",
		password:"",
		repoName:"trial",
		repoBranch:"",
		path:"local/VagrantFile",
		comment:"Trial commit"
	};
	
	$scope.getGitRepoList = function(){
		
		if( !$scope.githubConfig.password ){
			$scope.passValid = true ;
			return -1;

		}
		$scope.passValid = false ;
		var gC = $scope.githubConfig;
		
		var github = new Github({
			username: gC.username,
			password: gC.password,
			auth : 'basic'
		});
		
		$scope.loaderValid = true;
		
		github.getUser().repos(function(err, response){
			if(err != null){
				$scope.loaderValid = false;
				$scope.loginValid = true;
				$scope.githubConfig.username = "";
				$scope.githubConfig.password = "";
				return -1;
			}
			
			if(err == null){
				$scope.loginValid = false;
				$scope.loaderValid = false;
				gC.gitRepoList = response;
				for(resp in response){
				console.log("**************"+resp);
			}
			}
		});
		
	}
	$scope.getGitBranchList = function(){
		var gC = $scope.githubConfig;
		var github = new Github({
			username: gC.username,
			password: gC.password,
			auth : 'basic'
		});

		$scope.loaderValid = true;
		
		github.getRepo(gC.username, gC.repoName).listBranches(function(err, response){

			if(err == null){
				gC.gitBranchList = response;
				$scope.loaderValid = false;
				$scope.repoValid = true;

			}
		});
	}
	$scope.branchValidity = function(){
		$scope.branchValid = true;
	}
	
	
	$scope.commitToGithub = function(){
		var gC = $scope.githubConfig;
		var github = new Github({
			username: gC.username,
			password:gC.password,
			auth : 'basic'
		});
	
		var gitPath = gC.path;
		var repo = github.getRepo(gC.username, gC.repoName);
		var gitBranch = gC.repoBranch;
		var gitCommitMessage = gC.comment;
		Projects.save($scope.newProject,function(data){
				$scope.projects.push(angular.copy(data.beanData));
				//Reset New Project Modal Data
				$scope.newProject = {};
				//Reset form pristine state
				$scope.newProjectData.$setPristine();
			});
		fetchVagrantFile.content($scope.serverAddress).then(function(response){
			var gitContent;
			if(response.statusCode === 0){
				gitContent = response.fileContent;
				repo.write(gitBranch, gitPath, gitContent, gitCommitMessage, function(err) {
					if(err !== null){
						var responseObj = JSON.parse(err.request.response);
						alert(responseObj.message);
					}
					else{
						alert('Vagrant File has been successfully committed to your repository');
					}
				});
			}else{
				alert('Vagrant file could not be fetched');
			}			
		});
		
	}
}]).factory('fetchVagrantFile',function($http,$q){
		return{
			content : function(serverLocation){
				var completeURL = serverLocation + "/services/getVagrantFile";
				var deferred = $q.defer();
				$http({	
					method:'GET',
					headers:{'Content-Type':'application/json; charset=UTF-8'},
					url:completeURL
				}).
				success(function(response, status, headers, config) {
					deferred.resolve(response);
				}).
				error(function(data, status, headers, config) {
					console.log(" : Error fetching vagrant file : ");
				});
				return deferred.promise;
			}
	}
}).factory('fetchListOfgitRepo', function($http, $q){
	return{
		repoList: function(username){
			var repoURL = "https://api.github.com/"+username + "/repos";
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
		}

	}
});


