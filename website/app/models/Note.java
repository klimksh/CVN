package models;
import models.elasticsearch.*;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;
import play.modules.elasticsearch.Query;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;
import scala.Array;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.List;
@ElasticSearchable
@Entity(name = "Notes")
public class Note extends Model {
	
    @Expose
    public String noteTitle; // title of the note
    @Expose
    public String content; // content will be around 140 charac
    @Expose
    public int startTime; // start time in the video in which will be shown the note
   @ElasticSearchEmbedded(fields={"id"})
    @ManyToOne(fetch=FetchType.EAGER)
    public Video videoNote; // the video in which is the note
    @Expose
    @ElasticSearchEmbedded(fields={"name"})
    @ManyToOne
    public User noteWriter; // user who has wrote it
    @Expose
    public long visited; // number of visitors who have visited this note
    @Expose
    @ElasticSearchEmbedded (fields={"tagTitle"})
    @ManyToMany (fetch=FetchType.EAGER)
    public List<Tag> noteTags;// tags of the note
    public long parentVideoId;
    public Note(String title, String content, int startTime, /*int endTime,*/ Video video, User user, ArrayList<Tag> tags) {
        super();
        this.noteTitle = title;
        this.content = content;
        this.startTime = startTime;
        /*this.endTime = endTime;*/
        this.videoNote = video;
        this.parentVideoId=video.id;
        this.noteWriter = user;
        this.visited = 0;
        this.noteTags = tags;
        create();

    }

    public Note(String title, String content, int startTime, /*int endTime,*/ Video video, User user, ArrayList<Tag> tags, long visited) {
        super();
        this.noteTitle = title;
        this.content = content;
        this.startTime = startTime;
        /*this.endTime = endTime;*/
        this.videoNote = video;
        this.noteWriter = user;
        this.visited = visited;
        this.noteTags = tags;
        this.parentVideoId=video.id;
        create();
    }

    public static List<Note> findAllByVideoId(Video video) {
        return find("video = ? order by startTime desc", video).fetch();
    }
    public static List<Note> searchNotes(String query, long id)
    {
    	Query<Note> queryNote=  play.modules.elasticsearch.ElasticSearch.query(QueryBuilders.boolQuery().must(QueryBuilders.fieldQuery("parentVideoId", id)).must(QueryBuilders.queryString(query)), Note.class);
    	queryNote.hydrate(true);
    	return queryNote.fetch().objects;
    }
}
 
