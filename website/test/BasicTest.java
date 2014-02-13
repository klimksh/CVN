import models.Note;
import models.Tag;
import models.User;
import models.Video;

import org.junit.*;

import controllers.NoteController;

import java.util.*;
import play.test.*;

public class BasicTest extends UnitTest {

	@Test
	public void aVeryImportantThingToTest() {
		assertEquals(2, 1 + 1);
	}

	@Test
	public void tesstInsertNote() {
		int number = Note.findAll().size();
		// Note p= new Note("testNote", "testNote content", 1,
		// Video.findById(1L), User.findById(1L));
		NoteController.saveNote();
		assertEquals(number + 1, Note.findAll().size());
		

	}

	@Test
	public void testIndexing() {
		int a = Video.searchQuery("tittle test2").size();
		assertEquals(true, a > 0);

	}

	@Test
	public void insertTag() {
		Tag a = new Tag("test");
		a.save();
	}

}
