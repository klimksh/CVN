package models;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;
import play.modules.elasticsearch.Query;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;
import play.mvc.Scope.Session;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.List;

@ElasticSearchable
@Entity(name = "Notes")
public class Note extends Model {
	@Expose
	public long fakeId; // reference to the models id
	@Expose
	public String title;
	@Expose
	public String content;
	@Expose
	public int startTime;
	@ElasticSearchEmbedded(fields={"id"})
	@ManyToOne (fetch = FetchType.EAGER)
	public Video video;
	@Expose
	@ElasticSearchEmbedded(fields = { "name" })
	@ManyToOne
	public User noteWriter;
	@Expose
	public long visited;
	@Expose
	@ElasticSearchEmbedded(fields = { "title" })
	@ManyToMany (fetch=FetchType.EAGER)
	public List<Tag> tags;

	public Note(String title, String content, int startTime, Video video,
			User user) {
		super();
		this.title = title;
		this.content = content;
		this.startTime = startTime;
		this.video = video;
		this.noteWriter = user;
		this.visited = 0;
		this.tags = new ArrayList<Tag>();
	}

	public int compareTo(Note note) {
		int compareStart = ((Note) note).startTime;
		// ascending order
		return this.startTime - compareStart;
	}
     //notes  of a video with id=id and containds string query
	public static List<Note> searchNotes(String query, long id) {
		Query<Note> queryNote = play.modules.elasticsearch.ElasticSearch.query(
				QueryBuilders.boolQuery()
						.must(QueryBuilders.fieldQuery("video.id", id))
						.must(QueryBuilders.queryString(query)), Note.class);
		queryNote.hydrate(true);
		return queryNote.fetch().objects;
	}
	public static List<Note> searchOnlyNotes(String query) {
		Query<Note> queryNote = play.modules.elasticsearch.ElasticSearch.query(
				QueryBuilders.boolQuery()
						.must(QueryBuilders.queryString(query)), Note.class);
		queryNote.hydrate(true);
		return queryNote.fetch().objects;
	}
	public static List<Note> myNotes()
	{
		String a= Session.current().get("googleUserId").toString();
		User usr=User.findByGoogleID(a);
		 return find("noteWriter", usr).fetch();
	}
	
}
