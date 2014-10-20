angular.module("boxuppApp").controller('gitController',function($scope,fetchVagrantFile){
	$scope.githubConfig = {
		username:"",
		password:"",
		repoName:"trial",
		repoBranch:"master",
		path:"VagrantFile",
		comment:"Trial commit"
	};
	
	$scope.commitToGithub = function(){
	
		var gC = $scope.githubConfig;
		var gitUsername = gC.username;
		var gitPassword = gC.password;
		var gitRepo = gC.repoName;
		var github = new Github({
			username: gitUsername,
			password: gitPassword	
		});
		var gitPath = gC.path;
		var repo = github.getRepo(gitUsername, gitRepo);
		var gitBranch = gC.repoBranch;
		var gitCommitMessage = gC.comment;
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
}).factory('fetchVagrantFile',function($http,$q){
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
});


