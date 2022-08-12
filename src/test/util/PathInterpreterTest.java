package test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import driver.Controller;
import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.NotADirectoryException;
import io.Directory;
import io.File;
import io.FolderElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.MockController;
import util.Path;
import util.PathInterpreter;

public class PathInterpreterTest {

  private Controller system;
  private Directory root;
  private Directory sub1;
  private Directory sub2;
  private Directory sub3;
  private File file1;
  private File file2;
  private File file3;
  private PathInterpreter pathInterpreter;

  @Before
  public void setup() throws DuplicateException, IllegalNameException {
    system = new MockController();
    pathInterpreter = new PathInterpreter(system);
    root = system.getRootDir();
    sub1 = root.createDirectory("sub1");
    sub3 = root.createDirectory("sub3");
    sub2 = sub1.createDirectory("sub2");
    root.createFile("file1");
    file1 = sub1.createFile("subFile1");
    file3 = sub3.createFile("subFile3");
    file2 = sub2.createFile("subFile2");
  }

  /**
   * Test if toFolderElement() can locate correct file.
   */
  @Test
  public void testToElement1()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("/../sub1/sub2/../sub2/./subFile2");
    FolderElement actual = pathInterpreter.toFolderElement(path);
    assertEquals(file2, actual);
  }

  /**
   * Test if toFolderElement() can locate correct directory with relative path.
   */
  @Test
  public void testToElement2()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("../../");
    system.setWorkingDir(sub2);
    FolderElement actual = pathInterpreter.toFolderElement(path);
    assertEquals(root, actual);
  }

  /**
   * Test if toFolderElement() correctly returns null when the target file does
   * not exist.
   */
  @Test
  public void testToElement3()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("/../sub1/sub2/../sub2/./subFile2333");
    FolderElement actual = pathInterpreter.toFolderElement(path);
    assertNull(actual);
  }

  /**
   * Test if toFolderElement() throws an exception when the path navigates
   * through a nonexistent directory.
   */
  @Test(expected = FileNotExistException.class)
  public void testToElementNotExist()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("/sub3/subNotExistButCancelled/../subNotExist/fileX");
    pathInterpreter.toFolderElement(path);
  }

  /**
   * Test if toFolderElement() throws an exception when the path navigate
   * through a file.
   */
  @Test(expected = NotADirectoryException.class)
  public void testToElementNotDirectory()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("/sub3/subFile3/fileDNE");
    pathInterpreter.toFolderElement(path);
  }

  /**
   * Test if createDummy() can correctly return the target if it exists.
   */
  @Test
  public void testCreateDummyGrabFile1()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("/../sub1/sub2/../sub2/./subFile2");
    FolderElement actual = pathInterpreter.createDummyAt(path);
    assertEquals(file2, actual);
  }

  /**
   * Test if createDummy() can correctly return the target if it exists, with
   * upward path.
   */
  @Test
  public void testCreateDummyGrabFile2()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("/../../../../../");
    system.setWorkingDir(sub2);
    FolderElement actual = pathInterpreter.createDummyAt(path);
    assertEquals(root, actual);
  }

  /**
   * Test if createDummy() can correctly return a dummy directory that has
   * correct parent directory and name.
   */
  @Test
  public void testCreateDummy1()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("/../sub1/sub2/../sub2/./mockFile");
    FolderElement actual = pathInterpreter.createDummyAt(path);
    assertEquals("mockFile", actual.getName());
    assertEquals(sub2, actual.getParentDir());
  }

  /**
   * Test if createDummy() can correctly return a dummy directory that has
   * correct parent directory and name, with upward path.
   */
  @Test
  public void testCreateDummy2()
      throws NotADirectoryException, FileNotExistException {
    Path path = new Path("/../../../../../mockFile");
    system.setWorkingDir(sub2);
    FolderElement actual = pathInterpreter.createDummyAt(path);
    assertEquals("mockFile", actual.getName());
    assertEquals(root, actual.getParentDir());
  }

  @After
  public void tearDown() {
    ((MockController) system).clear();
  }

}
