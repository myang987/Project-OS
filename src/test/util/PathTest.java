package test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import org.junit.Test;
import util.Path;

public class PathTest {

  /**
   * Test if constructors can construct a single character path.
   */
  @Test
  public void testConstructSingleChar1() {
    Path actual = new Path("c");
    assertEquals("c/", actual.getText());
    assertFalse(actual.isAbsolute());
  }

  /**
   * Test if constructors can construct a single character path.
   */
  @Test
  public void testConstructSingleChar2() {
    Path actual = new Path(".");
    assertEquals("/", actual.getText());
    assertFalse(actual.isAbsolute());
  }

  /**
   * Test if constructors can construct a correct path to root directory.
   */
  @Test
  public void testConstructRoot1() {
    Path path = new Path("/");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "/";
    assertEquals(textExpected, textActual);
    assertTrue(isAbsoluteActual);
  }

  /**
   * Test if constructors can construct a correct path to root directory, with
   * unnecessary characters.
   */
  @Test
  public void testConstructRoot2() {
    Path path = new Path("///////////");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "/";
    assertEquals(textExpected, textActual);
    assertTrue(isAbsoluteActual);
  }

  /**
   * Test if constructors can construct a correct absolute path.
   */
  @Test
  public void testConstructAbsolute1() {
    Path path = new Path("/sub1/sub2/file2/");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "sub1/sub2/file2/";
    assertEquals(textExpected, textActual);
    assertTrue(isAbsoluteActual);
  }

  /**
   * Test if constructors can construct a correct absolute path, with
   * unnecessary characters.
   */
  @Test
  public void testConstructAbsolute2() {
    Path path = new Path("/////sub1//./././..//sub2/file2////////");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "sub2/file2/";
    assertEquals(textExpected, textActual);
    assertTrue(isAbsoluteActual);
  }

  /**
   * Test if constructors can handle odd separator path construction.
   */
  @Test
  public void testConstructAbsolute3() {
    Path path = new Path("/~/sub1/~/sub2/~/../~//~/file2/~//~/", "/~/");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "sub1/~/file2/~/";
    assertEquals(textExpected, textActual);
    assertTrue(isAbsoluteActual);
  }

  /**
   * Test if constructors can construct a correct relative path.
   */
  @Test
  public void testConstructRelative1() {
    Path path = new Path("sub1/sub2/file2/");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "sub1/sub2/file2/";
    assertEquals(textExpected, textActual);
    assertFalse(isAbsoluteActual);
  }

  /**
   * Test if constructors can construct a correct relative path, with
   * unnecessary characters.
   */
  @Test
  public void testConstructRelative2() {
    Path path = new Path("sub1//.//sub2/../../file2//");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "file2/";
    assertEquals(textExpected, textActual);
    assertFalse(isAbsoluteActual);
  }

  /**
   * Test if constructors can handle odd separator path construction.
   */
  @Test
  public void testConstructRelative3() {
    Path path = new Path("sub1~~..~~.~~sub2~~~~file2~~~~", "~~");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "sub2~~file2~~";
    assertEquals(textExpected, textActual);
    assertFalse(isAbsoluteActual);
  }

  /**
   * Test if constructors can handle excessive ".."
   */
  @Test
  public void testConstructRelative4() {
    Path path = new Path("../../file0/../file1/../../file2/./");
    String textActual = path.getText();
    boolean isAbsoluteActual = path.isAbsolute();
    String textExpected = "../../../file2/";
    assertEquals(textExpected, textActual);
    assertFalse(isAbsoluteActual);
  }

  /**
   * Test iterator can correctly identify a path with no readable section
   */
  @Test
  public void testIterator1() {
    Path path = new Path(".");
    Iterator<String> itr = path.iterator();
    assertFalse(itr.hasNext());
  }

  /**
   * Test iterator can correctly iterate a single section path.
   */
  @Test
  public void testIterator2() {
    Path path = new Path("/file1/");
    Iterator<String> itr = path.iterator();
    assertEquals("file1", itr.next());
  }

  /**
   * Test iterator can correctly iterate a long path.
   */
  @Test
  public void testIterator3() {
    Path path = new Path("/file1/file2/file3/file4");
    String[] actual = new String[4];
    int i = 0;
    for (String str : path) {
      actual[i] = str;
      i++;
    }
    String[] expected = new String[]{"file1", "file2", "file3", "file4"};
    assertArrayEquals(expected, actual);
  }

  /**
   * Test iterator can correctly iterate a path with excessive ".."
   */
  @Test
  public void testIterator4() {
    Path path = new Path("/../../../file4");
    String[] actual = new String[4];
    int i = 0;
    for (String str : path) {
      actual[i] = str;
      i++;
    }
    String[] expected = new String[]{"..", "..", "..", "file4"};
    assertArrayEquals(expected, actual);
  }

}
