angular.module('boxuppApp').controller('ctrlBarController',function($scope,shellScript,$routeParams,miscUtil){

	$scope.saveNewScript = function(newShellScriptData){
		/*$scope.shellObj = new shellScript();
		$scope.shellObj.$save(newShellScriptData).then(function(data){
			console.log('Shell Script saved');
		});*/
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
});