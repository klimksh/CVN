package controllers;

import models.Tag;
import models.User;
import models.Video;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Milya on 04.12.13.
 */
public class VideoController extends Controller {

    public static void index() {
    	List<Video> videos = Video.findAll();
        LinkedList<LinkedList<Video>> chunks = new LinkedList<LinkedList<Video>>();;
        int counter = 0;
        LinkedList<Video> chunk = new LinkedList<Video>();
        for (Video video : videos) {
        	chunk.add(video);
            counter++;
            if (counter==6){
            	chunks.add(chunk);
            	System.out.println(chunk);
                chunk = new LinkedList<Video>();
                counter=0;
            }
        }
        if (chunk.size()>0){
        	chunks.add(chunk);
        }

        render(chunks);
    }

    public static void addVideo() {
    	if(session.get("id")!=null)
       	 session.put("id","");
        render();
    }

    public static void saveVideo() {
        String title = request.params.get("video-title");
        String desc = request.params.get("video-description");

        /* TODO: add thumbnail to Video class*/
        String thumbnail = request.params.get("video-thumbnail");
        String link = request.params.get("video-id");

        /* TODO: change to actual user*/
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

        /* TODO: change to actual user*/
        User user = new User("email", "pass", "user");
        user.save();

        render(video, user);
    }

}
