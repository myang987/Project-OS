package test.commands;

import static org.junit.Assert.assertEquals;

import commands.CommRm;
import driver.Controller;
import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.JShellException;
import io.Directory;
import io.FolderElement;
import io.OutputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.MockController;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;

public class CommRmTest {

  private Controller system;
  private OutputBuilder builder;
  private CommRm commRm;

  /**
   * root: [sub1: [subFile1, sub2: [subFile2, sub3: [subFile3]]], file1]
   */
  @Before
  public void setUp() throws DuplicateException, IllegalNameException {
    system = new MockController();
    builder = new OutputBuilder(new OutputHandler(new PathInterpreter(system)));
    commRm = new MockCommRm(system);
    Directory root = system.getRootDir();
    Directory sub1 = root.createDirectory("sub1");
    Directory sub3 = root.createDirectory("sub3");
    Directory sub2 = sub1.createDirectory("sub2");
    root.createFile("file1");
    sub1.createFile("subFile1");
    sub3.createFile("subFile3");
    sub2.createFile("subFile2");
  }


  /**
   * Test rm can remove the input dir from directory.
   */
  @Test
  public void testRemove()
      throws JShellException {
    Message args = new Message(new String[]{"sub1"});
    Directory expected = system.getRootDir().copy();
    expected.removeElement("sub1");
    commRm.execute(args, builder);
    Directory actual = system.getRootDir().copy();
    assertEquals(expected, actual);
  }

  /**
   * Test rm can remove the input dir from directory recursively.
   */
  @Test(expected = FileNotExistException.class)
  public void testRemoveRecursively()
      throws JShellException {
    Message args = new Message(new String[]{"sub1"});
    Path path = new Path("/sub1/subFile1");
    commRm.execute(args, builder);
    FolderElement actual =
        system.getPathInterpreter().toFolderElement(path);
    assertEquals(actual, null);
  }

  /**
   * Test rm does not affect folder element other than input DIR.
   */
  @Test
  public void testRemoveUntouched()
      throws JShellException {
    Message args = new Message(new String[]{"sub1"});
    Path path = new Path("/sub3");
    FolderElement expected =
        system.getPathInterpreter().toFolderElement(path);
    commRm.execute(args, builder);
    FolderElement actual =
        system.getPathInterpreter().toFolderElement(path);
    assertEquals(expected, actual);
  }


  @After
  public void tearDown() {
    ((MockController) system).clear();
  }
}