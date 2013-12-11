$(function() {
    initializeTimeline();

    $("#delay").blur(function(){
       if(parseInt($("#delay").val()) < 5) {
           $("#delay").val("5");
       }
        addTimeToPanel();
    });
})

/**
 * Adds the timeframe a note is visible to the notes header
 * DEVELOPMENT ONLY
 */
function addTimeToPanel() {
    $("#timeline .note").each(function(){
        var startTime = parseInt($(this).data("start"));
        var endTime = startTime+parseInt($("#delay").val());
        var title = $(this).children(".panel-heading").html();
        if( title.indexOf("(") !== -1 ) {
            title = title.substr(0, title.indexOf("("));
        }

        $(this).children(".panel-heading").html(title+" ("+formatTime(startTime)+"-"+formatTime(endTime)+")");
        console.log("Time added to Note");
    });
}

/**
 * Removes old notes from the timeline, while watching a video
 * @param currentVideoTime
 */
function slideTimeline(currentVideoTime) {
    $(".note:not(.past)").each(function(){
        var noteEndTime = parseInt($(this).data("start"))+parseInt($("#delay").val());

        if(noteEndTime<=currentVideoTime ) {
            $(this).addClass("past");

            var width = 200; // $('.note').width();
            var currentPos = $('#timeline').scrollLeft(); // doesn't work if someone moves the slider by hand
            console.log("CurrentPos: "+currentPos);
            console.log("Width: "+width);

            $("#timeline").animate({scrollLeft: currentPos+width}, 500);
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

    s = Math.floor(timeInSeconds%60);
    m = Math.floor((timeInSeconds-s)/60)%60;

    if((timeInSeconds-s-m*60) !== 0) {
        h = Math.floor((timeInSeconds-s-m*60)/3600);
    } else {
        h = 0;
    }


    if(h > 0) {
        return h+"h"+m+"m"+s+"s";
    } else if(m > 0) {
        return m+"m"+s+"s";
    } else {
        return s+"s";
    }
}

/**
 * Create a single Note from JsonObject
 * @param singleNote
 * @returns {string}
 */
function createNote( singleNote ) {
    return "<div class='panel panel-default note' data-start='"+singleNote.startTime
        +"s'><div class='panel-heading'>"+singleNote.title
        +"</div><div class='panel-body'>"+singleNote.content
        +"</div><div class='panel-footer'><a href='/user?id="+singleNote.user.id+"'>by "+singleNote.user.name
        +"</a></div></div>";
}

/**
 * Creates all notes and adds them to the timeline
 */
function initializeTimeline() {
    $.getJSON( "/public/data/data.json", function( data ) {
        var notes = [];
        $.each( data, function( index ) {
            if(data[index].title !== "") {
                notes.push( createNote(data[index]) );
            }
        });

        $("#timeline").html( notes.join( "" ) );
    });
}

/**
 * Adds notes to the timeline after initialization
 */
function updateTimeline() {
    $.getJSON( "data/update.json", function( data ) {
        $.each( data, function( index ) {
            var prevNote = findPrevNote(data[index].startTime);
            if(prevNote !== null) { // prevNote found, add after the prevNote
                prevNote.after( createNote(data[index]) );
            } else { // no notes found, add at beginning of timeline
                $("#timeline").prepend( createNote(data[index]) );
            }
        });
    });
}

/**
 * Searches for the previous note to a certain startTime
 * @param startTime
 * @returns prevNote | null
 */
function findPrevNote(startTime) {
    var prevNote = null;
    var time = 0;
    $(".note").each(function() {
        time = parseInt( $(this).data("start") );
        if(time <= startTime) {
            prevNote = $(this);
        }
    });

    return prevNote;
}