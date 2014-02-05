package models;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
//@ElasticSearchable
@Entity(name = "Notes")
public class Note extends Model {
    @Expose
    public String title; // title of the note
    @Expose
    public String content; // content will be around 140 charac
    @Expose
    public int startTime; // start time in the video in which will be shown the note
    //public int endTime;
    @ManyToOne
    public Video video; // the video in which is the note
    @Expose
    @OneToOne
    public User noteWriter; // user who has wrote it
    @Expose
    public long visited; // number of visitors who have visited this note
    @Expose
    @ManyToMany
    public List<Tag> tags;// tags of the note

    public Note(String title, String content) {
        super();
        this.title = title;
        this.content = content;
    }
    
    public Note(String title, String content, int startTime, /*int endTime,*/ Video video, User user, ArrayList<Tag> tags) {
        super();
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        /*this.endTime = endTime;*/
        this.video = video;
        this.noteWriter = user;
        this.visited = 0;
        if(tags!=null) this.tags.addAll(tags);
        //create();

    }

    public Note(String title, String content, int startTime, /*int endTime,*/ Video video, User user, ArrayList<Tag> tags, long visited) {
        super();
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        /*this.endTime = endTime;*/
        this.video = video;
        this.noteWriter = user;
        this.visited = visited;
        this.tags = tags;
    }

    public static List<Note> findAllByVideoId(Video video) {
        return find("video = ? order by startTime desc", video).fetch();
    }
}
 
