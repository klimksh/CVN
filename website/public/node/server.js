/**
 * This file is part of Collaborative VideoNotes.
 * User: Daniel
 * Date: 1/20/14
 * Time: 5:17 PM
 */

io = require('socket.io').listen(9000);

io.sockets.on('connection', function (socket) {

    socket.on('note', function(noteJsonString) {
        io.sockets.emit('note', noteJsonString);
    });

});