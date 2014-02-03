package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import models.Note;
import models.Video;
import play.mvc.Controller;

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

        JsonElement json = gson.toJsonTree(notes);
        renderJSON(json);
    }
}
