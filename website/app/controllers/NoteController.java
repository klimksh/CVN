package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import models.Note;
import models.Video;
import play.mvc.Controller;
import java.util.ArrayList;

import java.util.List;

public class NoteController extends Controller {

    public static void addNote(){
        render();
    }
    public static void getNotes(String id){

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Video video = Video.findById(Long.parseLong(id));
        List<Note> notes = Note.findAllByVideoId(video);
        List<Note> editedNotes = new ArrayList<Note>();

        // make id visible in json
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            note.fakeId = (Long)note.getId();
            editedNotes.add(note);
        }

        JsonElement json = gson.toJsonTree(editedNotes);
        renderJSON(json);
    }
}
