import java.util.List;

import models.Note;
import models.User;
import models.Video;
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
				List<Note> notes = Note.findAll();
				Video a = Video.findById(1L);
				for (Note no : notes) {
					no.video = a;
					no.save();
				}
				List<Note> notes1 = Note.findAll();
				for(Note no: notes1)
				{
					System.out.println(no.video.id);
				}

		 }
		
	}

}
