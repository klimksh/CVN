package models;
import com.google.gson.annotations.Expose;
import play.db.jpa.Model;

import play.modules.elasticsearch.Query;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//@ElasticSearchable
@ElasticSearchable
@Entity(name = "Notes")
public class Note extends Model {
    @Expose
    public long fakeId; // reference to the models id
    @Expose
    public String title; // title of the note
    @Expose
    public String content; // content will be around 140 charac
    @Expose
    public int startTime; // start time in the video in which will be shown the note
    @ElasticSearchEmbedded(fields={"id"})
    @ManyToOne(fetch=FetchType.EAGER)
    public Video video; // the video in which is the note*/
    @Expose
    @ElasticSearchEmbedded(fields={"name"})
    @ManyToOne
    public User noteWriter; // user who has wrote it
    @Expose
    public long visited; // number of visitors who have visited this note
    @Expose

	@ElasticSearchEmbedded (fields={"tagTitle"})
    @ManyToMany(fetch=FetchType.EAGER)
    public List<Tag> tags;// tags of the note

    public Note(String title, String content) {
        super();
        this.title = title;
        this.content = content;
    }

    public Note(String title, String content, int startTime, Video video, User user) {
        super();
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.video = video;
        
        this.noteWriter = user;
        this.visited = 0;
        this.tags = new ArrayList<Tag>();
    }

    public static List<Note> findAllByVideoId(Video video) {
            return find("video = ? order by startTime desc", video).fetch();
    }
    public int compareTo(Note note) {
   	 
		int compareStart = ((Note) note).startTime; 
		//ascending order
		return this.startTime - compareStart;
	}
    public static List<Note> searchNotes(String query, long id)
    {
    	Query<Note> queryNote=  play.modules.elasticsearch.ElasticSearch.query(QueryBuilders.boolQuery().must(QueryBuilders.fieldQuery("video.id", id)).must(QueryBuilders.queryString(query)), Note.class);
    	queryNote.hydrate(true);
    	return queryNote.fetch().objects;
    }
 //   public static 
    
}
 
