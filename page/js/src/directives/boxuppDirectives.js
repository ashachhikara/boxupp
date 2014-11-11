angular.module('boxuppApp').directive('customScroll',[function(){
	
	function link(scope, element, attributes){		
		element.perfectScrollbar({
			  wheelSpeed: 20,
			  wheelPropagation: true,
			  minScrollbarLength: 20
		});
	}

	return {
		link : link
	}

}]);