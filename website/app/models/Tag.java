package models;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;

import play.modules.elasticsearch.annotations.ElasticSearchEmbedded;
import play.modules.elasticsearch.annotations.ElasticSearchField;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
//@ElasticSearchable
@Entity(name = "Tags")
public class Tag extends Model {

    // because of many to many relationship
    @ElasticSearchIgnore
	@ManyToMany(mappedBy="noteTags")
    public List<Note> tagNotes;
	@ElasticSearchIgnore
    @ManyToMany(mappedBy = "tags" )
    public List<Video> videos;

    @Expose
    public String tagTitle;

    public Tag(String title) {
        this.tagTitle = title;       
        create();
    }

    public Tag(String title, Note note, Video video) {
         this.tagNotes.add(note);
         this.videos.add(video);
         this.tagTitle = title;
          create();
    }

    public static Tag findByTitle(String title) {
        return find("title", title).first();
    }
}
