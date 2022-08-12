package test.util;

import static org.junit.Assert.assertEquals;

import commands.AbstractCommand;
import driver.Controller;
import exceptions.WrongSyntaxException;
import io.OutputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.MockController;
import test.commands.MockEmptyCommand;
import util.CommandExecutor;
import util.Message;

public class CommandExecutorTest {

  private Controller system;
  private CommandExecutor commandExecutor;

  @Before
  public void setup() {
    system = new MockController();
    commandExecutor = new CommandExecutor(new OutputHandler(
        system.getPathInterpreter()));
  }

  /**
   * Test invoke method strips > and >> flags sections from args
   */
  @Test
  public void testArgsStrip() throws Exception {
    AbstractCommand command = new MockEmptyCommand();
    Message actual = new Message(new String[]{
        "empty", "arg1", "arg2", ">", "redirect1", ">>", "redirect2", "arg3"
    });
    commandExecutor.invoke(command, actual);
    Message expected = new Message(new String[]{
        "arg1", "arg2", "arg3"
    });
    assertEquals(expected, actual);
  }

  /**
   * Test error - key present but no value given
   */
  @Test(expected = WrongSyntaxException.class)
  public void testArgsStripError() throws Exception {
    AbstractCommand command = new MockEmptyCommand();
    Message args = new Message(new String[]{
        "empty", "arg1", "arg2", ">", "redirect1", ">>"
    });
    commandExecutor.invoke(command, args);
  }

  @After
  public void tearDown() {
    ((MockController) system).clear();
    system = null;
  }

}
