package Model;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;
import play.db.jpa.Model;
@Entity(name="Users")
public class User extends Model{
	public String email;
	public String password;
	public String name;
	public String googleUserId;
	@ManyToMany
	public List<Video>watchedVideos;

	public User(String email, String password, String name) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.googleUserId = "";
		this.watchedVideos = new ArrayList<Video>();
		create();
	}

	public User(String email, String password, String name, String googleUserId)
	 {
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

}
