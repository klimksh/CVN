package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Tags")
public class Tag extends Model {

    // because of many to many relationship
    @ManyToMany(mappedBy = "noteTags")
    public List<Note> notes;
    @ManyToMany(mappedBy = "videoTags")
    public List<Video> videos;
    public String title;

    public Tag(String title) {
        super();
        notes = new ArrayList<Note>();
        videos = new ArrayList<Video>();
        this.title = title;
        create();
    }

    public static Tag findByTitle(String title) {
        return find("title", title).first();
    }
}
