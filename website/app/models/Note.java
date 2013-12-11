package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "Notes")
public class Note extends Model {
    public String title; // title of the note
    public String content; // content will be around 140 charac
    public Date startTime; // start time in the video in which will be shown the note
    @ManyToOne
    public Video video; // the video in which is the note
    public User user; // user who has wrote it
    public long visited; // number of visitors who have visited this note
    @ManyToMany
    public List<Tag> noteTags;// tags of the note

    public Note(String title, String content, Date startTime, Video video, User user, ArrayList<Tag> noteTags) {
        super();
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.video = video;
        this.user = user;
        this.visited = 0;
        this.noteTags = noteTags;
        create();

    }

    public Note(String title, String content, Date startTime, Video video, User user, ArrayList<Tag> noteTags, long visited) {
        super();
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.video = video;
        this.user = user;
        this.visited = visited;
        this.noteTags = noteTags;
        create();
    }

    public static List<Note> findAllByVideoId(Video video) {
        return find("video = ? order by startTime desc", video).fetch();
    }
}
