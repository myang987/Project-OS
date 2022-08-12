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

public class CommCpTest {

  private Controller system;
  private OutputBuilder builder;
  private CommMv commCp;

  @Before
  public void setUp() throws DuplicateException, IllegalNameException {
    system = new MockController();
    builder = new OutputBuilder(new OutputHandler(new PathInterpreter(system)));
    commCp = new MockCommMv(system);
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
   * Test cp can copy directory.
   */
  @Test
  public void testCopyDirectory()
      throws WrongSyntaxException, NotADirectoryException,
      FileNotExistException, DuplicateException, IllegalNameException,
      ResourceBusyException, IllegalOperationException {
    Directory expected = system.getRootDir().copy();
    Directory sub1 = (Directory) expected.getElementByName("sub1");
    Directory sub2 = (Directory) sub1.getElementByName("sub2").copy();
    expected.insertElement(sub2);

    Message args = new Message(new String[]{"/sub1/sub2/", "/"});
    commCp.execute(args, builder);
    Directory actual = system.getRootDir();
    assertEquals(expected, actual);
  }

  /**
   * Test cp can copy file.
   */
  @Test
  public void testCopyFile()
      throws WrongSyntaxException, NotADirectoryException,
      FileNotExistException, DuplicateException, IllegalNameException,
      ResourceBusyException, IllegalOperationException {
    Directory expected = system.getRootDir().copy();
    Directory sub1 = (Directory) expected.getElementByName("sub1");
    File subFile1 = (File) sub1.getElementByName("subFile1").copy();
    expected.insertElement(subFile1);

    Message args = new Message(new String[]{"/sub1/subFile1/", "/"});
    commCp.execute(args, builder);
    Directory actual = system.getRootDir();
    assertEquals(expected, actual);
  }

  @After
  public void tearDown() {
    ((MockController) system).clear();
  }
}