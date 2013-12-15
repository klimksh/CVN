import models.User;
import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {
	public void doJob()
	{
		 if(User.count()== 0)
		 {
			 Fixtures.loadModels("data-users.yml");
			 Logger.info("Users are loaded");
			 Fixtures.loadModels("data-tags.yml");
			 Logger.info("Tags are loaded");
			 Fixtures.loadModels("data-notes.yml");
			 Logger.info("Notes are loaded");
			 Fixtures.loadModels("data-videos.yml");
	         Logger.info("Videos are loaded");
		 }
	}

}
