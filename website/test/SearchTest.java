import java.util.List;

import models.Note;
import models.Video;

import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.test.Fixtures;
import play.test.UnitTest;

public class SearchTest extends UnitTest {

//	@Before
//	public void dataLoading() {
//	}

	@Test
	public void searchVideoQuery() {
		int a = Video.searchQuery("Test video").size();
		assertEquals(true, a > 0);
	}

	@Test
	public void testSearchOnlyNotes() {
		int a = Note.searchOnlyNotes("test").size();
		assertEquals(1, a);

	}

	@Test
	public void testSearchNotesInAVideo() {
		int a = Note.searchNotes("test", 1).size();
		assertEquals(1, a);

	}

	@Test
	public void testWithNoNotes() {
		int a = Video.searchQuery("test").size();
		assertEquals(0, a);

	}

	@Test
	public void testmyVideos() {
		int a = Video.myVideos().size();
		assertEquals(0, a);
	}

	@Test
	public void testmyNotes() {
		int a = Note.myNotes().size();
		assertEquals(0, a);
	}

}
