package test.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import exceptions.DuplicateException;
import exceptions.IllegalNameException;
import io.Directory;
import io.File;
import io.FileEditor;
import io.FileManager;
import org.junit.Before;
import org.junit.Test;

public class FileTest {

  Directory parent;
  File self;
  FileEditor fileEditor;
  FileManager fileManager;

  @Before
  public void setup() {
    fileEditor = new FileEditor();
    fileManager = new FileManager(fileEditor);
    parent = new Directory("parent", fileManager);
    self = new File("test", parent, fileManager, fileEditor);
  }

  /**
   * Test the newly constructed file has intended contents.
   */
  @Test
  public void testConstruct() {
    String actual = self.getContents();
    assertEquals("", actual);
  }

  /**
   * Test equals - same name and contents.
   */
  @Test
  public void testEquals1() {
    self.appendToContents("1234");
    File other = new File("test", parent, fileManager, fileEditor);
    other.appendToContents("1234");
    assertEquals(self, other);
    assertEquals(other, self);
  }

  /**
   * Test equals - same name and contents, different parentDir.
   */
  @Test
  public void testEquals2() {
    File other = new File("test", new Directory("not", fileManager),
        fileManager, fileEditor);
    assertEquals(self, other);
    assertEquals(other, self);
  }

  /**
   * Test not equals - same contents, different name.
   */
  @Test
  public void testNotEquals1() throws DuplicateException, IllegalNameException {
    File other = parent.createFile("not");
    assertNotEquals(self, other);
    assertNotEquals(other, self);
  }

  /**
   * Test not equals - same name, different contents.
   */
  @Test
  public void testNotEquals2() throws DuplicateException, IllegalNameException {
    File other = parent.createFile("test");
    other.overwriteContentsAs("1234");
    assertNotEquals(self, other);
    assertNotEquals(other, self);
  }

  /**
   * Test overwriteContentsAs()
   */
  @Test
  public void testOverwrite() {
    self.overwriteContentsAs("12345");
    assertEquals("12345", self.getContents());
    self.overwriteContentsAs("apple banana");
    assertEquals("apple banana", self.getContents());
  }

  /**
   * Test appendToContents()
   */
  @Test
  public void testAppend() {
    self.appendToContents("123");
    assertEquals("123", self.getContents());
    self.appendToContents("apple");
    assertEquals("123apple", self.getContents());
  }

  /**
   * Test copy.
   */
  @Test
  public void testCopy() {
    self.overwriteContentsAs("12345");
    File copied = self.copy();
    assertEquals(self, copied);
    assertEquals(copied, self);
  }

}
