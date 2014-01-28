package models;

import com.google.gson.annotations.Expose;
import play.data.validation.Email;
import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
@Entity(name="Users")
public class User extends Model{
    @Expose
    @Email
	@Unique
	public String email;
	public String password;
    @Expose
	public String name;
	public String googleUserId;
	@ManyToMany
	public List<Video>watchedVideos;

	public User(@Email @Unique String email, String password, String name) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.googleUserId = "";
		this.watchedVideos = new ArrayList<Video>();
		create();
	}

	public void insertUser(String email, String password, String name)
	{
		new User(email, password, name);
	}

    public User(String email, String password, String name, String googleUserId) {
        super();
        this.email = email;
        this.password = password;
        this.name = name;
        this.googleUserId = googleUserId;
        this.watchedVideos = new ArrayList<Video>();
        create();
    }

    public static User findByEmail(String email) {
        return find("email", email).first();
    }

    public static User findByGoogleID(String googleUserId) {
        return find("googleUserId", googleUserId).first();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail()
    {
    	return this.email;
    }

    public void inserUser(String email, String password, String name, String googleUserId)
    {
    	new User(email, password, name, googleUserId);
    	
    }
   
}
