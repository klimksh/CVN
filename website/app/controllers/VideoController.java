package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import models.Tag;
import models.User;
import models.Video;
import play.mvc.Controller;

import java.util.*;

public class VideoController extends Controller {

	public static void index() {

        List<Video> videosList = Video.findAll();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonElement videos = gson.toJsonTree(videosList);
//        System.out.println(videos);
        render(videos);

    }

	public static void addVideo() {
		if (session.get("id") != null)
			session.put("id", "");
		render();
	}

	public static void saveVideo() {
		String title = request.params.get("video-title");
		String desc = request.params.get("video-description");

		/* TODO: add thumbnail to Video class */
		// String thumbnail = request.params.get("video-thumbnail");
		String link = request.params.get("video-id");

		String userEmail = request.params.get("user-email-data");

		User user = User.findByEmail(userEmail);
		if (user == null) {
			new User(userEmail, "", request.params.get("user-name-data"),
					request.params.get("user-id-data")).save();
		}

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		Video video = new Video(title, desc, link, new Date(year, month, day),
				new ArrayList<Tag>(), user);
		video.save();

        System.out.println(video);
        redirect("/video/" + video.id + "#header");
	}

	public static void video(String url) {
        List<Video> searchResult =  Video.find("url", url).fetch();
        Video video = (searchResult.size() >0 )? (Video) searchResult.get(0) :null;

        if (video == null) {
			redirect("/");
		}
		render(video);
	}

	public static void search(String query) {
		Video.searchQuery(query);
		List<Video> videos = Video.searchQuery(query);
		LinkedList<LinkedList<Video>> chunks = new LinkedList<LinkedList<Video>>();
		;
		int counter = 0;
		LinkedList<Video> chunk = new LinkedList<Video>();
		for (Video video : videos) {
			chunk.add(video);
			counter++;
			if (counter == 6) {
				chunks.add(chunk);
				chunk = new LinkedList<Video>();
				counter = 0;
			}
		}
		if (chunk.size() > 0) {
			chunks.add(chunk);
		}
		render(chunks);
	}
}
