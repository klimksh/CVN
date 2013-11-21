package Model;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

public class Video extends Model{
    public String title;
    public String description;
    public String url;
    public Date uploadDate;
    @ManyToMany
    public ArrayList<Tag>videoTags;
    @ManyToOne
    public User owner;
    @ManyToMany(mappedBy="watchedVideo")
    ArrayList<User>Whatchers;

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
    
    
}
