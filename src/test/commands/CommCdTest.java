package test.commands;


import commands.CommCd;
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

public class CommCdTest {
  private Controller system;
  private OutputBuilder builder;
  private CommCd commCd;
	   		  
		  
  @Before
  public void setUp() throws DuplicateException, IllegalNameException, WrongSyntaxException {
    system = new MockController();
    builder = new OutputBuilder(new OutputHandler(new PathInterpreter(system)));
    commCd = new MockCommCd(system);
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
   * Test cd change the working directory to input DIR if DIR exist.
   */
  @Test
  public void testCdToExistDirectory() throws JShellException {
    Message args = new Message(new String[]{"/sub1"});
    commCd.execute(args, builder);
    Path path = new Path("/sub1");
    FolderElement expected =
            system.getPathInterpreter().toFolderElement(path);
    FolderElement actual = system.getWorkingDir();
    assertEquals(expected, actual);
  }
  
  /**
   * Test cd throws NotADirectoryException if the input DIR does not exist
   */
  @Test(expected = NotADirectoryException.class)
  public void testCdToUnExistDirectory() throws JShellException {
    Message args = new Message(new String[]{"/sub1000"});
    commCd.execute(args, builder);
  }
  
  /**
   * Test cd throws NotADirectoryException if the input DIR is a file
   */
  @Test(expected = NotADirectoryException.class)
  public void testCdToFile() throws JShellException {
    Message args = new Message(new String[]{"/file1"});
    commCd.execute(args, builder);
  }
  
  @After
  public void tearDown() {
    ((MockController) system).clear();
  }
}