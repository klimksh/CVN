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

    public static void saveNote(){
        String title = request.params.get("note-title-data");
        String content = request.params.get("note-content-data");
        int startTime = Integer.parseInt(request.params.get("note-start-data"));
        Video video = (Video) Video.find("byUrl", request.params.get("video-id-data")).fetch().get(1);
        /*change to actual user*/
        User user = new User("email", "pass", "user");
        user.save();

        Note note = new Note(title, content, startTime, /*endTime,*/ video, user, null);

        String tagsString = request.params.get("tags");
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


        System.out.println("///////////end//");

        redirect("/video/" + video.id);
    	LiveAnnotation.liveStream.publish(note);
//        try {
//            ServerSocket serverSocket = new ServerSocket(8080);
//            Socket clientSocket = serverSocket.accept();
//            BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );
//            String noteString;
//
//            while ((noteString = in.readLine()) != null) {
//                JsonArray jsonNote;
//                JsonParser jsonParser = new JsonParser();
//                jsonNote = jsonParser.parse(noteString).getAsJsonArray();
//
//                Video video = Video.findById(
//                        Long.parseLong(
//                                jsonNote.get(4).getAsString() // Video-ID
//                        )
//                );
//
//                /* TODO: change to actual user */
//                User user = new User("email", "pass", "user");
//                user.save();
//
//                Note note = new Note(
//                        jsonNote.get(0).getAsString(), // Title
//                        jsonNote.get(1).getAsString(), // Content
//                        jsonNote.get(2).getAsInt(),    // Start-Time
//                        video,
//                        user,
//                        null
//                );
//
//                String tagsString = jsonNote.get(3).getAsString(); // Tags
//                String[] tagsStringList = tagsString.split(";");
//                ArrayList<Tag> tags = new ArrayList<Tag>();
//
//                for(String tag : tagsStringList){
//                    Tag tagObj = new Tag(tag, note, video);
//                    tagObj.save();
//                    tags.add(tagObj);
//                }
//                note.tags = tags;
//                video.tags = tags;
//                video.save();
//                note.save();
//
//                System.out.println("Note saved!");
//            }
//
//        } catch (IOException e) {
//            System.out.println("Socket-Connection crashed: "+e.getMessage().toString());
//        }
    }

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
