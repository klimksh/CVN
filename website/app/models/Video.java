package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.Entity;
import play.db.jpa.Model;
@Entity(name="Videos")
public class Video extends Model{
    public String title;
    public String description;
    public String url;
    public Date uploadDate;
    @ManyToMany
    public List<Tag>videoTags;
    @ManyToOne
    public User owner;
    @ManyToMany(mappedBy="watchedVideos")
    List<User>whatchers;

	public Video(String title, String description, String url, Date uploadDate,
			ArrayList<Tag> videoTags, User owner) {
		super();
		this.title = title;
		this.description = description;
		this.url = url;
		this.uploadDate = uploadDate;
		this.videoTags = videoTags;
		this.owner = owner;
		create();
	}
	public Video(String title, String description, String url,
			ArrayList<Tag> videoTags, String userEmail)
	{
		super();
		this.uploadDate= new Date();
		this.owner = User.findByEmail(userEmail);
		this.title = title;
		this.description = description;
		this.url = url;
		this.videoTags = videoTags;
		create();		
	}
	public Video(String title, String description, String url,
			ArrayList<Tag> videoTags, long id)
	{
		super();
		this.uploadDate= new Date();
		this.owner = User.findById(id);
		this.title = title;
		this.description = description;
		this.url = url;
		this.videoTags = videoTags;
		create();		
	}
	public void insertVideo(String title, String description, String url,
			ArrayList<Tag> videoTags, String userEmail)
	{
		Video tt= new Video(title, description, url, videoTags, userEmail);	
		
	}
	public void insertVideo(String title, String description, String url,
			ArrayList<Tag> videoTags, long id)
	{
		Video tt= new Video(title, description, url, videoTags, id);	
		
	}
    
    
}
