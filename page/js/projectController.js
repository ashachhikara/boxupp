angular.module("boxuppApp").controller('projectController',function($scope,Projects,Providers,User,$routeParams,$filter,$location){

	/*$scope.projects = Projects.query(function(){
		if($scope.projects.length === 0) $scope.noProjectsInfo = true;
		
	});*/

	User.getProjects($routeParams.userID).then(function(projectsList){
			$scope.projects = projectsList;
	});

	$scope.providers = Providers.query(function(){

		//Queries all the providers stored in the database
		
	});

	$scope.selectProject = function(project){
		//alert('Project ID :-' + project.id + ' Project ProviderType :- '+project.providerType);
		//alert($routeParams.userID);
		$location.path("/projects/" + $routeParams.userID + "/" + project.id + "/" + project.providerType);
	}


	/*$scope.providers = [
						{id:1, name:'VirtualBox'},
						{id:2, name:'Docker'}
	];*/

	$scope.checkProjectInput = function(){
		     return !(!$scope.newProjectData.providerNames.$pristine && $scope.newProjectData.providerNames.$valid
		     		  && !$scope.newProjectData.projectTitle.$pristine && $scope.newProjectData.projectTitle.$valid
		     		  && !$scope.newProjectData.projectDesc.$pristine && $scope.newProjectData.projectDesc.$valid);
	}

	$scope.submitNewMachineData = function(){
			
			$scope.newProject.owner=$routeParams.userID;
			
			$scope.newProject.creationTime = $filter('date')(new Date().getTime(), "yyyy'-'MM'-'dd HH':'mm':'ss");
								 
			Projects.save($scope.newProject,function(data){
				$scope.projects.push(angular.copy(data.beanData));
				//Reset New Project Modal Data
				$scope.newProject = {};
				//Reset form pristine state
				$scope.newProjectData.$setPristine();
			});

			//$scope.newProject = {};
			
			
	}

});