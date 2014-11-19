
angular.module('boxuppApp').controller('vboxController',function($scope,$http,$rootScope,$routeParams,$timeout,MachineConfig,ResourcesData,vagrantStatus,executeCommand,retrieveMappings,puppetModule,miscUtil,shellScript){

	$scope.projectData = {
		boxesState : {
			update : false,
		},
		scriptsState : {
			update : false
		}
	};

	$scope.boxuppMappings = {};
	$scope.serverAddress = "http://"+window.location.host;
	$scope.serverWSAddress = "ws://"+window.location.host;
	$scope.deployCommand = "";
	$scope.vagrantOptions = 0;
	$scope.apiHitInterval = 500; //0.5 second
	$scope.activeVM = null;
	$scope.activeScript = null;
	$scope.projectData.activeModule = null;
	$scope.outputConsole = {};
	$scope.outputConsole.boxuppExecuting = false;
	$scope.searchingModules = false;
	$scope.outputConsole.boxuppOutputWindow = false;
	$scope.providerValidation = false;
	$scope.bodyStyle.applyDashBoardStyling = true;
	$scope.quickBox = {};
	$scope.moduleResults=[];
	$scope.rawBox = {};
	$scope.rawScript = {};
	$scope.rawBoxForm = {};
	$scope.rawBoxFormNetworkSettings = {};
	$scope.moduleProvMappings = {};
	$scope.shellProvMappings = {};
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
			}else{
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
	$scope.userSignout = function(){
		$location.path('/login/');
	}

	$scope.deleteActiveBox = function(){

		MachineConfig.delete({id:$scope.activeVM.machineID},function(){			
			var boxCounter = 0;
			angular.forEach($scope.boxesData,function(box){
				if(box.machineID === $scope.activeVM.machineID){
					$scope.boxesData.splice(boxCounter,1);
				}
				boxCounter++;
			});
		});

		/*$scope.machine = new MachineConfig({id : $scope.activeVM.machineID },function(){
			$scope.machine.$delete(function(){
				alert('Machine deleted');
			});
		});*/
	}

	$scope.deleteActiveScript = function(){
		alert('Script deleted');
	}

	$scope.editActiveBox = function(){
		var toBeEditedBox = angular.copy($scope.activeVM);
		$scope.rawBox = toBeEditedBox;
		$('#boxModal').modal('show');
		$scope.projectData.boxesState.update = true;
	}

	$scope.editActiveScript = function(){
		var toBeEditedScript = angular.copy($scope.activeScript);
		$scope.rawScript = toBeEditedScript;
		$('#scriptModal').modal('show');
		$scope.projectData.scriptsState.update = true;
	}

	$scope.updateBox = function(){
		var updatedContent = $scope.rawBox;
		$scope.entry = MachineConfig.get({id:updatedContent.machineID},function(){
			angular.extend($scope.entry,updatedContent);
			$scope.entry.configChangeFlag =1;
			$scope.entry.$update(function(){
				angular.forEach($scope.boxesData,function(box){
					if(box.machineID === $scope.entry.beanData.machineID){
						angular.extend(box,$scope.entry.beanData);
						box.configChangeFlag = 1;
						return;
					}
				});
			});
		});
	}

	$scope.updateScript = function(){
		var updatedContent = $scope.rawScript;
		$scope.entry = shellScript.get({id:updatedContent.scriptID},function(){
			angular.extend($scope.entry,updatedContent);
			$scope.entry.creationTime = miscUtil.fetchCurrentTime();
			$scope.entry.userID = $routeParams.userID;
			$scope.entry.$update(function(){
				var beanScriptID = $scope.entry.beanData.scriptID;
				angular.forEach($scope.shellScripts,function(script){
					if(script.scriptID === beanScriptID){
						angular.extend(script,$scope.entry.beanData);
						$scope.triggerScriptChangeFlag(script);
						return;
					}
				});
			});
		});
	}

	$scope.triggerScriptChangeFlag = function(script){
		var scriptID = script.scriptID;
		angular.forEach($scope.shellProvMappings,function(mapping,key){
			if($scope.shellProvMappings[key].indexOf(scriptID) != -1){
				angular.forEach($scope.boxesData,function(box){
					if(box.machineID == key){
						$scope.setScriptFlagForBox(box);
					}
				});	
			}
		});
	}

	$scope.setScriptFlagForBox = function(box){
		var updatedContent = angular.copy(box);
		$scope.entry = MachineConfig.get({id:box.machineID},function(){
							angular.extend($scope.entry,updatedContent);
							$scope.entry.scriptChangeFlag = 1;
							$scope.entry.$update(function(){
								box.scriptChangeFlag = 1;		
							});
						});
	}


	// $scope.selectedProvMachine = {};

	$scope.listOfSSHImages=[
		{
			"path":"boxupp / centos-base",
			"iconSrc":"img/centos-32.png"
		},
		{
			"path":"boxupp / redhat-base",
			"iconSrc" : "img/redhat-32.png"
		},
		{
			"path":"boxupp / debian-base",
			"iconSrc" : "img/debian-32.png"
		},
		{
			"path":"boxupp / ubuntu-base",
			"iconSrc" : "img/ubuntu-32.png"
		}
	];
	
	$scope.searchNewModule = function(moduleSearchText){
		$scope.searchingModules = true;
		puppetModule.searchPuppetModule($scope,moduleSearchText).then(function(response){
			$scope.moduleResults = response;	
			$scope.searchingModules = false;
		});
	}

	$scope.downloadNewModule = function(toBeDownloadedModule){
		toBeDownloadedModule.downloadButtonText = 'Downloading';
		toBeDownloadedModule.downloading = true;
		puppetModule.downloadPuppetModule(toBeDownloadedModule).then(function(response){
			toBeDownloadedModule.downloadButtonText = 'Download';
			toBeDownloadedModule.downloading = false;
			$scope.projectData.modules.push(response.beanData);
		});
	}
	$scope.selectScript = function(num){
		// if($scope.nodeSelectionDisabled === true) animateArrow();
		$scope.activeScript = $scope.shellScripts[num];
		// $scope.nodeSelectionDisabled = false;
	}

	$scope.resetCtrlBarSecNav = function(){
		$('ul.ctrl-bar-sec-list li').removeClass('active');
	}
	
	
	
	$scope.vagrantCommands = {
		0:"Choose what's best"
	};
	$scope.selectProviderPage = function(){
		$scope.providerValidation = true;
	}

	$scope.fetchBoxList = function(){
		ResourcesData.fetchBoxList($routeParams.projectID).then(function(response){
			if(response.length > 0){
				// $scope.boxesData.push(angular.copy(response));
				$scope.boxesData = response;
			}			
		});	
	}

	$scope.fetchScriptList = function(){
		ResourcesData.fetchScriptList($routeParams.projectID).then(function(response){
			console.log(response);
			$scope.shellScripts = response;
		});		
	}
	
	$scope.fetchModuleList = function(){
		ResourcesData.fetchModuleList($routeParams.projectID).then(function(response){
			$scope.projectData.modules = response;
		});			
	}

	$scope.markActiveProject = function(){
		miscUtil.selectActiveProject().then(function(response){
			console.log(response);
		});
	}

	$scope.fetchShellScriptMappings = function(){
		retrieveMappings.fetchScriptMappings().then(function(mappings){
			$scope.shellProvMappings = {};
			$scope.shellProvMappings = $scope.convertScriptMappingsStructure(mappings);
		});
	}

	$scope.convertScriptMappingsStructure = function(mappings){
		var newMappings = {};
		angular.forEach(mappings,function(map){
			var machineID = map.machineConfig.machineID;
			var scriptID = map.script.scriptID;
			if(newMappings.hasOwnProperty(machineID)){
				newMappings[machineID].push(scriptID);
			}else{
				newMappings[machineID] = [];
				newMappings[machineID].push(scriptID);
			}
		});
		return newMappings;
	}

	$scope.fetchPuppetMappings = function(){
		retrieveMappings.fetchPuppetMappings().then(function(mappings){
			$scope.moduleProvMappings = {};
			angular.forEach(mappings,function(map){
				var machineID = map.machineConfig.machineID;
				var puppetID = map.puppetModule.puppetID;
				if($scope.moduleProvMappings.hasOwnProperty(machineID)){
					$scope.moduleProvMappings[machineID].push(puppetID);
				}else{
					$scope.moduleProvMappings[machineID] = [];
					$scope.moduleProvMappings[machineID].push(puppetID);
				}
			});
		});
	}

	$scope.fetchBoxList();
	$scope.fetchScriptList();
	$scope.fetchModuleList();
	$scope.markActiveProject();
	$scope.fetchShellScriptMappings();
	$scope.fetchPuppetMappings();
	
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
			"guiMode":false
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
	
	$scope.$watch('activeVM',function(newVal,oldVal){
		console.log(newVal +""+oldVal);
		// console.log("new val "+newVal +" : old val "+oldVal);// console.log("new val "+newVal +" : old val "+oldVal);
	});

	$scope.setVagrantChangeFlag = function(){
		$scope.boxuppConfig.vagrantChangeFlag = 1;
	}
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
	$scope.boxesData = [];
	/*{
							"vagrantID":"trialvm",
							"hostName":"boxupp.test.machine",
							"boxType":"Ubuntu",
							"boxUrl":"http://cloud-images.ubuntu.com/vagrant/precise/current/precise-server-cloudimg-i386-vagrant-disk1.box",
							"networkIP":"192.168.111.23",
							"syncFolders":[{
											"hostFolder":"",
											"vmFolder":""
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
							"memory":"512",
							"bootTimeout":"300",
							"guiMode":false
						}*/
	$scope.projectData.modules = [];
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
	
	/*$scope.$watch('boxesData',function(newVal,oldVal){
		if(newVal !== oldVal){
			$scope.setVagrantChangeFlag();
		}
	},true);*/
	
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
	$scope.deployAllBoxes = function(){
		
	}
	$scope.deployEnvironment = function(vmConfig){
		if($scope.checkDataValidity()){
			if($scope.boxuppStateChanged()){
				executeCommand.saveBoxuppData($scope, $routeParams.projectID, $routeParams.userID).then(function(response){
					$scope.startDeployment(vmConfig);			
				});			
			}else{
				executeCommand.saveBoxuppData($scope, $routeParams.projectID, $routeParams.userID).then(function(response){
					$scope.startDeployment();			
				});
			}		
		}		
	}
	$scope.startDeployment = function(vmConfig){
		$scope.outputConsole.boxuppExecuting = true;
		$scope.outputConsole.boxuppOutputWindow = true;
		var optionSelected = $scope.vagrantOptions;
		if(optionSelected === 0){
			$scope.pushCustomMessage();
			vagrantStatus.updateVagrantStatus($scope.serverAddress,$scope, $routeParams.userID).then(function(response){
				$scope.chooseBestDeployOption(vmConfig);
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
	
	$scope.chooseBestDeployOption = function(vmConfig){
		var flagStatesCombination = vmConfig.configChnageFlag + "" +vmConfig.moduleChangeFlag + "" +
									(vmConfig.scriptChangeFlag || vmConfig.cookbooksChangeFlag) + "" +
									vmConfig.vagrantExecutionFlag + "";	
									
		this.deployCommand = this.fetchVagrantCommand(flagStatesCombination)+" " + vmConfig.vagrantID;
		
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
		console.log("Environment state : " + combination);
		var commands = {
			"0000":"vagrant up",
			"0001":"vagrant reload --provision",
			"0010":"vagrant up --provision",
			"0011":"vagrant provision --provision-with shell",
			"0100":"vagrant up --provision",
			"0101":"vagrant provision",
			"0110":"vagrant up --provision",
			"0111":"vagrant reload --provision",
			"1000":"vagrant up",
			"1001":"vagrant reload",
			"1010":"vagrant up --provision",
			"1011":"vagrant reload --provision",
			"1100":"vagrant reload --provision",
			"1101":"vagrant reload --provision",
			"1110":"vagrant up --provision",
			"1111":"vagrant reload --provision",
			"0003":"vagrant up --provision",
			"0013":"vagrant up --provision",
			"0103":"vagrant up --provision",
			"0113":"vagrant up --provision",
			"1003":"vagrant up --provision",
			"1013":"vagrant up --provision",
			"1103":"vagrant up --provision",
			"1113":"vagrant up --provision"
			
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
		// $scope.resetBoxURLChangeStatus();
	}

	$scope.deleteFolderMapping = function(mappingNumber){
		$scope.rawBox.syncFolders.splice(mappingNumber,1);
	}
	
	$scope.delDefaultFolderMapping = function(mappingNumber){
		$scope.defaultConfigurations.syncFolders.splice(mappingNumber,1);
	}
	
	$scope.deletePortMapping = function(mappingNumber){
		$scope.rawBox.portMappings.splice(mappingNumber,1);
	}
	$scope.pushSyncFolderMapping = function(hostFolder,vmFolder){
		var syncFolderMapping = {hostFolder:"",
								 vmFolder:""};
		syncFolderMapping.hostFolder = hostFolder;
		syncFolderMapping.vmFolder = vmFolder;
		if($scope.rawBox !== null){
			if(!angular.isArray($scope.rawBox.syncFolders)){
				$scope.rawBox.syncFolders = [];
			}
			$scope.rawBox.syncFolders.push(syncFolderMapping);
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
		if(!angular.isArray($scope.rawBox.portMappings)){
			$scope.rawBox.portMappings = [];
		}
		$scope.rawBox.portMappings.push(portMapping);
	}
	$scope.addDockerLink=function(){
		var dockerLink ={linkContainer:""};
		if(!angular.isArray($scope.rawBox.dockerLinks)){
			$scope.rawBox.dockerLinks=[];
		}
		$scope.rawBox.dockerLinks.push(dockerLink);
	}
	$scope.deleteDockerLink = function(mappingNumber){
		$scope.rawBox.dockerLinks.splice(mappingNumber,1);
	}
	$scope.pushCustomMessage = function(){
		$scope.activeOutputSnippet = {};
		$scope.activeOutputSnippet.dataEnd = false;
		$scope.activeOutputSnippet.type = "normal";
		$scope.activeOutputSnippet.output = "Checking Vagrant Status...";
		$scope.activeOutputSnippet.vagrantFileExists = true;
		$scope.vagrantOutput.push($scope.activeOutputSnippet);
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

	

});

	
		