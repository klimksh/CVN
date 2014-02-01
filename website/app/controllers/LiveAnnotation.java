package controllers;

import models.Note;
import play.Logger;
import play.libs.F.Either;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.WebSocketEvent;
import play.mvc.WebSocketController;

public class LiveAnnotation extends Controller {
	public static play.libs.F.EventStream<Note> liveStream = new play.libs.F.EventStream();

	public static class WebSocket extends WebSocketController {

		public static void stream() {	
			while (inbound.isOpen()) {
				Note event = await(liveStream.nextEvent());    
				if (event != null) {
					outbound.sendJson(event);
				}
			}
		}
	}
}
