package controllers;

import models.Note;
import play.mvc.Controller;
import play.mvc.WebSocketController;

public class LiveAnnotation extends Controller {
	public static play.libs.F.EventStream<Note> liveStream = new play.libs.F.EventStream();

	public static class WebSocket extends WebSocketController {

		public static void stream() {	
			while (inbound.isOpen()) {
                System.out.println("+++++++++++++++");
                Note event = await(liveStream.nextEvent());
				if (event != null) {
                    System.out.println("22222222222222222222");
                    outbound.sendJson(event);
				}
			}
		}
	}
}
