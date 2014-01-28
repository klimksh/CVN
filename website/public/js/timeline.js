var delayTime = 10;
var videoId = $('#video-id').val();

/**
 * Adds the timeframe a note is visible to the notes header
 * DEVELOPMENT ONLY
 */
function addTimeToPanel() {
    $("#timeline .note").each(function () {
        var startTime = parseInt($(this).data("start"));
        var title = $(this).children(".panel-heading").html();
        if (title.indexOf("(") !== -1) {
            title = title.substr(0, title.indexOf("("));
        }

        $(this).children(".panel-heading").html(title + " (" + formatTime(startTime) + "-" + formatTime(startTime+delayTime) + ")");
    });
}

$('#autoslide').change(function(){
    if( $(this).is(':checked') ) {
        console.log("manual slide");
        var pastNotes = Math.max(0, $('.past').length);
        var width = 200; // $('.note').width();
        var scrollToPosition = width*pastNotes;
        $("#timeline").animate({scrollLeft: scrollToPosition}, 100);
    }
});

/**
 * Removes old notes from the timeline, while watching a video
 * @param currentVideoTime
 */
function slideTimeline(currentVideoTime) {
    $(".note:not(.past)").each(function () {
        console.log("Future note found");
        var currentPosition = $('#timeline').scrollLeft();
        var pastNotes = Math.max(0, $('.past').length);
        var width = 200; // $('.note').width();

        var slideToleranceMin = (pastNotes-1)*width;
        var slideToleranceMax = (pastNotes+1)*width;

        if(currentPosition<slideToleranceMin || currentPosition>slideToleranceMax) {
            $('#autoslide').prop('checked', false);
            console.log("Out of tolerance area -> automatical sliding deactivated");
        }

        var noteEndTime = parseInt($(this).data("start"))+delayTime;
        var autoSlide = $('#autoslide').is(':checked');

        if (noteEndTime < currentVideoTime) {
            console.log("Passed note! delay: "+delayTime+" endTime: "+noteEndTime+" Curr: "+currentVideoTime);
            $(this).addClass("past");
            pastNotes++;

            if(autoSlide) {
                var scrollToPosition = width*pastNotes;
                console.log("scroll from "+currentPosition+" to "+scrollToPosition);
                $("#timeline").animate({scrollLeft: scrollToPosition}, 500);
            }
        }
    });
}

/**
 * Formatting the timeInSeconds
 * Examples: 5s, 2m5s, 1h2m5s
 * @param timeInSeconds
 * @returns {string}
 */
function formatTime(timeInSeconds) {
    var h, m, s;

    s = Math.floor(timeInSeconds % 60);
    m = Math.floor((timeInSeconds - s) / 60) % 60;

    if ((timeInSeconds - s - m * 60) !== 0) {
        h = Math.floor((timeInSeconds - s - m * 60) / 3600);
    } else {
        h = 0;
    }


    if (h > 0) {
        return h + "h" + m + "m" + s + "s";
    } else if (m > 0) {
        return m + "m" + s + "s";
    } else {
        return s + "s";
    }
}

/**
 * Create a single Note from JsonObject
 * @param note
 * @returns {string}
 */
function createNote(note) {
    var username;
    if(note.noteWriter !== undefined) {
        username = note.noteWriter.name;
    } else {
        username = note.username;
    }
    return "<div style='display:none' id='note"+note.startTime+"' class='panel panel-default note' data-start='" + note.startTime + "'>" +
        "<div class='panel-heading'>" + note.title + " </div>" +
        "<div class='panel-body'>" + note.content + "</div>" +
        "<div class='panel-footer'>" +
        "<a href='#'>by " + username + "</a>" +
        "</div>" +
        "</div>";
}

/**
 * Creates all notes and adds them to the timeline
 */
function initializeTimeline(videoId) {
    $.getJSON("/notes/"+videoId, function (data) {
        $.each(data.elements, function (index, note) {
            if (note.title !== "") {
                addNoteToTimeline(note);
            }
        });
    });
}

function addNoteToTimeline(note) {
    var prevNote = findPrevNote(note.startTime);
    $('#noNotesAvailable').fadeOut();
    if (prevNote !== null) { // prevNote found, add after the prevNote
        prevNote.after(createNote(note));
    } else { // no notes found, add at beginning of timeline
        $("#timeline").prepend(createNote(note));
    }
    $('#note'+note.startTime).fadeIn();
}

/**
 * Searches for the previous note to a certain startTime
 * @param startTime
 * @returns prevNote | null
 */
function findPrevNote(startTime) {
    var prevNote = null;
    var time = 0;
    $(".note").each(function () {
        time = parseInt($(this).data("start"));
        if (time <= startTime) {
            prevNote = $(this);
        }
    });

    return prevNote;
}

function getStartTime() {
    var time = $('#note-start').val();
    split = time.split(':');
    return parseInt(split[0])*60+parseInt(split[1]);
}

/**
 * Create a Note Object
 */
function createNoteObject() {
    var noteObj = new Object();
    noteObj.title       = $('#note-title').val();
    noteObj.content     = $('#note-content').val();
    noteObj.startTime   = getStartTime();
    noteObj.tags        = $('#tags').val();
    noteObj.videoId     = videoId;
    noteObj.username    = $('#username').val();

    console.log(noteObj);
    return noteObj;
}

/**
 * Initiate socket connection for specific video-page
 * @param videoId
 */
function initSockets() {

    //depending on the browser we have to use WebSocket or MozWebSocket
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var websocket = new WS("ws://localhost:9000/websocket/listen");

    //sends a message when the 'send' button is clicked
    $('#note-save').click(function() {
        var noteObj = createNoteObject();
        websocket.send(JSON.stringify(noteObj))
    });

    websocket.onmessage = function (event) {
        console.log('Note received');
        noteObj = eval("("+event.data+")");
        if(noteObj.videoId == videoId) {
            addNoteToTimeline(noteObj);
        }
    }
}