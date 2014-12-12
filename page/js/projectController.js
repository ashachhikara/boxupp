angular.module("boxuppApp").controller('projectController',function($scope,$rootScope, Projects,Providers,User,$routeParams,$filter,$location,miscUtil,$http,$timeout){

	/*$scope.projects = Projects.query(function(){
		if($scope.projects.length === 0) $scope.noProjectsInfo = true;
		
	});*/
	 
	$scope.bodyStyle.applyDashBoardStyling = false;
	
	User.getProjects($routeParams.userID).then(function(projectsList){
			$scope.projects = projectsList;
	});

	$scope.providers = Providers.query(function(){
		console.log($scope.providers);
		//Queries all the providers stored in the database
		
	});

	$scope.selectProject = function(project){

		//alert('Project ID :-' + project.id + ' Project ProviderType :- '+project.providerType);
		//alert($routeParams.userID);
		
		$location.path("/projects/" + $routeParams.userID + "/" + project.projectID + "/" + project.providerType);
	}


	/*$scope.providers = [
						{id:1, name:'VirtualBox'},
						{id:2, name:'Docker'}
	];*/

	$scope.checkProjectInput = function(){
		     return !(!$scope.newProjectData.providerNames.$pristine && $scope.newProjectData.providerNames.$valid
		     		  && !$scope.newProjectData.projectTitle.$pristine && $scope.newProjectData.projectTitle.$valid
		     		  && !$scope.newProjectData.projectDesc.$pristine && $scope.newProjectData.projectDesc.$valid
		     		  && ($scope.newProject.providerType > 0));
	}

	$scope.submitNewProjectData = function(){
			
			$scope.newProject.owner=$routeParams.userID;
			$scope.newProject.creationTime = miscUtil.fetchCurrentTime();								 
			$scope.newProject.isDisabled = false;
			Projects.save($scope.newProject,function(data){
				$scope.projects.push(angular.copy(data.beanData));
				//Reset New Project Modal Data
				$scope.newProject = {};
				//Reset form pristine state
				$scope.newProjectData.$setPristine();
				$scope.newProjectCreated = true;
				$timeout(function(){
					$scope.newProjectCreated = false;
				},3000);
			});

			//$scope.newProject = {};
			
			
	}

});