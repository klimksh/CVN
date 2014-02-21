var periodLength     = 60;
var currentPeriodStart = 0;
var mainPanelFlipped = false;
var optionsFlipped   = false;
var lastClickedNote  = null;
var allNotes         = Array();
var noPastNotesText     = "<span class='noNotesText'>Currently there are no past notes for this video. If you progress in the video and pass some notes, they will appear here.</span>";
var noCurrentNotesText  = "<span class='noNotesText'>Currently there are no current notes for this video. If you progress in the video and pass some notes, they will appear here. Feel free to add some, by clicking the <span style='color: red'>+ New note</span> button</span>";
var noFutureNotesText   = "<span class='noNotesText'>Currently there are no future notes for this video. Feel free to add some, by clicking the <span style='color:red'>+ New note</span> button</span>";
var newNoteFormContent  = "";
var optionsContent      = "";
var lastVideoUpdate     = 0;
var addNoteTime         = 0;
var timelineIsSynced    = true;

// save the form content and options content to avoid ID-conflicts
$(function(){
    newNoteFormContent  = $('#newNoteForm').html();
    optionsContent      = $('#optionsPanel').html();
    $('#newNoteForm').remove();
});

/**
 * Flip between the "new Note"-Form and the current Notes
 */
function flipMainPanel() {
    if(mainPanelFlipped === false) {
        $("#mainFlipBox").flippy({
            verso: newNoteFormContent,
            direction:"TOP",
            duration:"500",
            color_target:"",
            onStart:function(){
                if(timelineIsSynced) {
                    addNoteTime = lastVideoUpdate;
                } else {
                    addNoteTime = currentPeriodStart;
                }
            }
        });
        mainPanelFlipped = true;
    } else {
        $("#mainFlipBox").flippyReverse();
        mainPanelFlipped = false;
    }
    $('#addNoteBtn').toggle();
    $('#revertFlip').toggle();
    initNoteFlip();
}

/**
 * Flip between the options/noteContent
 */
function initNoteFlip() {
    $('.note').click(function(){
        var note = $(this);

        if( lastClickedNote == note.attr('id') ) {
            revertOptionsFlip();
        } else if(optionsFlipped === false || lastClickedNote != $(this).attr('id')) {
            $('.noteHighlighted').removeClass('noteHighlighted');
            title   = note.children('p').html();
            content = note.data('content');
            user    = note.data('user');
            noteContent = prettifyNoteContent(title, content, user);

            $("#optionsPanel").flippy({
                verso: noteContent,
                direction:"TOP",
                duration:"500",
                color_target:"",
                onFinish:function(){
                    // update LastClickedNote to the current note
                    lastClickedNote = note.attr('id');

                    // mark as current note
                    note.addClass('noteHighlighted');

                    // activate close-button on noteBlock
                    $('#closeNoteBlock').click(function(){
                        revertOptionsFlip();
                    });
                    // activate jump Button
                    $('#jumpBtn').click(function(){
                        var startTime = $('#'+lastClickedNote).data('start');
                        player.seekTo(startTime);
                        updateTimeline(startTime);
                    });
                }
            });
            optionsFlipped = true;
        }
    });
}

function revertOptionsFlip() {
    $('.noteHighlighted').removeClass('noteHighlighted');
    $("#optionsPanel").flippy({
        verso: optionsContent,
        direction:"TOP",
        duration:"500",
        color_target:"",
        onFinish:function(){
            setTimer();
        }
    });
    optionsFlipped = false;

}

function prettifyNoteContent(title, content, user) {
    return  '<div class="noteBlock panel panel-default">' +
                '<div class="panel-heading">'+title+'<span id="closeNoteBlock" class="glyphicon glyphicon-remove pull-right"></span></div>' +
                '<div class="panel-body">'+content+'</div>' +
                '<div class="panel-footer">by '+user+' <span id="jumpBtn" class="label label-danger">Jump</span></div>' +
            '</div>'
}
/**
 * Creates all notes and adds them to the timeline
 */
function initializeTimeline(videoId) {
    $.getJSON("/notes/"+videoId, function (data) {
        $.each(data.elements, function (index, note) {
            if (note.title !== "" || note.content !== "") {
                addNoteToNoteCollection(note)
            }
        });
    });
}

/**
 * Adds the note to the Array of all notes and updates the timeline
 * @param note
 */
