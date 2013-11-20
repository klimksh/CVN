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
	public Tag()
	{
		notes= new ArrayList<Note>();
		videos= new ArrayList<Video>();
		create();
	}
	public Tag(String title)
	{
		this.title=title;
		create();
	}
	public static Tag findBygoogleID(String title) {
	    return find("title", title).first();
	}

}
