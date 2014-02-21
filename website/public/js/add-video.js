$(document).ready(function () {

    $('#addVideo').modal({
    	keyboard:true,
  		show: false,
  		remote: add_video_url
	});

    $('.datepicker').datepicker({
        format: 'dd.mm.yyyy'
    });
});