package models;

import org.elasticsearch.index.query.QueryBuilders;
import play.data.validation.Required;
import play.db.jpa.Model;

import play.modules.elasticsearch.Query;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded.Mode;
import play.modules.elasticsearch.annotations.ElasticSearchField;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.search.SearchResults;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.elasticsearch.index.query.FieldQueryBuilder;
import org.elasticsearch.index.query.HasChildQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//@ElasticSearchable
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
    
    @ElasticSearchEmbedded (fields={"tagTitle"})
    @ManyToMany 
    public List<Tag> tags; 
    
    @ElasticSearchEmbedded
    @ManyToOne
    public User owner;
    
    @ElasticSearchIgnore
    @ManyToMany
    List<User> whatchers;

    @ElasticSearchEmbedded (mode=Mode.embedded) 
    @OneToMany// (mappedBy="videoNote", fetch=FetchType.EAGER)
    public List<Note>notes;

    public Video(String title, String description, String url, Date uploadDate, ArrayList<Tag> tags, User owner) {
        super();
        this.title = title;
        this.description = description;
        this.url = url;
        this.uploadDate = uploadDate;
        this.tags = tags;
        this.owner = owner;
    }

    public Video(String title, String description, String url, ArrayList<Tag> tags, String userEmail) {
        super();
        this.uploadDate = new Date();
        this.owner = User.findByEmail(userEmail);
        this.title = title;
        this.description = description;
        this.url = url;
        this.tags = tags;
    }

    public Video(String title, String description, String url, ArrayList<Tag> tags, long id) {
        super();
        this.uploadDate = new Date();
        this.owner = User.findById(id);
        this.title = title;
        this.description = description;
        this.url = url;
        this.tags = tags;
    }
    

    public Video(String title, String description, String url, Date uploadDate,
		List<Tag> tags, User owner, List<Note> notes) {
		super();
		this.title = title;
		this.description = description;
		this.url = url;
		this.uploadDate = uploadDate;
		this.tags = tags;
		this.owner = owner;
		this.notes = notes;
	}

	public void insertVideo(String title, String description, String url, ArrayList<Tag> tags, String userEmail) {
    	  new Video(title, description, url, tags, userEmail);
    }

    public void insertVideo(String title, String description, String url, ArrayList<Tag> tags, long id) {
    	  new Video(title, description, url, tags, id);
    }
    public static List<Video> searchQuery(String query)
    {
    	Query<Video> queryVideo=  play.modules.elasticsearch.ElasticSearch.query(QueryBuilders.queryString(query), Video.class);
    	queryVideo.hydrate(true);
    	QueryBuilders.boolQuery().should(QueryBuilders.queryString(query)).should(QueryBuilders.queryString(query));
    
    	for(int i=0;i<queryVideo.fetch().objects.size();i++)
    	{
            //video
    		System.out.println(queryVideo.fetch().objects.get(i).id+" "+queryVideo.fetch().objects.get(i).title +" "+queryVideo.fetch().objects.get(i).notes.size() );
    		//notes of the video that contains the viedo
    		List<Note> notes = Note.searchNotes(query, queryVideo.fetch().objects.get(i).id);
    		
    	}
    	return queryVideo.fetch().objects; //list.objects;
    }
}
