package controllers;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Note;
import models.VideoStream;
import play.mvc.Controller;
import play.mvc.WebSocketController;

public class LiveAnnotation extends Controller {
	
	private static HashMap<Long, VideoStream> videoStreams = new HashMap<Long, VideoStream>();
	
	public static VideoStream getVideoStream(Long videoId){
		VideoStream videoStream = videoStreams.get(videoId);
		if (videoStream != null)
			return videoStream;
		else {
			videoStream = new VideoStream(videoId);
			videoStreams.put(videoId, videoStream);
			return videoStream;
		}
		
	}

	public static class WebSocket extends WebSocketController {

		public static void stream(Long videoId) {
		    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			while (inbound.isOpen()) {
				VideoStream videoStream = getVideoStream(videoId);
                Note event = await(videoStream.liveStream.nextEvent());
				if (event != null) {
					String json_message = gson.toJson(event);
                    outbound.send(json_message);
				}
			}
		}
	}
}
