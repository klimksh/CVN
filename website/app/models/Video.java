package models;

import org.elasticsearch.index.query.QueryBuilders;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;
import play.modules.elasticsearch.search.SearchResults;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded.Mode;
import play.mvc.Scope.Session;

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
	@ManyToMany //(fetch=FetchType.EAGER)
	public List<Tag> tags;
	@ElasticSearchIgnore
	@ManyToOne
	public User owner;
	@ElasticSearchIgnore
	@ManyToMany(mappedBy = "watchedVideos")
	List<User> whatchers;
	@ElasticSearchEmbedded (mode=Mode.embedded) 
	@OneToMany //(mappedBy = "video")
//	@OneToMany 
	public List<Note> notes;

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
		myVideos();
		SearchResults<Video> list = play.modules.elasticsearch.ElasticSearch
				.searchAndHydrate(QueryBuilders.queryString(query), Video.class);
		return list.objects;
	}
	
	public static List<Video> searchQuery1(String query) {
		
		//s
		SearchResults<Video> list = play.modules.elasticsearch.ElasticSearch
				.search(QueryBuilders.queryString(query), Video.class);
		return list.objects;
	}
	public static List<Video>myVideos()
	{
		String a= Session.current().get("googleUserId").toString();
		User usr=User.findByGoogleID(a);
		 return find("owner", usr).fetch();
		
	}
	
}
