package test.commands;

import static org.junit.Assert.assertEquals;

import commands.CommMv;
import driver.Controller;
import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.IllegalOperationException;
import exceptions.JShellException;
import exceptions.NotADirectoryException;
import exceptions.ResourceBusyException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.File;
import io.OutputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.MockController;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.PathInterpreter;

public class CommMvTest {

  private Controller system;
  private OutputBuilder builder;
  private CommMv commMv;

  @Before
  public void setUp() throws DuplicateException, IllegalNameException {
    system = new MockController();
    builder = new OutputBuilder(new OutputHandler(new PathInterpreter(system)));
    commMv = new MockCommMv(system);
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
   * Test mv can handle in place movement.
   */
  @Test
  public void testInPlace()
      throws IllegalOperationException, DuplicateException,
      WrongSyntaxException, FileNotExistException, IllegalNameException,
      NotADirectoryException, ResourceBusyException {
    Directory expected = system.getRootDir().copy();
    Message args = new Message(new String[]{"/sub1", "/"});
    commMv.execute(args, builder);
    Directory actual = system.getRootDir().copy();
    assertEquals(expected, actual);
  }

  /**
   * Test mv can handle in place movement with rename.
   */
  @Test
  public void testInPlaceRename()
      throws IllegalOperationException, DuplicateException,
      WrongSyntaxException, FileNotExistException, IllegalNameException,
      NotADirectoryException, ResourceBusyException {
    Directory expected = system.getRootDir().copy();
    Directory sub1 = (Directory) expected.getElementByName("sub1").copy();
    expected.removeElement("sub1");
    sub1.renameTo("rename1");
    expected.insertElement(sub1);

    Message args = new Message(new String[]{"/sub1", "/rename1"});
    commMv.execute(args, builder);
    Directory actual = system.getRootDir();
    assertEquals(expected, actual);
  }

  /**
   * Test mv can move file up.
   */
  @Test
  public void testMoveFileUp()
      throws IllegalOperationException, DuplicateException,
      WrongSyntaxException, FileNotExistException, IllegalNameException,
      NotADirectoryException, ResourceBusyException {
    Directory expected = system.getRootDir().copy();
    Directory sub1 = (Directory) expected.getElementByName("sub1");
    File subFile1 = (File) sub1.getElementByName("subFile1").copy();
    sub1.removeElement("subFile1");
    expected.insertElement(subFile1);

    Message args = new Message(new String[]{"/sub1/subFile1/", "/subFile1"});
    commMv.execute(args, builder);
    Directory actual = system.getRootDir();
    assertEquals(expected, actual);
  }

  /**
   * Test mv can move directory up.
   */
  @Test
  public void testMoveDirUp()
      throws DuplicateException, IllegalNameException, WrongSyntaxException,
      NotADirectoryException, IllegalOperationException, FileNotExistException,
      ResourceBusyException {
    Directory expected = system.getRootDir().copy();
    Directory sub1 = (Directory) expected.getElementByName("sub1");
    Directory sub2 = (Directory) sub1.getElementByName("sub2").copy();
    sub1.removeElement("sub2");
    expected.insertElement(sub2);

    Message args = new Message(new String[]{"/sub1/sub2/", "/"});
    commMv.execute(args, builder);
    Directory actual = system.getRootDir();
    assertEquals(expected, actual);
  }

  /**
   * Test mv can move things and rename them.
   */
  @Test
  public void testMoveWithRename()
      throws IllegalNameException, DuplicateException, WrongSyntaxException,
      NotADirectoryException, IllegalOperationException, FileNotExistException,
      ResourceBusyException {
    Directory expected = system.getRootDir().copy();
    Directory sub1 = (Directory) expected.getElementByName("sub1");
    Directory sub2 = (Directory) sub1.getElementByName("sub2").copy();
    sub1.removeElement("sub2");
    sub2.renameTo("renamed2");
    Directory sub3 = (Directory) expected.getElementByName("sub3");
    sub3.insertElement(sub2);

    Message args = new Message(new String[]{"/sub1/sub2/", "/sub3/renamed2"});
    commMv.execute(args, builder);
    Directory actual = system.getRootDir();
    assertEquals(expected, actual);
  }

  /**
   * Test mv will throw an exception when trying to move directory into itself.
   */
  @Test(expected = IllegalOperationException.class)
  public void testMoveIntoSelfError1() throws JShellException {
    Message args = new Message(new String[]{"/sub1/", "/sub1"});
    commMv.execute(args, builder);
  }

  /**
   * Test mv will throw an exception when trying to move directory into a
   * descendant of itself.
   */
  @Test(expected = ResourceBusyException.class)
  public void testMoveIntoSelfError2() throws JShellException {
    Directory sub1 = (Directory) system.getRootDir().getElementByName("sub1");
    Directory sub2 = (Directory) sub1.getElementByName("sub2");
    system.setWorkingDir(sub2);
    Message args = new Message(new String[]{"/sub1/", "sub3"});
    commMv.execute(args, builder);
  }

  /**
   * Test mv will throw an exception when trying to move an ancestral directory
   * of the working directory.
   */
  @Test(expected = ResourceBusyException.class)
  public void testMoveAncestorError()
      throws IllegalOperationException, DuplicateException,
      WrongSyntaxException, FileNotExistException, IllegalNameException,
      NotADirectoryException, ResourceBusyException {
    Directory sub1 = (Directory) system.getRootDir().getElementByName("sub1");
    Directory sub2 = (Directory) sub1.getElementByName("sub2");
    system.setWorkingDir(sub2);

    Message args = new Message(new String[]{"/sub1/", "/sub3"});
    commMv.execute(args, builder);
  }

  @After
  public void tearDown() {
    ((MockController) system).clear();
  }
}
