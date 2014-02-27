package models;

import com.google.gson.annotations.Expose;
import play.data.validation.Email;
import play.data.validation.Unique;
import play.db.jpa.Model;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@ElasticSearchable

@Entity(name = "Users")
public class User extends Model {
	@Expose
	@Email
	@Unique
	public String email;
	@Expose
	public String name;
	public String googleUserId;
	@ElasticSearchIgnore
	@ManyToMany
	public List<Video> watchedVideos;

	public User(String email, String name, String googleUserId) {
		super();
		this.email = email;
		this.name = name;
		this.googleUserId = googleUserId;
		this.watchedVideos = new ArrayList<Video>();
	}

	public static User findByEmail(String email) {
		return find("email", email).first();
	}

	public static User findByGoogleID(String googleUserId) {
		return find("googleUserId", googleUserId).first();
	}


	public String getEmail() {
		return this.email;
	}

	public User() {
	}

	public void inserUser(String email, String name,
			String googleUserId) {
		new User(email, name, googleUserId);
	}

}
