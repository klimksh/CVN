package models;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@ElasticSearchable
@Entity(name = "Tags")
public class Tag extends Model {

	@Expose
	public String title;

	public static Tag findByTitle(String title) {
		return find("title", title).first();
	}
	public Tag(String title)
	{
		this.title=title;
	}
}
