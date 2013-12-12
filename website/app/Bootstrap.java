import models.User;
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
			 Fixtures.loadModels("data-tags.yml");
			 Fixtures.loadModels("data-notes.yml");
			 Fixtures.loadModels("data-videos.yml");
			 
		 }
	}

}
