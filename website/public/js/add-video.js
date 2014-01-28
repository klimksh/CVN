$(document).ready(function () {

    $('#addVideo').modal({
    	keyboard:true,
  		show: false,
  		remote: add_video_url
	});

    $('#addNote').modal({
        keyboard: true,
        show: false,
        remote: add_note_url
    })



});