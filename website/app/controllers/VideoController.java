package controllers;

import models.Tag;
import models.User;
import models.Video;
import play.mvc.Controller;

import java.util.*;

public class VideoController extends Controller {

	public static void index() {
		List<Video> videos = Video.findAll();
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
			new User(userEmail, request.params.get("user-name-data"),
					request.params.get("user-id-data")).save();
		}

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		Video video = new Video(title, desc, link, new Date(year, month, day), user);
		video.save();

		video(video.id.toString());
	}

	public static void video(String id) {
		Video video = Video.findById(Long.parseLong(id));
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
