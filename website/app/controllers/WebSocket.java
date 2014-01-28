package controllers;

import com.google.gson.Gson;
import models.*;
import play.Logger;
import play.mvc.Http;
import play.mvc.Http.WebSocketEvent;
import play.mvc.WebSocketController;

import java.util.ArrayList;

public class WebSocket extends WebSocketController {

	public static void listen() {

        while( inbound.isOpen() ) {
            WebSocketEvent e = await(inbound.nextEvent());
            if(e instanceof Http.WebSocketFrame) {
                Http.WebSocketFrame frame = (Http.WebSocketFrame)e;
                outbound.send(frame.textData);
                Logger.info("Note send: "+frame.textData);
                saveNote(frame.textData);
                Logger.info("Note saved!");
            }
        }
    }

    private static void saveNote(String textData) {
        Logger.info("textdata receiver: "+textData);
        JsonNote jsonNote = new Gson().fromJson(textData, JsonNote.class);
        Logger.info("jsonNote: " + jsonNote.toString());

        Video video = Video.findById( Long.valueOf(jsonNote.videoId) );

        /* TODO: change to actual user */
        User user = new User("email", "pass", "user");
        user.save();

        Note note = new Note(
                jsonNote.title,
                jsonNote.content,
                jsonNote.startTime,
                video,
                user,
                null
        );

        String tagsString = jsonNote.tags; // Tags
        String[] tagsStringList = tagsString.split(";");
        ArrayList<Tag> tags = new ArrayList<Tag>();

        for(String tag : tagsStringList){
            Tag tagObj = new Tag(tag, note, video);
            tagObj.save();
            tags.add(tagObj);
        }
        note.tags = tags;
        video.tags = tags;
        video.save();
        note.save();

        System.out.println("Note saved!");
    }
}
