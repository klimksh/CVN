import java.util.Calendar;
import java.util.Date;

import models.Note;
import models.Tag;
import models.User;
import models.Video;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;


public class VideoTest extends UnitTest {
	String title;
	String link;
	String desc;
	String userEmail;
	Tag tag;
	Note note;
	User user;
	Date currentDate;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp(){
		title = "testTitle";
		desc = "videoDescription";

		link = "videoId";

		userEmail = "email@gmail.com";

		user = User.findByEmail(userEmail);
		if (user == null) {
			new User(userEmail,"userName", "userId").save();
		}
		
		tag = new Tag("test");
		tag.save();

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		currentDate = new Date(year, month, day);
	} 
	
	@Test
    public void saveNoteToVideoTest() {

		
		Video video = new Video(title, desc, link, currentDate, user);
		System.out.println(video.title);
		
//		video.tags.add(tag);
		
		video.save();
//		assertEquals("test", video.tags.get(0).title);
		note = new Note("TestNoteTitle", "NoteContent", 187, video, user);
		note.save();
		video.notes.add(note);
		assertEquals("TestNoteTitle", video.notes.get(0).title);
	}

	@Test
    public void saveVideoTest() {

		Video video = new Video(title, desc, link, currentDate, user);
//		video.tags.add(tag);
		video.save();
//		assertEquals("test", video.tags.get(0).title);
		note = new Note("TestNoteTitle", "NoteContent", 187, video, user);
		video.notes.add(note);
	}

	
}
