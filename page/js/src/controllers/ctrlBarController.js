angular.module('boxuppApp').controller('ctrlBarController',function($scope,shellScript){

	$scope.saveNewScript = function(activeScript){
		shellScript.save(activeScript).then(function(data){
			console.log('Shell Script saved');
		});
	}
});