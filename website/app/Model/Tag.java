package Model;

import java.util.ArrayList;

import javax.persistence.ManyToMany;

import play.db.jpa.Model;

public class Tag extends Model{
	
	// because of many to many relationship
	@ManyToMany (mappedBy="noteTags")
	public ArrayList<Note>notes;
	@ManyToMany (mappedBy="videoTags")
	public ArrayList<Video>videos;
	public String title;

	public Tag(String title)
	{
        notes = new ArrayList<Note>();
        videos = new ArrayList<Video>();
		this.title = title;
		create();
	}

    public static Tag findByGoogleID(String title) {
	    return find("title", title).first();
	}

}
