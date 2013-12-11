package controllers;

import models.Tag;
import models.User;
import models.Video;
import models.Note;
import play.mvc.Controller;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Milya on 04.12.13.
 */
public class VideoController extends Controller {

    public static void getAllVideos() {
    }

    public static void addVideo() {
        render();
    }

    public static void saveVideo() {
        String title = request.params.get("video-title");
        String desc = request.params.get("video-description");
        /*add thumbnail to Video class*/
        String thumbnail = request.params.get("video-thumbnail");
        String link = request.params.get("video-link");
        /*change to actual user*/
        User user = new User("email", "pass", "user");
        user.save();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        Video video = new Video(title, desc, link, new Date(year, month, day), new ArrayList<Tag>(), user);
        video.save();
    }

    public static void getVideo(String id){
        render(id);
    }

    public static void getNotes(Long id){
        Gson gson = new Gson();
        Video video = null;
        video.findById(id);

        List<Note> notes = Note.findAllByVideoId(video);
        String json = gson.toJson(notes);
        render(json);
    }
}
