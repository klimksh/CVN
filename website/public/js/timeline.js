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
    return "<div class='panel panel-default note' data-start='" + note.startTime + "'>" +
        "<div class='panel-heading'>" + note.title + " </div>" +
        "<div class='panel-body'>" + note.content + "</div>" +
        "<div class='panel-footer'>" +
        "<a href='#'>by " + note.noteWriter.name + "</a>" +
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

    // Start socket listener for note updates
}

function addNoteToTimeline(note) {
    var prevNote = findPrevNote(note.startTime);
    $('#noNotesAvailable').fadeOut();
    if (prevNote !== null) { // prevNote found, add after the prevNote
        prevNote.after(createNote(note));
    } else { // no notes found, add at beginning of timeline
        $("#timeline").prepend(createNote(note));
    }
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

/**
 * Receive Note-Updates via Socket and add them to the timeline if videoId is correct
 * Listening to Port 8080
 */
$(function(){
    var socket = io.connect('http://localhost:8080');

    socket.on('note', function(noteJsonString) {
        var noteObj = jQuery.parseJSON(noteJsonString);
        if(videoId === noteObj.videoId) {
            addNoteToTimeline(noteObj);
        }
    });
});

/**
 * Broadcast Note to all clients
 * Sending to Port 8080
 */
function broadcastNote() {
    var socket = io.connect('http://localhost:8080');

    var noteObj = new Object();
    noteObj.title       = $('#note-title').val();
    noteObj.content     = $('#note-content').val();
    noteObj.startTime   = $('#note-start-int').val();
    noteObj.tags        = $('#tags').val();
    noteObj.videoId     = videoId;
    noteObj.noteWriter  = new Object();
    noteObj.noteWriter.name = $('#username').val();

    console.log('Emit note');
    socket.emit('note', JSON.stringify(noteObj));
    console.log("Done sending note");
}