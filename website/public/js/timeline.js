var delayTime = 10;
var videoId = $('#video-id').val();
var mainPanelFlipped = false;
var allNotes = Array();
var noNotesText = "<span class='noNotesText'>Currently there are no notes for this video. Feel free to add some, by clicking the <span style='color: #13987e'>+ New note</span> button on the top right.</span>";

/**
 * Flip between the "new Note"-Form and the current Notes
 */
function flipMainPanel() {
    if(mainPanelFlipped === false) {
        $("#myFlippyBox").flippy({
            verso: $('#newNoteForm').html(),
            direction:"TOP",
            duration:"500",
            color_target:"#f8f8f8"
        });
        mainPanelFlipped = true;
    } else {
        $("#myFlippyBox").flippyReverse();
        mainPanelFlipped = false;
    }
    $('#addNoteBtn').toggle();
    $('#revertFlip').toggle();
}

/**
 * Creates all notes and adds them to the timeline
 */
function initializeTimeline(videoId) {
    $.getJSON("/notes/"+videoId, function (data) {
        $.each(data.elements, function (index, note) {
            if (note.title !== "") {
                allNotes.push(note);
            }
        });
        updateTimeline(0); // update Timeline for 0 sec
    });
}

/**
 * Auto resize all note-boxes when browser gets resized
 */
$( window ).resize(function(){
    setTimelineBoxSizes();
});

/**
 * Set the width of all timeline boxes to fit the video-player
 */
function setTimelineBoxSizes(){
    var playerWidth = $('iframe#player').width();
    var browserViewport = $('#content').width();
    var options = $('#options').width();

    var mainBox = playerWidth-options-12; // 12px = padding
    var sideBox = (browserViewport-mainBox-75)/2;

    $('#mainPanel').css('width', mainBox);
    if (browserViewport-playerWidth > 450) {
        $('#pastNotes, #futureNotes').fadeIn();
        $('#pastNotes, #futureNotes').css('width', sideBox);
        $('#timeline').css('width', sideBox*2+mainBox+options+14);  // 12px = padding
    } else {
        $('#pastNotes, #futureNotes').fadeOut();
        $('#timeline').css('width', playerWidth);
    }

    // positioning below video
    var leftOffset = $('iframe#player').offset().left;
    if( $('#pastNotes').is(':visible') ) {
        leftOffset = leftOffset-40-$('#pastNotes').width();
    }
    $('#timeline').offset({ left: leftOffset });
}
/**
 * Move notes to their part of the timeline (past, current, future)
 * @param currentVideoTime
 */
function updateTimeline(currentVideoTime) {
    if( !$('#autoslide').is(':checked') ) {
        return;
    }

    // Filter notes
    allNotes.forEach(function(note) {
        if(note.startTime+delayTime < currentVideoTime) {
            addNoteToTimeline('pastNotes', note);
            $('#currentNotes #note'+note.startTime).remove();
        } else if (note.startTime+delayTime <= (currentVideoTime+30)) { // Timeframe of 30 seconds
            addNoteToTimeline('currentNotes', note);
            $('#futureNotes #note'+note.startTime).remove();
        } else {
            addNoteToTimeline('futureNotes', note);
        }
    });

    // Set noNotes text if there are no notes for this period
    if( $('#pastNotes .note').length <= 0 ) {
        $('#pastNotes').html(noNotesText);
    }
    if( $('#currentNotes .note').length <= 0 ) {
        $('#currentNotes').html(noNotesText);
    }
    if( $('#futureNotes .note').length <= 0 ) {
        $('#futureNotes').html(noNotesText);
    }

    // Update Timer
    $('#currentVideoTime').html(convertSecToTime(currentVideoTime));
}

/**
 * Adds a note to one part of the timeline (past, current, future), if not already there
 * @param (pastNotes|currentNotes|futureNotes)
 * @param note
 */
function addNoteToTimeline(dom, note) {
    $('#'+dom+' .noNotesText').hide();
    var prevNote = findPrevNote(dom, note.startTime);
    if (prevNote !== null) { // prevNote found, add after the prevNote
        prevNote.after(createNote(note));
    } else { // no notes found, add at beginning of timeline
        $("#timeline #"+dom).prepend(createNote(note));
    }
    $('#note'+note.startTime).fadeIn();
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
    return "<div style='display:none' id='note"+note.startTime+"' class='note' data-start='" + note.startTime + "'>" +
                "<p>" + note.title + " </p>" +
           "</div>";
}

/**
 * Searches for the previous note to a certain startTime
 * @param startTime
 * @returns prevNote | null
 */
function findPrevNote(dom, startTime) {
    var prevNote = null;
    var time = 0;
    $("#"+dom+" .note").each(function () {
        time = parseInt($(this).data("start"));
        if (time <= startTime) {
            prevNote = $(this);
        }
    });
    return prevNote;
}

/**
 * Converts the formated time from new notes into seconds
 * @returns time in seconds
 */
function convertSecToTime(time) {
    var hours = Math.floor(time / 3600);
    var minutes = Math.floor(time / 60);
    var seconds = time - minutes * 60;

    if(minutes<10) {
        minutes = "0"+minutes;
    }
    if(seconds<10) {
        seconds = "0"+seconds;
    }

    if(hours>0) {
        return hours+":"+minutes+":"+seconds;
    } else {
        return minutes+":"+seconds;
    }
}
/**
 * Converts the formated time from new notes into seconds
 * @returns time in seconds
 */
function convertTimeToSec() {
    var time = $('#currentVideoTime').val();
    split = time.split(':');
    return parseInt(split[0])*60+parseInt(split[1]);
}