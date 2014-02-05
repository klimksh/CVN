package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Note;
import play.mvc.Controller;
import play.mvc.WebSocketController;

public class LiveAnnotation extends Controller {
	public static play.libs.F.EventStream<Note> liveStream = new play.libs.F.EventStream();

	public static class WebSocket extends WebSocketController {

		public static void stream() {	
			while (inbound.isOpen()) {
                Note event = await(liveStream.nextEvent());
				if (event != null) {
                    Gson gson = new GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create();
                    outbound.sendJson(gson.toJson(event));
				}
			}
		}
	}
}
