var delayTime = 10;

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

/**
 * Removes old notes from the timeline, while watching a video
 * @param currentVideoTime
 */
function slideTimeline(currentVideoTime) {
    $(".note:not(.past)").each(function () {
        var noteEndTime = parseInt($(this).data("end"));

        if (noteEndTime <= currentVideoTime) {
            $(this).addClass("past");

            var width = 200; // $('.note').width();
            var currentPos = $('#timeline').scrollLeft(); // doesn't work if someone moves the slider by hand

            $("#timeline").animate({scrollLeft: currentPos + width}, 500);
        }
    });
}

/**
 * Formats the timeInSeconds
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
    return "<div class='panel panel-default note' data-start='" + note.startTime + "s'>" +
        "<div class='panel-heading'>" + note.title + " </div>" +
        "<div class='panel-body'>" + note.content + "</div>" +
        "<div class='panel-footer'>" +
        "<a href='/user/" + note.noteWriter.id + "'>by " + note.noteWriter.name + "</a>" +
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