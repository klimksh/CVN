package models;

import play.libs.F.EventStream;

public class VideoStream {
	public play.libs.F.EventStream<Note> liveStream;
	
	public Long videoId;

	public VideoStream(Long videoId) {
		super();
		this.liveStream = new play.libs.F.EventStream();
		this.videoId = videoId;
	}
	
}