function addNoteToNoteCollection(note) {
    allNotes.push(note);
    updateTimeline(lastVideoUpdate);
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
    var playerWidth = $('iframe#main_player').width();
    var browserViewport = $('#content').width();
    var options = $('#optionsPanel').width();

    var mainBox = playerWidth-options-3; // 3px = padding and borders
    var sideBox = (browserViewport-mainBox-75)/2;

    $('#mainPanel').css('width', mainBox);
    if (browserViewport-playerWidth > 450) {
        $('#pastNotes, #futureNotes').fadeIn();
        $('#pastNotes, #futureNotes').css('width', sideBox);
        $('#timeline').css('width', sideBox*2+mainBox+options+14);  // 14px = padding
    } else {
        $('#pastNotes, #futureNotes').fadeOut();
        $('#timeline').css('width', playerWidth);
    }

    // positioning below video
    var leftOffset = $('iframe#main_player').offset().left;
    // second if is necessary because during fadeIn/Out the boxes are still visible and cause errors
    if( $('#pastNotes').is(':visible') && $('#pastNotes').width() > 100 ) {
        leftOffset = leftOffset-40-$('#pastNotes').width();
    }

    $('#timeline').offset({ left: leftOffset });
}
/**
 * Move notes to their part of the timeline (past, current, future)
 * @param currentVideoTime
 */
function updateTimeline(currentVideoTime) {
    if( !timelineIsSynced ) {
        return;
    }

    // video has been rewinded
    if( lastVideoUpdate > currentVideoTime) {
        $('.note').remove();
    }

    lastVideoUpdate = currentVideoTime;
    currentPeriodStart = currentVideoTime;

    // Filter notes
    allNotes.forEach(function(note) {
        if(note.startTime+periodLength < currentVideoTime) {
            addNoteToTimeline('pastNotes', note);
        } else if (note.startTime+periodLength <= (currentVideoTime+periodLength)) {
            addNoteToTimeline('currentNotes', note);
        } else {
            addNoteToTimeline('futureNotes', note);
        }
    });
    addManualSlideButtons();

    // Set noNotes text if there are no notes for this period
    if( $('#pastNotes .note').length <= 0 ) {
        $('#pastNotes').html(noPastNotesText);
    }
    if( $('#currentNotes .note').length <= 0 ) {
        $('#currentNotes').html(noCurrentNotesText);
        addManualSlideButtons();
    }
    if( $('#futureNotes .note').length <= 0 ) {
        $('#futureNotes').html(noFutureNotesText);
    }

    setTimer();
    initNoteFlip();
}

function addManualSlideButtons() {
    var leftButton, rightButton, leftButtonDisabled = "", rightButtonDisabled = "";
    $('#scrollLeft').remove();
    $('#scrollRight').remove();
    $('#currentNotes .clearfix').remove();

    if(currentPeriodStart <= 0) {
        leftButtonDisabled = 'disabled="disabled"';
    }
    if( currentPeriodStart+periodLength >= player.getDuration() ) {
        rightButtonDisabled = 'disabled="disabled"';
    }

    leftButton = '<div class="manualSlideButton btn btn-danger pull-left" id="scrollLeft" '+leftButtonDisabled+'>&laquo;</div>';
    rightButton = '<div class="manualSlideButton btn btn-danger pull-right" id="scrollRight" '+rightButtonDisabled+'>&raquo;</div>';

    $('#currentNotes').prepend("<div class='clearfix'></div>").prepend(rightButton).prepend(leftButton);

    initManualSlideButtons();
}

function initManualSlideButtons() {
    $('#scrollLeft').click(function() {
        if(currentPeriodStart-periodLength <= lastVideoUpdate && currentPeriodStart > lastVideoUpdate) {
            syncTimelineWithVideo();
        } else {
            checkSyncMode();
            currentPeriodStart = Math.max(0, currentPeriodStart-periodLength);
            asyncTimelineUpdate();
            changeDisplayTimeToPeriod();
        }
    });

    $('#scrollRight').click(function() {
        if(currentPeriodStart+periodLength <= lastVideoUpdate && currentPeriodStart+2*periodLength > lastVideoUpdate) {
            syncTimelineWithVideo();
        } else {
            checkSyncMode();
            currentPeriodStart = Math.min(Math.floor(player.getDuration()-periodLength), currentPeriodStart+periodLength);
            asyncTimelineUpdate();
            changeDisplayTimeToPeriod();
        }
    });
}

