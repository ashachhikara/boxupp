var ngBoxuppApp = angular.module('boxuppApp');

ngBoxuppApp.controller('projectInitController',function($scope,$routeParams,Providers,$location,$http){


    $scope.$watch('selectedProvider',function(newVal, oldVal){
        if(newVal === oldVal) return;
        console.info('watch triggered');
        if(newVal.name === 'Docker') {
            //$location.path("#/projects/{{$routeParams.projectID}}/docker");
            var userID = $routeParams.userID;
            var projectID = $routeParams.projectID;
            var providerID = $routeParams.providerType;
            
            $location.path("/projects/" + userID + "/" + projectID + "/" + providerID +"/docker/");
        }else{

            var userID = $routeParams.userID;
            var projectID = $routeParams.projectID;
            var providerID = $routeParams.providerType;
            
            $location.path("/projects/" + userID + "/" + projectID + "/" + providerID +"/virtualbox/");
        }
    },true);
    /*
    $http({method: 'GET', url: '/services/resources/providers/1'}).
      success(function(data, status, headers, config) {
            
            $scope.selectedProvider = data;
      }).
      error(function(data, status, headers, config) {
            console.error("Error");
      });*/

	$scope.selectedProvider = Providers.get({id : $routeParams.providerType});
    
    // $scope.providerID = $routeParams.providerType;
    // $scope.selectedProvider = Providers.get({ id: $scope.providerID }, function() {
    //     console.log($scope.selectedProvider);
    // });

    
});