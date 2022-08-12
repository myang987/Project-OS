package test.io;

import static org.junit.Assert.assertEquals;

import driver.Controller;
import exceptions.DuplicateException;
import exceptions.IllegalNameException;
import io.Directory;
import io.File;
import io.FileEditor;
import io.FileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.MockController;
import util.PathInterpreter;

public class FileManagerTest {

  private Directory actual;
  private Controller system;
  private PathInterpreter pathInterpreter;
  private final FileEditor fileEditor = new FileEditor();
  private final FileManager fileManager = new FileManager(fileEditor);

  @Before
  public void setup() {
    actual = new Directory("test", fileManager);
    system = new MockController();
    pathInterpreter = new PathInterpreter(system);
  }

  @Test
  public void testCreateValidDirectory()
      throws DuplicateException, IllegalNameException {
    Directory expected = new Directory("test", fileManager);
    expected.insertElement(new Directory("Valid_Name", actual, fileManager));
    actual.createDirectory("Valid_Name");
    assertEquals(expected, actual);
  }

  @Test
  public void testCreateValidFile()
      throws DuplicateException, IllegalNameException {
    Directory expected = new Directory("test", fileManager);
    expected
        .insertElement(new File("Valid_File", actual, fileManager, fileEditor));
    actual.createFile("Valid_File");
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalNameException.class)
  public void testCreateInvalidDirectory()
      throws DuplicateException, IllegalNameException {
    actual.createDirectory("!!!");
  }

  @Test(expected = IllegalNameException.class)
  public void testCreateInvalidFile()
      throws DuplicateException, IllegalNameException {
    actual.createFile("???");
  }

  @Test
  public void testInsertValid()
      throws DuplicateException, IllegalNameException {
    File validFile = new File("valid_file", actual, fileManager, fileEditor);
    actual.insertElement(validFile);
    Directory expected = new Directory("test", fileManager);
    expected.createFile("valid_file");
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalNameException.class)
  public void testInsertInvalid()
      throws DuplicateException, IllegalNameException {
    File invalid = new File("XAXAXAXAXAXA!!!", actual, fileManager, fileEditor);
    actual.insertElement(invalid);
  }

  /**
   * Test copy method returns an equivalent object.
   */
  @Test
  public void testCopy() throws DuplicateException, IllegalNameException {
    actual.createFile("file1");
    Directory sub = actual.createDirectory("dir1");
    sub.createFile("subFile");
    Directory expected = actual;
    Directory actual = this.actual.copy();
    assertEquals(expected, actual);
    assertEquals(actual, expected);
  }

  /**
   * Test clear method leaves a directory that is equivalent to newly created
   * ones. No recursion.
   */
  @Test
  public void testClear() throws DuplicateException, IllegalNameException {
    actual.createDirectory("dir1");
    actual.createFile("file1");
    actual.removeAll();
    Directory expected = new Directory("test", fileManager);
    assertEquals(actual, expected);
  }

  /**
   * Test clear method can recursively clear all sub directories.
   */
  @Test
  public void testClearRecur() throws DuplicateException, IllegalNameException {
    actual.createFile("file_1");
    Directory sub1 = actual.createDirectory("sub1");
    sub1.createFile("file1_1");
    Directory sub2 = sub1.createDirectory("sub2");
    sub2.createFile("file12_1");
    actual.removeAll();
    assertEquals(new Directory("sub2", fileManager), sub2);
    assertEquals(new Directory("sub1", fileManager), sub1);
    assertEquals(new Directory("test", fileManager), actual);
  }

  @After
  public void tearDown() {
    actual = null;
  }
}