function changeDisplayTimeToPeriod() {
    $('#timerText').html('Period');
    var start = convertSecToTime(currentPeriodStart);
    var end = convertSecToTime(currentPeriodStart+periodLength);

    $('#timer').html(start+"-"+end);
}

function changeDisplayPeriodToTime() {
    $('#timerText').html('Time');
    setTimer();
}

function checkSyncMode() {
    if( timelineIsSynced ) {
        timelineIsSynced = false;
        var syncButton = '<div class="btn btn-danger btn-xs" id="syncButton">Sync with Video</div>';
        $('#syncText').html("<div class='alert alert-danger'><span class='glyphicon glyphicon-share'></span> Timeline is asynchronous "+syncButton+"</div>");

        $('#syncButton').click(function(){
            syncTimelineWithVideo();
        });
    }
}

function syncTimelineWithVideo() {
    console.log('sync with video');
    $('#syncText').html("<div class='alert alert-success'><span class='glyphicon glyphicon-check'></span> Timeline is syncronized with the video</div>");
    timelineIsSynced = true;
    changeDisplayPeriodToTime();
}

function asyncTimelineUpdate() {
    $('.note').remove();

    // Filter notes
    allNotes.forEach(function(note) {
        if(note.startTime+periodLength < currentPeriodStart) {
            addNoteToTimeline('pastNotes', note);
        } else if (note.startTime+periodLength <= (currentPeriodStart+periodLength)) {
            addNoteToTimeline('currentNotes', note);
        } else {
            addNoteToTimeline('futureNotes', note);
        }
    });

    // Set noNotes text if there are no notes for this period
    if( $('#pastNotes .note').length <= 0 ) {
        $('#pastNotes').html(noPastNotesText);
    }
    if( $('#currentNotes .note').length <= 0 ) {
        $('#currentNotes').html(noCurrentNotesText);
        addManualSlideButtons();
    }
    if( $('#futureNotes .note').length <= 0 ) {
        $('#futureNotes').html(noFutureNotesText);
    }
    addManualSlideButtons();

    setTimer();
    initNoteFlip();
}

/**
 * Update Timer
 */
function setTimer() {
    $('#timer').html(convertSecToTime(lastVideoUpdate));
}

/**
 * Adds a note to one part of the timeline (past, current, future), if not already there
 * @param (pastNotes|currentNotes|futureNotes)
 * @param note
 */
function addNoteToTimeline(dom, note) {
    if( $('#'+dom+' #note'+note.fakeId).length > 0 ) {
        return;
    }

    switch(dom) {
        case 'pastNotes':
            $('#currentNotes #note'+note.fakeId).remove();
            $('#futureNotes #note'+note.fakeId).remove();
            break;
        case 'currentNotes':
            $('#futureNotes #note'+note.fakeId).remove();
            break;
    }

    $('#'+dom+' .noNotesText').hide();
    var prevNote = findPrevNote(dom, note.startTime);

    if (prevNote !== null) { // prevNote found, add after the prevNote
        prevNote.after(createNote(note));
    } else { // no notes found, add at beginning of timeline
        $("#timeline #"+dom).prepend(createNote(note));
    }
    $('#note'+note.fakeId).fadeIn();
}

/**
 * Create a single Note from JsonObject
 * @param note
 * @returns {string}
 */
function createNote(note) {
    var username;
    var title;
    var content;

    if(note.noteWriter !== undefined) {
        username = note.noteWriter.name;
    } else {
        username = note.username;
    }
    if(note.title == "") {
        title = note.content.substr(0,Math.min(note.content.length,20))+"...";
    } else {
        title = note.title;
    }
    if(note.content == "") {
        content = "This note does not have any content.";
    } else {
        content = note.content;
    }

    return "<div style='display:none' id='note"+note.fakeId+"' class='note' data-start='" + note.startTime + "' data-user='"+username+"' data-content='"+content+"'>" +
                "<p>" + title + " </p>"
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
function convertSecToTime(timeInSeconds) {
    var hours   = Math.floor(timeInSeconds / 3600);
    var minutes = Math.floor((timeInSeconds-hours*3600) / 60);
    var seconds = timeInSeconds - minutes * 60 - hours * 3600;

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
    var time = $('#timer').val();
    split = time.split(':');
    return parseInt(split[0])*60+parseInt(split[1]);
}