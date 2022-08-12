package test.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import exceptions.DuplicateException;
import exceptions.IllegalNameException;
import io.Directory;
import io.File;
import io.FileEditor;
import io.FileManager;
import io.FolderElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DirectoryTest {

  private Directory self;
  private final FileEditor fileEditor = new FileEditor();
  private final FileManager fileManager = new FileManager(fileEditor);

  @Before
  public void setup() {
    self = new Directory("test", fileManager);
  }

  /**
   * Test equals for having the same files.
   */
  @Test
  public void testEquals1() throws DuplicateException, IllegalNameException {
    self.createFile("Test");
    Directory that = new Directory("test", fileManager);
    that.createFile("Test");
    assertEquals(self, that);
    assertEquals(that, self);
  }

  /**
   * Test equals for having same sub files, but in different orders.
   */
  @Test
  public void testEquals2() throws DuplicateException, IllegalNameException {
    self.createDirectory("Test");
    self.createFile("test2");
    self.createFile("FFF3");
    Directory that = new Directory("test", fileManager);
    that.createFile("test2");
    that.createFile("FFF3");
    that.createDirectory("Test");
    assertEquals(self, that);
    assertEquals(that, self);
  }

  /**
   * Test equals when involving sub directory contents.
   */
  @Test
  public void testEquals3() throws DuplicateException, IllegalNameException {
    self.createFile("test2");
    self.createFile("FFF3");
    Directory selfSub = self.createDirectory("Sub");
    selfSub.createFile("deepFile");
    selfSub.createDirectory("deepDir");
    Directory that = new Directory("test", fileManager);
    that.createFile("test2");
    that.createFile("FFF3");
    Directory thatSub = that.createDirectory("Sub");
    thatSub.createFile("deepFile");
    thatSub.createDirectory("deepDir");
    assertEquals(self, that);
    assertEquals(that, self);
  }

  /**
   * Test not equals when some sub elements have different names.
   */
  @Test
  public void testNotEquals1() throws DuplicateException, IllegalNameException {
    self.createFile("Test");
    Directory that = new Directory("test", fileManager);
    that.createFile("NotTest");
    assertNotEquals(self, that);
    assertNotEquals(that, self);
  }

  /**
   * Test not equals when some sub elements of the same name have different
   * types.
   */
  @Test
  public void testNotEquals2() throws DuplicateException, IllegalNameException {
    self.createFile("Test");
    Directory that = new Directory("test", fileManager);
    that.createDirectory("Test");
    assertNotEquals(self, that);
    assertNotEquals(that, self);
  }

  /**
   * Test not equals when two directories have different names.
   */
  @Test
  public void testNotEquals3() throws DuplicateException, IllegalNameException {
    self.createFile("Test");
    Directory that = new Directory("notTest", fileManager);
    that.createFile("Test");
    assertNotEquals(self, that);
    assertNotEquals(that, self);
  }

  /**
   * Test not equals when some files have different contents.
   */
  @Test
  public void testNotEquals4() throws DuplicateException, IllegalNameException {
    File f1 = self.createFile("File");
    f1.overwriteContentsAs("Test text 1");
    Directory that = new Directory("test", fileManager);
    File f2 = that.createFile("File");
    f2.overwriteContentsAs("Different text");
    assertNotEquals(self, that);
    assertNotEquals(that, self);
  }

  /**
   * Test not equals when either of the directory is a subset of another.
   */
  @Test
  public void testNotEquals5() throws DuplicateException, IllegalNameException {
    self.createDirectory("Test");
    self.createFile("test2");
    self.createFile("FFF3");
    Directory that = new Directory("test", fileManager);
    that.createFile("test2");
    that.createFile("FFF3");
    that.createDirectory("Test");
    that.createFile("Excess");
    assertNotEquals(self, that);
    assertNotEquals(that, self);
  }

  /**
   * Test not equals when involving sub directory differences.
   */
  @Test
  public void testNotEquals6() throws DuplicateException, IllegalNameException {
    self.createFile("test2");
    self.createFile("FFF3");
    Directory selfSub = self.createDirectory("Sub");
    selfSub.createFile("deepFile");
    selfSub.createDirectory("deepDir");
    Directory that = new Directory("test", fileManager);
    that.createFile("test2");
    that.createFile("FFF3");
    Directory thatSub = that.createDirectory("Sub");
    thatSub.createFile("deepFile");
    thatSub.createDirectory("deepDir");
    // That has one more file in Sub
    thatSub.createFile("Excess");
    assertNotEquals(self, that);
    assertNotEquals(that, self);
  }

  @Test
  public void testGetByName() throws DuplicateException, IllegalNameException {
    self.createDirectory("name1");
    self.createDirectory("dir2");
    self.createFile("file3");
    FolderElement actual = self.getElementByName("name1");
    FolderElement expected = new Directory("name1", self, fileManager);
    assertEquals(expected, actual);
  }

  /**
   * Test if removeElement() removes the correct file or directory.
   */
  @Test
  public void testRemove() throws DuplicateException, IllegalNameException {
    self.createDirectory("Test");
    self.createFile("test2");
    self.createFile("FFF3");
    Directory that = new Directory("test", fileManager);
    that.createFile("test2");
    that.createFile("FFF3");
    that.createDirectory("Test");
    that.createFile("ExcessFile");
    that.createDirectory("ExcessDir");

    that.removeElement("ExcessFile");
    that.removeElement("ExcessDir");

    assertEquals(self, that);
    assertEquals(that, self);
  }

  /**
   * Test if isDescendant() returns true when {@code that} is actually an
   * ancestor.
   */
  @Test
  public void testDescendant1()
      throws DuplicateException, IllegalNameException {
    Directory root = new Directory("root", fileManager);
    Directory sub1 = root.createDirectory("sub1");
    Directory sub2 = sub1.createDirectory("sub2");
    assertTrue(sub2.isDescendantOf(sub1));
    assertTrue(sub2.isDescendantOf(root));
    assertTrue(sub1.isDescendantOf(root));
  }

  /**
   * Test if isDescendant() returns false when {@code that} is this, or is an
   * descendant of this.
   */
  @Test
  public void testDescendant2()
      throws DuplicateException, IllegalNameException {
    Directory root = new Directory("root", fileManager);
    Directory sub1 = root.createDirectory("sub1");
    Directory sub2 = sub1.createDirectory("sub2");
    assertFalse(root.isDescendantOf(root));
    assertFalse(sub1.isDescendantOf(sub1));
    assertFalse(root.isDescendantOf(sub1));
    assertFalse(sub1.isDescendantOf(sub2));
  }

  @After
  public void tearDown() {
    self = null;
  }
}
