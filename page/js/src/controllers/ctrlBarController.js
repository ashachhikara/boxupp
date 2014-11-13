angular.module('boxuppApp').controller('ctrlBarController',function($scope,shellScript,$routeParams,miscUtil,MachineConfig){

	

	$scope.saveNewScript = function(newShellScriptData){
		newShellScriptData.isDisabled = false;
		newShellScriptData.userID = $routeParams.userID;
		newShellScriptData.description = "v1 script";
		newShellScriptData.creationTime = miscUtil.fetchCurrentTime();
		newShellScriptData.ProjectID = $routeParams.projectID;
		shellScript.save(newShellScriptData,function(data){
			if(data.statusCode === 0){
				console.info('Shell Script has been saved successfully');
				console.log(data);
				$scope.shellScripts.push(data.beanData);
			}
			
		});
	}

	$scope.createBoxes = function(boxData){
		console.log("**********"+boxData.dockerLinks);
		for(link in  boxData.dockerLinks){
			console.log("%"+link[0]);
		}
		$scope.quickBoxCommitLoader = true;
		/*if(boxData.dockerImage){
			boxData.dockerImage = boxData.dockerImage.name;
		}*/
		$scope.toBeCreatedBox = angular.copy(boxData);
		$scope.toBeCreatedBox.projectID = $routeParams.projectID;
		$scope.toBeCreatedBox.isDisabled = false;
		MachineConfig.save($scope.toBeCreatedBox,function(data){
			$scope.boxesData.push(data.beanData);
			$scope.quickBox = {};
			$scope.quickBoxForm.$setPristine();
		});
		$scope.quickBoxCommitLoader = false;
	}

	$scope.cloneBoxData = function(cloneBox){
		$scope.toBeClonedBox = angular.copy(cloneBox);
		$scope.toBeClonedBox.networkIP = null;
		$scope.toBeClonedBox.vagrantID = null;
		$scope.activeVM = $scope.toBeClonedBox;
		$('#boxModal').modal('show');
	}

	$scope.checkFormStates = {

		vmQuickBox : function(){

				return !(!$scope.quickBoxForm.vagrantID.$pristine && $scope.quickBoxForm.vagrantID.$valid &&
				    !$scope.quickBoxForm.hostName.$pristine && $scope.quickBoxForm.hostName.$valid &&
				    !$scope.quickBoxForm.boxType.$pristine && $scope.quickBoxForm.boxType.$valid &&
				    !$scope.quickBoxForm.boxUrl.$pristine && $scope.quickBoxForm.boxUrl.$valid);
		},
		containerQuickBox : function(){
				return !(!$scope.containerQuickBoxForm.vagrantID.$pristine && $scope.containerQuickBoxForm.vagrantID.$valid &&
				    !$scope.containerQuickBoxForm.hostName.$pristine && $scope.containerQuickBoxForm.hostName.$valid &&
				    $scope.containerQuickBoxForm.imageName.$valid);								
				// !$scope.containerQuickBoxForm.imageName.$pristine &&
		}
	}				
});