package models;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@ElasticSearchable
@Entity(name = "Tags")
public class Tag extends Model {

	// because of many to many relationship
	@ElasticSearchIgnore
	@ManyToMany(mappedBy = "tags")
	public List<Note> notes;
	@ElasticSearchIgnore
	@ManyToMany(mappedBy = "tags")
	public List<Video> videos;
	@Expose
	public String title;

	public Tag() {
		super();
		this.notes = new ArrayList<Note>();
		this.videos = new ArrayList<Video>();
		this.title = "";
	}

	public Tag(String title, Note note, Video video) {
		super();
		if (this.notes == null) {
			this.notes = new ArrayList<Note>();
		}
		this.notes.add(note);
		if (this.videos == null) {
			this.videos = new ArrayList<Video>();
		}
		this.videos.add(video);
		this.title = title;
	}

	public static Tag findByTitle(String title) {
		return find("title", title).first();
	}
	public Tag(String title)
	{
		this.title=title;
		
	}
}
