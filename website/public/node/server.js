/**
 * This file is part of Collaborative VideoNotes.
 * User: Daniel
 * Date: 1/20/14
 * Time: 5:17 PM
 */

var io = require('socket.io').listen(8080);

io.sockets.on('connection', function (socket) {
    console.log('connected')
    socket.on('note', function(noteJsonString) {
        console.log('note detected: '+noteJsonString);
        io.sockets.emit('note', noteJsonString);
        console.log('note emitted');
    });
});