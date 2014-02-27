package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import models.Note;
import models.Tag;
import models.User;
import models.Video;
import play.mvc.Controller;
import java.util.ArrayList;
import java.util.List;

public class NoteController extends Controller {

    public static void saveNote() {
        String title   = request.params.get("note-title-data");
        String content = request.params.get("note-content-data");
        int startTime  = Integer.parseInt(request.params.get("note-start-data"));
        Video video    = Video.findById(Long.parseLong(request.params.get("video-id-data")));

        String userEmail = request.params.get("user-email-data");

        User user = User.findByEmail(userEmail);
        if (user == null) {
            new User(userEmail, request.params.get("user-name-data"), request.params.get("user-id-data")).save();
            user = User.findByEmail(userEmail);
        }
        Note note = new Note(title, content, startTime, video, user);

        String tagsString = (request.params._contains("tags")) ? request.params.get("tags") : "";
        String[] tagsStringList = tagsString.split(",");
        ArrayList<Tag> tags = new ArrayList<Tag>();

        for (String tag : tagsStringList) {
            Tag tagObj = new Tag(tag);
            tagObj.save();
            tags.add(tagObj);
        }
        note.tags.addAll(tags);
        note.save();
        video.notes.add(note);
        video.save();
        LiveAnnotation.getVideoStream(video.id).liveStream.publish(note);
    }

    public static void addNote() {
        render();
    }

    public static void getNotes(String id) {

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Video video = Video.findById(Long.parseLong(id));
        List<Note> notes = video.notes;
        List<Note> editedNotes = new ArrayList<Note>();

        // make id visible in json
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            note.fakeId = (Long) note.getId();
            editedNotes.add(note);
        }

        JsonElement json = gson.toJsonTree(editedNotes);
        renderJSON(json);
    }
}
