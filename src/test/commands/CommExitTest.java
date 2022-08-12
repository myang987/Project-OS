package test.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import driver.Controller;
import exceptions.ConnectionFailedException;
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

import commands.CommCurl;
import commands.CommExit;
import test.MockController;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.PathInterpreter;

public class CommExitTest {
  
  private Controller system;
  private OutputBuilder builder;
  private CommExit commExit;
  
  
  @Before
  public void setUp() throws DuplicateException, IllegalNameException {
    system = new MockController();
    builder = new OutputBuilder(new OutputHandler(new PathInterpreter(system)));
    commExit = new MockCommExit(system);
    Directory root = system.getRootDir();
  }
  
  /**
   * Test exit does quits the program.
   */
  @Test
  public void testExit() throws JShellException {
    Message args = new Message(new String[]{""});
    commExit.execute(args, builder);
    assert(!system.isRunning());
  }
  
  @After
  public void tearDown() {
    ((MockController) system).clear();
  }
}
