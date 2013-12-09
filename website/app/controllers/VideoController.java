package controllers;

import models.Tag;
import models.User;
import models.Video;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
}
