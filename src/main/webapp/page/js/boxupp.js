angular.module('boxuppApp',['ui.codemirror','app','ngAnimate']).controller('boxuppAppController',function($scope,$http,$rootScope,$timeout,vagrantStatus,executeCommand){
	
	$scope.boxuppMappings = {};
	$scope.serverAddress = "http://"+window.location.host;
	$scope.serverWSAddress = "ws://"+window.location.host;
	$scope.deployCommand = "";
	$scope.vagrantOptions = 0;
	$scope.apiHitInterval = 500; //0.5 second
	$scope.activeVM = null;
	$scope.outputConsole = {};
	$scope.outputConsole.boxuppExecuting = false;
	$scope.outputConsole.boxuppOutputWindow = false;
	
	$scope.vagrantCommands = {
		0:"Choose what's best"
	};
	
	$scope.server = {
		connect : function() {
			
			var location = $scope.serverWSAddress + "/vagrantConsole/";
			this._ws = new WebSocket(location);
			this._ws.onopen = this._onopen;
			this._ws.onmessage = this._onmessage;
			this._ws.onclose = this._onclose;
		},

		_onopen : function() {
			//server._send('websockets are open for communications!');
			console.info('WebSocket connection initiated');
		},
		
		checkReadyState : function(){
			return this._ws.readyState;				
		},
		
		_send : function(message) {
			if (this._ws)
				this._ws.send(message);
		},

		send : function(text) {
			if (text != null && text.length > 0)
				this._send(text);
		},

		_onmessage : function(message) {
			var data = JSON.parse(message.data);
			if(data.dataEnd === false){
				$scope.activeOutputSnippet = {};
				$scope.activeOutputSnippet.dataEnd = data.dataEnd;
				$scope.activeOutputSnippet.type = data.type;
				$scope.activeOutputSnippet.output = data.output;
				$scope.activeOutputSnippet.vagrantFileExists = data.vagrantFileExists;
				if(data.type !== 'empty'){
					if((data.output.indexOf('rogress') !== -1)){
						$scope.vagrantOutput.splice($scope.vagrantOutput.length-1,1);
						$scope.vagrantOutput.push($scope.activeOutputSnippet);
					}else{
						$scope.vagrantOutput.push($scope.activeOutputSnippet);
					}
					$("#consoleOutput").scrollTop(1500000);
				}
			}
			else{
				$scope.activeOutputSnippet = {};
				$scope.activeOutputSnippet.dataEnd = data.dataEnd;
				$scope.activeOutputSnippet.type = data.type;
				$scope.activeOutputSnippet.output = data.output;
				$scope.vagrantOutput.push($scope.activeOutputSnippet);
				$scope.outputConsole.boxuppExecuting = false;
				$("#consoleOutput").scrollTop(1500000);
			}
		},

		_onclose : function(m) {
			this._ws = null;
		}
	};
			
	$scope.defaultConfigurations = {
			"vagrantID":"",
			"hostName":"",
			"boxType":"Ubuntu",
			"boxUrl":"http://cloud-images.ubuntu.com/vagrant/precise/current/precise-server-cloudimg-i386-vagrant-disk1.box",
			"networkIP":"192.168.111.24",
			"syncFolders":[{
							"hostFolder":"",
							"vmFolder":""
			}],
			"portMappings":[{
							"hostPort":"",
							"vmPort":""
			}],
			"providerType":"virtualbox",
			"linkedScripts":[],
			"isPuppetMaster":false,
			"provisionerName":"",
			"cpuExecCap":"",
			"memory":"",
			"bootTimeout":"300",
			"guiMode":true
	};	
	
	$scope.$watch('vagrantSelection.$valid',function(newVal,oldVal){
		if($scope.activeVM !== null){
			$scope.activeVM.isValid = $scope.vagrantSelection.$valid;
		}
	});
	$scope.uploadFolder = "modules";
	$scope.fetchUploadFolder = function(){
		return $scope.uploadFolder;
	};
	
	/*$scope.validations = {
		"vagrantID":"",
		"networkIP":"^(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))$",
	}*/	
		
	$scope.settingsExec = function(option){
		if(option === 0){
			$scope.startProductTour();
		}else if(option === 1){
			$scope.toggleTooltipStatus();
		}
	}
	
	$scope.startProductTour = function(){
		var boxuppIntro = introJs();
		boxuppIntro.setOption("showStepNumbers", false);
		var targetElement = $("div.provisionSec");
		boxuppIntro.onbeforechange(function(targetElement) {  
			var nextStep = $(targetElement).data('step');
			if(nextStep === 1){
				$('#vagrant').click();
				$('#Box1').click();
			}
			if(nextStep === 5){
				$('#provision').click();
			}
			
		});
		boxuppIntro.start();	
	}
	
	$scope.toggleTooltipStatus = function(){
		$scope.tooltipStatus = ($scope.tooltipStatus === 'On') ? 'Off' : 'On';
	}
	
	$scope.$watch('tooltipStatus',function(newVal,oldVal){
		if(newVal === 'On'){
			$('input').tooltip();
		}else{
			$('input').tooltip('destroy');
		}
	});
	
	$scope.editorOptions = {
        lineWrapping : true,
		lineNumbers  : true,
		mode:'puppet',        
    };
	
	$scope.scriptEditorOptions = {
		lineWrapping : true,
		lineNumbers  : true,
		mode:'shell',	
	};
	
	
	$scope.getVagrantBlockID = function(num){
		if($scope.boxesData[num].isPuppetMaster){
			return "master";
		}else{
			return $scope.boxesData[num].vagrantID;
		}
	}
	
	$scope.shellScripts = [];
	$scope.boxesData = [{
							"vagrantID":"trialVM",
							"hostName":"boxupp.test.machine",
							"boxType":"Ubuntu",
							"boxUrl":"http://cloud-images.ubuntu.com/vagrant/precise/current/precise-server-cloudimg-i386-vagrant-disk1.box",
							"networkIP":"192.168.111.23",
							"syncFolders":[{
											"hostFolder":"syncFolder",
											"vmFolder":"/syncFolder"
							}],
							"portMappings":[{
											"hostPort":"8080",
											"vmPort":"8787"
							}],
							"providerType":"virtualbox",
							"linkedScripts":[],
							"isPuppetMaster":false,
							"provisionerName":"testMachine",
							"cpuExecCap":"15",
							"memory":"1024",
							"bootTimeout":"300",
							"guiMode":true
						}];
	$scope.puppet = {
		"manifests":[{"moFileName":"nodes.pp",
					  "moFileSource":"# Sample nodes.pp file\n# Add master node \n# node \"puppet.vagrant.master.com\"\n# {\n# }\n# Add agent node \n# node \"puppet.vagrant.mysql.com\"\n# {\n    #Include modules to be added on the node in this format include module_name refer below e.g\n    #include haproxy\n# }",
					  "editName":false
					},
					{"moFileName":"site.pp",
					 "moFileSource":"# Sample site.pp file \n# import 'nodes.pp'\n# filebucket { 'main':\n#  \t\tserver => 'puppet',\n#  \t\tpath   => false,\n# }\n# File { backup => 'main' }\n\n# node default {}\n\n# Add agent definitions below\n# node 'agent' {\n# notify {'agent':}\n\n# create a simple hostname and ip host entry\n# host { 'mysql':\n#    \tip => '192.168.111.20',\n# }",
					 "editName":false
					}],
		"modules":[],
		"files":[]
	};
	
	$scope.minimizeConsole = function(){
		$scope.outputConsole.boxuppOutputWindow = false;
	}
	
	$scope.limitLength = function(){
		if(!$scope.vagrantSelection.vagrantID.$error.pattern && 
			!$scope.vagrantSelection.vagrantID.$error.maxlength && 
			!$scope.vagrantSelection.vagrantID.$error.required){
		var vagrantID = "";
		vagrantID = $scope.activeVM.vagrantID +"";
		if(vagrantID.length>8){
			vagrantID = vagrantID.substring(0,8);
			$scope.activeVM.vagrantID = vagrantID;
		}
		}
	}
	
	$scope.vagrantOutput = [];
	$scope.activeOutputSnippet = {};
		
	$scope.urlInfo = {};
	$scope.urlInfoAvailable = false;
	
	$scope.resetBoxURLChangeStatus = function(){	
		$scope.urlInfo = {};
		$scope.urlInfoAvailable = false;
	}
	
	$scope.successTrials = {
		"dataPersistance":5,
		"vagrantOutputFetch":5
	}
	$scope.commitBoxuppData = function(){
		var boxuppMappings = {"vmData":$scope.boxesData,"shellScripts":$scope.shellScripts,"puppetData":$scope.puppet,"defaultConfigurations":$scope.defaultConfigurations};
		var completeURL = $scope.serverAddress + "/services/persistData";
		$http({	
				method:'POST',
				headers:{'Content-Type':'application/json; charset=UTF-8'},
				url:completeURL,
				data:boxuppMappings
			}).
		  success(function(data, status, headers, config) {
			console.info('Boxupp data persisted');
			$scope.successTrials.dataPersistance = 5;
			$timeout($scope.commitBoxuppData,10000);
		  }).
		  error(function(data, status, headers, config) {
			console.log("Error persisting data on the server ");
			if($scope.successTrials.dataPersistance > 0){
				$scope.successTrials.dataPersistance = $scope.successTrials.dataPersistance - 1;
				console.warn("Data persistance trial : "+$scope.successTrials.dataPersistance);
				$timeout($scope.commitBoxuppData,15000);
			}
		  });
	}
	
	
	$scope.updateURLInformation = function(){
		$scope.urlInfoAvailable = false;
		var completeURL = $scope.serverAddress + "/services/checkURL?boxURL=" + $scope.activeVM.boxUrl;
		var boxURL = $scope.activeVM.boxUrl;
		//To avoid empty URL triggering event//
		if(boxURL === " " || boxURL === undefined || boxURL === ""){
			$scope.resetBoxURLChangeStatus();
			return;
		}
		
		$http({		
				method:'GET',
				headers:{'Content-Type':'application/json; charset=UTF-8'},
				url:completeURL
			}).
		  success(function(data, status, headers, config) {
				$scope.urlInfo.contentLength = data.contentLength;
				$scope.urlInfo.statusCode = data.statusCode;
				$scope.urlInfoAvailable = true;
		  }).
		  error(function(data, status, headers, config) {
			console.log('Problem in fetching information for box URL. Probably, internet is not working');
		  });
	}
	
	$scope.fetchCommand = function(num){
		return $scope.vagrantCommands[num];
	}
	
	$scope.$watch('boxesData',function(newVal,oldVal){
		if(newVal !== oldVal){
			$scope.setVagrantChangeFlag();
		}
	},true);
	
	$scope.$watch('boxesData.length',function(newVal,oldVal){
		$('#nodesContainer').perfectScrollbar('update');
	},true);
	
	$scope.setVagrantChangeFlag = function(){
		$scope.boxuppConfig.vagrantChangeFlag = 1;
	}
	
	$scope.boxuppConfig = {
		activeVM:1,
		totalVM:1,
		activeNode:1,
		activeProvisioner:"shell",
		vagrantChangeFlag:0,
		shellChangeFlag:0,
		puppetChangeFlag:0,
		cookbooksChangeFlag:0,
		vagrantExecutionFlag:0,
		manifestDefaults:{
			"content":"Hello"
		}
	};
	
	$scope.flushVagrantOutputConsole = function(){
		$scope.vagrantOutput = [];
	}
	
	$scope.chooseBestDeployOption = function(){
		var bC = $scope.boxuppConfig;
		var flagStatesCombination = bC.vagrantChangeFlag + "" +
									(bC.shellChangeFlag || bC.puppetChangeFlag || bC.cookbooksChangeFlag) + "" +
									bC.vagrantExecutionFlag + "";	
		this.deployCommand = this.fetchVagrantCommand(flagStatesCombination);
	}
	
	$scope.toggleConsoleWindow = function(){
		if($scope.outputConsole.boxuppOutputWindow){
			if(!$('#puppetEditor').hasClass('maximized')){
				$scope.outputConsole.boxuppOutputWindow = false;
			}
			return;
		}
		$scope.outputConsole.boxuppOutputWindow = true;
	}

	$scope.fetchVagrantCommand = function(combination){
		//console.log("Environment state : " + combination);
		var commands = {
			"000":"vagrant up",
			"001":"vagrant reload --provision",
			"010":"vagrant up --provision",
			"011":"vagrant provision",
			"100":"vagrant reload",
			"101":"vagrant reload",
			"110":"vagrant reload --provision",
			"111":"vagrant reload --provision",
			"003":"vagrant up --provision",
			"013":"vagrant up --provision",
			"103":"vagrant up --provision",
			"113":"vagrant up --provision"
		};
		return commands[combination];	
	}
	
	$scope.fetchVagrantOutput = function(){
		$scope.outputConsole.boxuppExecuting = true;
		var completeURL = $scope.serverAddress + "/services/getStream";
		$http({		
				method:'GET',
				headers:{'Content-Type':'application/json; charset=UTF-8'},
				url:completeURL
			}).
		  success(function(data, status, headers, config) {
					$scope.successTrials.vagrantOutputFetch = 5;
					if(data.dataEnd === false){
						$scope.activeOutputSnippet = {};
						$scope.activeOutputSnippet.dataEnd = data.dataEnd;
						$scope.activeOutputSnippet.type = data.type;
						$scope.activeOutputSnippet.output = data.output;
						$scope.activeOutputSnippet.vagrantFileExists = data.vagrantFileExists;
						if(data.type !== 'empty'){
							if((data.output.indexOf('rogress') !== -1)){
								$scope.vagrantOutput.splice($scope.vagrantOutput.length-1,1);
								$scope.vagrantOutput.push($scope.activeOutputSnippet);
							}else{
								$scope.vagrantOutput.push($scope.activeOutputSnippet);
							}
							$("#consoleOutput").scrollTop(1500000);
						}
						
						setTimeout($scope.fetchVagrantOutput,$scope.apiHitInterval);	
					}
					else{
						$scope.activeOutputSnippet = {};
						$scope.activeOutputSnippet.dataEnd = data.dataEnd;
						$scope.activeOutputSnippet.type = data.type;
						$scope.activeOutputSnippet.output = data.output;
						$scope.vagrantOutput.push($scope.activeOutputSnippet);
						$scope.outputConsole.boxuppExecuting = false;
						$("#consoleOutput").scrollTop(1500000);
					}
		  }).
		  error(function(data, status, headers, config) {
			console.log("Error : fetching output stream" + data);
			$scope.outputConsole.boxuppExecuting = false;
		  });
	}
	
	$scope.resetFlags = function(){
		$scope.boxuppConfig.vagrantChangeFlag = 0;
		$scope.boxuppConfig.puppetChangeFlag = 0;
		$scope.boxuppConfig.shellChangeFlag = 0;
	}
	
	$scope.boxuppStateChanged = function(){
		var configData = $scope.boxuppConfig;
		return (
				configData.vagrantChangeFlag || 
				configData.shellChangeFlag || 
				configData.puppetChangeFlag || 
				configData.cookbooksChangeFlag
			);
	}
	
	$scope.saveBoxuppData = function(){
		
	}
	$scope.pushNewVM = function(id,name,type,url,ip){
		/*var newVMProps = {};
		newVMProps.vagrantID = id;
		newVMProps.hostName = name;
		newVMProps.boxType = type;
		newVMProps.boxUrl = url;
		newVMProps.networkIP = ip;
		newVMProps.syncFolders = [{"hostFolder":"","vmFolder":""}];
		newVMProps.portMappings = [{"hostPort":"","vmPort":""}];
		newVMProps.providerType= "virtualbox";
		newVMProps.linkedScripts = [];
		newVMProps.isPuppetMaster = false;
		newVMProps.isValid = true;*/
		$scope.boxesData.push(newVMProps);
	}
	$scope.addNewVM = function(){
		//$scope.pushNewVM("boxuppTest","boxupp.test.machine","Ubuntu","http://www.google.com","192.168.111.23");
		var newVM = angular.copy($scope.defaultConfigurations);
		$scope.boxesData.push(newVM);
		$scope.presetNextIP();
		
	}
	$scope.cloneVM = function(num){
		var newVM = angular.copy($scope.boxesData[num]);
		newVM.vagrantID = "";
		newVM.hostName = "";
		newVM.networkIP = "";
		newVM.portMappings = [];
		newVM.isPuppetMaster = false;
		newVM.provisionerName = "";
		/*var toBeClonedNode = $scope.boxesData[num];
		$scope.pushNewVM(
								toBeClonedNode.vagrantID,
								toBeClonedNode.hostName,
								toBeClonedNode.boxType,
								toBeClonedNode.boxUrl,
								toBeClonedNode.networkIP
							);*/
		$scope.boxesData.push(newVM);
		$scope.presetNextIP();
	}
	$scope.presetNextIP = function(){
		var presentIP = $scope.defaultConfigurations.networkIP;
		var ipValues = presentIP.split(".");
		var newIP;
		if(parseInt(ipValues[3]) === 255){
			newIP = ipValues[0]+"."+ipValues[1]+"."+(parseInt(ipValues[2]) + 1)+"."+0;
		}else{
			newIP = ipValues[0]+"."+ipValues[1]+"."+ipValues[2]+"."+(parseInt(ipValues[3]) + 1);
		}
		$scope.defaultConfigurations.networkIP = newIP;
	}
	
	$scope.removeVM = function(num){
		/*splice method removes elements and returns the removed elements,
		Delete on the other hand, replaces the element with 'undefined'*/
		/*if($scope.boxesData[num].vagrantID === $scope.activeVM.vagrantID){
			$scope.activeVM = null;
		}*/
		if($scope.boxesData.length === 1){
			alert('All boxes cannot be removed');
			return;
		}
		$scope.boxesData.splice(num,1);
		if($scope.boxesData.length === 1){
			$scope.activeVM = $scope.boxesData[0];
		}
	}
	$scope.selectNode = function(num){
		$scope.activeVM = $scope.boxesData[num];
		$scope.resetBoxURLChangeStatus();
	}

	$scope.deleteFolderMapping = function(mappingNumber){
		$scope.activeVM.syncFolders.splice(mappingNumber,1);
	}
	
	$scope.delDefaultFolderMapping = function(mappingNumber){
		$scope.defaultConfigurations.syncFolders.splice(mappingNumber,1);
	}
	
	$scope.deletePortMapping = function(mappingNumber){
		$scope.activeVM.portMappings.splice(mappingNumber,1);
	}
	$scope.pushSyncFolderMapping = function(hostFolder,vmFolder){
		var syncFolderMapping = {hostFolder:"",
								 vmFolder:""};
		syncFolderMapping.hostFolder = hostFolder;
		syncFolderMapping.vmFolder = vmFolder;
		if($scope.activeVM !== null){
			$scope.activeVM.syncFolders.push(syncFolderMapping);
		}
	}
	$scope.pushDefaultSyncFolderMapping = function(hostFolder,vmFolder){
		var syncFolderMapping = {hostFolder:"",
								 vmFolder:""};
		syncFolderMapping.hostFolder = hostFolder;
		syncFolderMapping.vmFolder = vmFolder;
		if($scope.defaultConfigurations !== null){
			$scope.defaultConfigurations.syncFolders.push(syncFolderMapping);
		}
	}
	$scope.addSyncFolderMapping = function(){
		$scope.pushSyncFolderMapping("","");
	}
	$scope.addDefaultSyncFolderMapping = function(){
		$scope.pushDefaultSyncFolderMapping("","");
	}
	$scope.addPortMapping = function(){
		var portMapping = {hostPort:"",vmPort:""};
		$scope.activeVM.portMappings.push(portMapping);
	}
	
	$scope.pushCustomMessage = function(){
		$scope.activeOutputSnippet = {};
		$scope.activeOutputSnippet.dataEnd = false;
		$scope.activeOutputSnippet.type = "normal";
		$scope.activeOutputSnippet.output = "Checking Vagrant Status...";
		$scope.activeOutputSnippet.vagrantFileExists = true;
		$scope.vagrantOutput.push($scope.activeOutputSnippet);
	}
	
	$scope.startDeployment = function(){
		$scope.outputConsole.boxuppExecuting = true;
		$scope.outputConsole.boxuppOutputWindow = true;
		var optionSelected = $scope.vagrantOptions;
		if(optionSelected === 0){
			$scope.pushCustomMessage();
			vagrantStatus.updateVagrantStatus($scope.serverAddress,$scope).then(function(response){
				$scope.chooseBestDeployOption();
				executeCommand.triggerVagrant($scope.serverAddress,$scope);
			});
		}else{
			var customCmd = prompt("Enter your Vagrant command","vagrant");
			if(customCmd !== null){
				$scope.deployCommand = customCmd;
				executeCommand.triggerVagrant($scope.serverAddress,$scope);
			}else{
				$scope.outputConsole.boxuppExecuting = false;
				$scope.outputConsole.boxuppOutputWindow = false;
				return false;
			}
		}
		
	}
	
	$scope.checkDataValidity = function(){
		for(box in $scope.boxesData){
			if($scope.boxesData[box].isValid === false){
				alert('Please check the box configurations for errors first');
				return false;
			}
		}
		return true;
	}
	
	$scope.deployEnvironment = function(){
		if($scope.checkDataValidity()){
			if($scope.boxuppStateChanged()){
				executeCommand.saveBoxuppData($scope.serverAddress,$scope).then(function(response){
					$scope.startDeployment();			
				});			
			}else{
				executeCommand.saveBoxuppData($scope.serverAddress,$scope).then(function(response){
					$scope.startDeployment();			
				});
			}		
		}		
	}
	
	$scope.waitForWSConnection = function(callback){
		setTimeout(
        function () {
            if ($scope.server.checkReadyState() === 1) {
                if(callback != null){
                    callback();
                }
                return;
            } else {
                console.log("waiting for connection...")
                waitForSocketConnection(callback);
            }
        }, 500);
	}
		
	}).factory('retrieveMappings',function($http,$q,$timeout,$cacheFactory){
		return{
			fetchMappings : function(serverLocation,$scope){
				var completeURL = serverLocation + "/services/retrieveData";
				var $httpDefaultCache = $cacheFactory.get('$http');
				$httpDefaultCache.removeAll();
				var deferred = $q.defer();
				$http({	
					method:'GET',
					url:completeURL
				}).
				success(function(response, status, headers, config) {
					//if(response.fileExists){
						deferred.resolve(response);
					//}
				}).
				error(function(data, status, headers, config) {
					console.log(" : Error retrieving mappings from server : " + status);
					lockBoxupp = false;
				});
				return deferred.promise;
			}
		}
	}).factory('vagrantStatus',function($http,$q,$timeout){
		return{
			updateVagrantStatus : function(serverLocation,$scope){
				var completeURL = serverLocation + "/services/checkVagrantStatus";
				var deferred = $q.defer();
				$http({	
					method:'GET',
					headers:{'Content-Type':'application/json; charset=UTF-8'},
					url:completeURL
				}).
				success(function(response, status, headers, config) {
						$scope.boxuppConfig.vagrantExecutionFlag = response.statusCode;
						deferred.resolve(response);
				}).
				error(function(data, status, headers, config) {
						console.log(" : Error checking vagrant status : ");
				});
				return deferred.promise;
			}
			}
	
	}).factory('executeCommand',function($http,$q,$timeout){
		return{
			
			triggerVagrant : function(serverLocation,$scope){
				if (!window.WebSocket){
					console.log("WebSocket not supported by this browser");
					var completeURL = $scope.serverAddress + "/services/boxupp?command=" + $scope.deployCommand;
					$http({		
						method:'GET',
						headers:{'Content-Type':'application/json; charset=UTF-8'},
						url:completeURL
					}).
					success(function(data, status, headers, config) {
						$scope.resetFlags();
						$scope.fetchVagrantOutput();
					}).
					error(function(data, status, headers, config) {
						console.log("Error executing Vagrant command : " + data);
					});
				}else{
					console.log("Websocket supported");
					$scope.server.connect();
					$scope.waitForWSConnection(function(){
						$scope.server.send($scope.deployCommand);
					});
					
				}
				
			},
			saveBoxuppData : function(serverLocation,$scope){
				var boxuppMappings = {"vmData":$scope.boxesData,"shellScripts":$scope.shellScripts,"puppetData":$scope.puppet};
				var completeURL = $scope.serverAddress + "/services/saveAsFile";
				var deferred = $q.defer();
				$http({	
						method:'POST',
						headers:{'Content-Type':'application/json; charset=UTF-8'},
						url:completeURL,
						data:boxuppMappings
					}).
				success(function(data, status, headers, config) {
					if(data.fileCreated){
						console.log("Vagrant File Saved at : " + data.fileCreationPath);
						$scope.resetFlags();
						$scope.flushVagrantOutputConsole();
						deferred.resolve(data);
					}
				}).
				error(function(data, status, headers, config) {
					console.log("Error saving Boxupp Data: " + data);
				});
				return deferred.promise;
			}
		}
	});