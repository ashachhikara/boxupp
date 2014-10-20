var activeScript = 0;
var firstRun = true;
//var flatData;
var scriptMappings = {}

$('body').on('click','#staticSection, button[id*="editScript"]',function(i){
	var divPosition = $('.consoleContent').css('right');
	if(divPosition === '-545px'){
		$(".consoleContent").animate({ right:'61px' }, {duration:1500,specialEasing:{right:"easeInOutQuint"}, complete: function(){
			$("#staticSection span").removeClass('closed');
			$("#staticSection span").addClass('open');
		}});		
	}
	else{
		$(".consoleContent").animate({ right:'-545px' }, {duration:1500,specialEasing:{right:"easeInOutQuint"}, complete: function(){
			$("#staticSection span").removeClass('open');
			$("#staticSection span").addClass('closed');
		}});
	}
});

function animateArrow(){
	var effectCounter = 4;
	for(var counter=1; counter<= effectCounter; counter++){
		$(".graph-arrow").animate({'opacity':1},1000);
		$(".graph-arrow").animate({'opacity':0},400);
	}
	firstRun = false;
}

$('body').on('click','.provisioningControl',function(event){
	$('.provisioningControl').each(function(){
		$(this).removeClass('activeProvisionControl');
	});
	var sectionButtonClicked = $(this).attr('data-provSection');
	$(this).addClass('activeProvisionControl');
	selectProvisioningControlSec(sectionButtonClicked);

});

$("body").on('click','.boxuppNav',function(event){
	$('.boxuppNav').each(function(){
		$(this).removeClass('activeNav');	
	});
	var navButtonClicked = $(this).attr('data-navIndex');
	$(this).addClass('activeNav');
	selectBoxuppNavTab(navButtonClicked);
});

function selectBoxuppNavTab(navigateToTab){
	$('.container,#sidebar-content').children('.TabbedPanelsContent').each(function(i){
		$(this).css("display","none");
		$(this).removeClass('activeBoxuppNav');
	});

	$('#sidebar-content').children('.TabbedPanelsContent:eq('+ navigateToTab +')').each(function(i){
		$(this).css("display","block");
	});
	
	$('.container').children('.TabbedPanelsContent:eq('+ navigateToTab +')').each(function(i){
		$(this).css("display","block");
	});
	//refreshNodesWindow();
}

function selectProvisioningControlSec(sectionButtonClicked){
	$('.provisioningConsole').children('.provisioningConsoleSection').each(function(i){
		$(this).css("display","none");
	});
	$('.provisioningConsole').children('.provisioningConsoleSection:eq('+ sectionButtonClicked +')').each(function(i){
		$(this).css("display","block");
	});
	//refreshNodesWindow();
}
