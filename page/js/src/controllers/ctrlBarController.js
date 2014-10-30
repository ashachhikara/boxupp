angular.module('boxuppApp').controller('ctrlBarController',function($scope,shellScript,$routeParams,miscUtil){

	$scope.saveNewScript = function(newShellScriptData){
		/*$scope.shellObj = new shellScript();
		$scope.shellObj.$save(newShellScriptData).then(function(data){
			console.log('Shell Script saved');
		});*/
		newShellScriptData.isDisabled = false;
		newShellScriptData.creatorUserId = $routeParams.userID;
		newShellScriptData.description = "v1 script";
		newShellScriptData.creationTime = miscUtil.fetchCurrentTime();
		newShellScriptData.ProjectId = $routeParams.projectID;
		shellScript.save(newShellScriptData,function(data){
			if(data.statusCode === 0){
				console.info('Shell Script has been saved successfully');
				console.log(data);
			}
			
		});
	}
});