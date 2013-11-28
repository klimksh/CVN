/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 26/11/13
 * Time: 12:19
 */

$(function() {
    addTimeToPanel();

    $("#delay").blur(function(){
       if(parseInt($("#delay").val()) < 5) {
           $("#delay").val("5");
       }
        addTimeToPanel();
    });
})

function addTimeToPanel() {
    $("#timeline .note").each(function(){
        var startTime = parseInt($(this).data("start"));
        var endTime = startTime+parseInt($("#delay").val());
        var title = $(this).children(".panel-heading").html();
        if( title.indexOf("(") !== -1 ) {
            title = title.substr(0, title.indexOf("("));
        }

        $(this).children(".panel-heading").html(title+" ("+formatTime(startTime)+"-"+formatTime(endTime)+")");
        console.log("time added");
    });
}

function updateNoteFeed(currentVideoTime) {
    $(".note").each(function(){
        var noteEndTime = parseInt($(this).data("start"))+parseInt($("#delay").val());
        if(noteEndTime<currentVideoTime && $(this).is(':visible')) {
            console.log("check");
            $(this).hide(500, function(){
                "slide", { direction: "left" }
            });
        }
    });
}

function startTimer() {
    currentTime = 0;
    window.setInterval(function(){
        currentTime++;
        $("#timer").html(formatTime(currentTime));
        updateNoteFeed(currentTime);
    }, 1000);
}

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