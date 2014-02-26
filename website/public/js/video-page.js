 $(function () {
    $(document).scrollTop($("#header").offset().top);
});

$('#note-content').autogrow();

function mustBeLoggedIn() {
    $('#mustBeLoggedIn').modal('show');
}

var player;

function onYouTubeIframeAPIReady() {
    player = new YT.Player('main_player', {
        height: '430',
        width: '780',
        videoId: video_url,
        events: {
            'onReady': onPlayerReady,
            'onStateChange': onPlayerStateChange
        }
    });
    setTimelineBoxSizes();
}

function onPlayerReady(event) {
    initializeTimeline(video_id);
    setTimelineBoxSizes();
}

function onPlayerStateChange(event) {
    if (event.data === 1) {  // if the video is playing start catching events
        checkTimePointsOnVideo();
    }
}

// !!!contains timer that runs slideTimeline
function checkTimePointsOnVideo() {
    if (!player.getCurrentTime) {
        return;
    }

    var interval = setInterval(function () {
        player.getPlayerState() // -- Possible values are unstarted (-1), ended (0), playing (1), paused (2), buffering (3), video cued (5).
        if (player.getPlayerState() === 1) {
            var played = Math.round(player.getCurrentTime());
            updateTimeline(played);
        } else if (player.getPlayerState() === 0 || player.getPlayerState() === 2) {
            clearInterval(interval);
        }
    }, 1000);
}

var startSeekTime = 0;
var endSeekTime = 0;

// Create a socket
var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
var socket = new WS(socket_pipe);

function sendNoteBySocket() {
    var bool = validateForm();
    if(bool === true) {
        $("#note-start-data").val(~~addNoteTime); // gets integer part
        var data = $('#mainFlipBox :input').serialize();
        $.post(save_note_url, data);
        flipMainPanel();
    }
}

function validateForm() {
    var title   = $('#note-title-data').val();
    var content = $('#note-content-data').val();
    if(title == "" && content == "") {
        $('#formValidateModal').modal('show');
        return false;
    }

    return true;
}

// Message received on the socket
socket.onmessage = function (event) {
    console.log(event);
    var obj = JSON.parse(event.data);
    // obj = eval("(" + obj + ")");
    var noteData = createNoteObject(obj);
    addNoteToNoteCollection(noteData);
};

socket.onopen = function (evt) {
    console.log("connected");
};

socket.onclose = function (evt) {
    console.log("disconnected");
};

socket.onerror = function (evt) {
    console.log("error: " + evt.data);
};

function createNoteObject(event) {
    var noteObj = new Object();
    noteObj.fakeId = Math.random().toString(36).substring(7);
    noteObj.title = event.title;
    noteObj.content = event.content;
    noteObj.startTime = event.startTime;
    noteObj.tags = event.tags;
    noteObj.username = event.noteWriter.name;

    return noteObj;
}