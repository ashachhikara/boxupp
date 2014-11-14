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
	$scope.setShellChangeFlag = function(){
		$scope.boxuppConfig.shellChangeFlag = 1;	
	}
	$scope.$watch('shellScripts',function(newValue,oldValue){
		if(newValue.length !== oldValue.length){
			//shell scripts deleted or added//
			$scope.setShellChangeFlag();
		}
		//Only handle script name change events//
		if((newValue !== oldValue) && ($scope.scriptSelected !== -1)){
		$scope.setShellChangeFlag();
		console.log($scope.boxuppConfig.shellChangeFlag);
		/*To handle empty scripts case*/
		if(((typeof newValue[$scope.scriptSelected]) !== 'undefined') && 
			((typeof oldValue[$scope.scriptSelected]) !== 'undefined')){
			if(newValue[$scope.scriptSelected].scriptName !== oldValue[$scope.scriptSelected].scriptName){
				var newScriptName = newValue[$scope.scriptSelected].scriptName;
				var oldScriptName = oldValue[$scope.scriptSelected].scriptName;
				for(index in $scope.boxesData){
					var oldScriptNameIndex = $scope.boxesData[index].linkedScripts.indexOf(oldScriptName);
					if( oldScriptNameIndex > -1){
						$scope.boxesData[index].linkedScripts.splice(oldScriptNameIndex,1);
						$scope.boxesData[index].linkedScripts.push(newScriptName);
					}
				}
			}
		}
	}},true);
	$scope.createBoxes = function(boxData){
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