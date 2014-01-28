package models;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity(name = "JsonNotes")
public class JsonNote extends Model {
    @Expose
    public String title; // title of the note
    @Expose
    public String content; // content will be around 140 charac
    @Expose
    public int startTime; // start time in the video in which will be shown the note
    @Expose
    public int videoId; // the video in which is the note
    @Expose
    public String username; // user who has wrote it
    @Expose
    public long visited; // number of visitors who have visited this note
    @Expose
    public String tags;// tags of the note

    public JsonNote(String title, String content, int startTime, int videoId, String username, String tags) {
        super();
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.videoId = videoId;
        this.username = username;
        this.visited = 0;
        this.tags = tags;
        create();

    }

    public JsonNote(String title, String content, int startTime, int videoId, String username, String tags, long visited) {
        super();
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.videoId = videoId;
        this.username = username;
        this.visited = visited;
        this.tags = tags;
        create();
    }
}
 
