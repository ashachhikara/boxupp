$('body').on('keyup','#vagrantFileModal input',function(){
		var newLength = ($(this).val().length);
		$(this).prop('size', newLength);
});

$("#vagrantFileModal").on('show.bs.modal',function(){
	$("#vagrantFileModal input").each(function(){
		if($(this).val().length > 0){
			$(this).trigger('keyup');
		}
	});
});

function loaderIntro(){
	$('#lightBox').show();
	$('#loaderWindow').show();
	setTimeout(function(){
		$('#loaderWindow').hide();
		//$('#lightBox').animate({opacity:0},1500);
		$("#lightBox").animate({opacity:0}, {duration:1000, complete: function(){
			$('#lightBox').hide();
		}});
	},6000);
}

$('body').on('click','#invokeShellConsole,button.shellConsoleClose',function(){
	if($("#staticSection span").hasClass('closed')){
		$("#staticSection").click();
	}
});