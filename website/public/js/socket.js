/**
 * Create a Note Object for the transport via socket
 */
function createNoteObject() {
    var noteObj = new Object();
    noteObj.title       = $('#note-title').val();
    noteObj.content     = $('#note-content').val();
    noteObj.startTime   = convertTimeToSec();
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
    // depending on the browser we have to use WebSocket or MozWebSocket
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var websocket = new WS("ws://localhost:9000/websocket/listen");

    // save a note when the 'save' button is clicked
    $('#saveNote').click(function() {
        console.log("Click & Save");
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