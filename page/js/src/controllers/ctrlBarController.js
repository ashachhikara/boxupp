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

	$scope.projectData.defaultSettings = {
		"cpuExecCap":30,
		"memory":512,
	};
	$scope.createBoxes = function(boxData){

		$scope.quickBoxCommitLoader = true;
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

	$scope.createQuickBox = function(boxData){
		
		$scope.toBeCreatedBox = angular.copy(boxData);
		$scope.projectData.defaultSettings.networkIP = $scope.generateNewIP();
		angular.extend($scope.toBeCreatedBox,$scope.projectData.defaultSettings);

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

	$scope.generateNewIP = function(){
		var presentIP ="";
		if(angular.isDefined($scope.boxesData)){
			var size = $scope.boxesData.length;
			presentIP = $scope.boxesData[size-1].networkIP;
		}else{
			presentIP = "192.168.1.1";
		}
		
		var ipValues = presentIP.split(".");
		var newIP;
		if(parseInt(ipValues[3]) === 255){
			newIP = ipValues[0]+"."+ipValues[1]+"."+(parseInt(ipValues[2]) + 1)+"."+0;
		}else{
			newIP = ipValues[0]+"."+ipValues[1]+"."+ipValues[2]+"."+(parseInt(ipValues[3]) + 1);
		}
		return newIP;
	}

	$scope.checkFormStates = {

		vmQuickBox : function(){

				return !(!$scope.quickBoxForm.vagrantID.$pristine && $scope.quickBoxForm.vagrantID.$valid &&
				    !$scope.quickBoxForm.hostName.$pristine && $scope.quickBoxForm.hostName.$valid &&
				    !$scope.quickBoxForm.boxType.$pristine && $scope.quickBoxForm.boxType.$valid &&
				    !$scope.quickBoxForm.boxUrl.$pristine && $scope.quickBoxForm.boxUrl.$valid);
		},
		vmRawBox : function(rawBoxFormBasicSettings,rawBoxFormNetworkSettings){

				return !(!rawBoxFormBasicSettings.vagrantID.$pristine && rawBoxFormBasicSettings.vagrantID.$valid &&
				    !rawBoxFormBasicSettings.hostName.$pristine && rawBoxFormBasicSettings.hostName.$valid &&
				    !rawBoxFormBasicSettings.boxType.$pristine && rawBoxFormBasicSettings.boxType.$valid &&
				    !rawBoxFormBasicSettings.boxUrl.$pristine && rawBoxFormBasicSettings.boxUrl.$valid &&
					!rawBoxFormNetworkSettings.networkIP.$pristine && rawBoxFormNetworkSettings.networkIP.$valid);
		},
		containerQuickBox : function(){
				return !(!$scope.containerQuickBoxForm.vagrantID.$pristine && $scope.containerQuickBoxForm.vagrantID.$valid &&
				    !$scope.containerQuickBoxForm.hostName.$pristine && $scope.containerQuickBoxForm.hostName.$valid &&
				    $scope.containerQuickBoxForm.imageName.$valid);								
				// !$scope.containerQuickBoxForm.imageName.$pristine &&
		}
	}				
});