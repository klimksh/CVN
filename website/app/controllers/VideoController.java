package controllers;

import models.Tag;
import models.User;
import models.Video;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Milya on 04.12.13.
 */
public class VideoController extends Controller {

    public static void index() {
    	List<Video> videos = Video.findAll();
    	render(videos);
    }

    public static void addVideo() {
    	if(session.get("id")!=null)
       	 session.put("id","");
        render();
    }

    public static void saveVideo() {
        String title = request.params.get("video-title");
        String desc = request.params.get("video-description");
        /*add thumbnail to Video class*/
        String thumbnail = request.params.get("video-thumbnail");
        String link = request.params.get("video-id");
        /*change to actual user*/
        User user = new User("email", "pass", "user");
        user.save();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        Video video = new Video(title, desc, link, new Date(year, month, day), new ArrayList<Tag>(), user);
        video.save();

        redirect("/video/" + video.id);
    }

    public static void video(String id){
        Video video = Video.findById(Long.parseLong(id));
        if(video == null) {
            redirect("/");
        }
        render(video);
    }

}
