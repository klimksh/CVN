package models;

import org.elasticsearch.index.query.QueryBuilders;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;
import play.modules.elasticsearch.search.SearchResults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@ElasticSearchable
@Entity(name = "Videos")
public class Video extends Model {
	@Required
	public String title;

	@Lob
	public String description;

	@Required
	public String url;
	@ElasticSearchIgnore
	public Date uploadDate;
	@ElasticSearchEmbedded(fields={"title"})
	@ManyToMany
	public List<Tag> tags;
	@ElasticSearchIgnore
	@ManyToOne
	public User owner;

	@ManyToMany(mappedBy = "watchedVideos")
	List<User> whatchers;
	// @ElasticSearchEmbedded(fields={"title","content"})
	@OneToMany(mappedBy = "video")
	List<Note> notes;

	public Video(String title, String description, String url, Date uploadDate,
			ArrayList<Tag> tags, User owner) {
		super();
		this.title = title;
		this.description = description;
		this.url = url;
		this.uploadDate = uploadDate;
		this.tags = tags;
		this.owner = owner;
	}

	public static List<Video> searchQuery(String query) {
		SearchResults<Video> list = play.modules.elasticsearch.ElasticSearch
				.search(QueryBuilders.queryString(query), Video.class);
		return list.objects;
	}
}
