package test.commands;

import static org.junit.Assert.*;

import commands.CommLs;
import driver.Controller;
import exceptions.DuplicateException;
import exceptions.IllegalNameException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.OutputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.MockController;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.PathInterpreter;

public class CommLsTest {

  private Controller system;
  private OutputBuilder builder;
  private CommLs commLs;
  private CmdOutput expected;

  @Before
  public void setUp() throws DuplicateException, IllegalNameException {
    system = new MockController();
    builder = new OutputBuilder(new OutputHandler(new PathInterpreter(system)));
    commLs = new MockCommLs(system);
    Directory root = system.getRootDir();
    Directory sub1 = root.createDirectory("sub1");
    Directory sub3 = root.createDirectory("sub3");
    Directory sub2 = sub1.createDirectory("sub2");
    root.createFile("file1");
    sub1.createFile("subFile1");
    sub3.createFile("subFile3");
    sub2.createFile("subFile2");
    expected = builder.build();
  }

  /**
   * Test no argument.
   */
  @Test
  public void testNoArg() throws WrongSyntaxException {
    Message args = new Message();
    CmdOutput actual = commLs.execute(args, builder);
    expected.add("sub1  sub3  file1  ");
    assertEquals(expected, actual);
  }

  /**
   * Test list a file.
   */
  @Test
  public void testFile() throws WrongSyntaxException {
    Message args = new Message("file1");
    CmdOutput actual = commLs.execute(args, builder);
    expected.add("/file1");
    assertEquals(expected, actual);
  }

  /**
   * Test list a directory.
   */
  @Test
  public void testDir() throws WrongSyntaxException {
    Message args = new Message("sub1");
    CmdOutput actual = commLs.execute(args, builder);
    expected.add("/sub1/: ");
    expected.add("  sub2  subFile1  ");
    assertEquals(expected, actual);
  }

  /**
   * Test multiple arguments.
   */
  @Test
  public void testMultiple() throws WrongSyntaxException {
    Message args = new Message(new String[]{"sub1", "sub3", "sub1/subFile1"});
    CmdOutput actual = commLs.execute(args, builder);
    expected.add("/sub1/: ");
    expected.add("  sub2  subFile1  ");
    expected.add("/sub3/: ");
    expected.add("  subFile3  ");
    expected.add("/sub1/subFile1");
    assertEquals(expected, actual);
  }

  /**
   * Test -R with no argument
   */
  @Test
  public void testRecursive() throws WrongSyntaxException {
    Message args = new Message(new String[]{"-R"});
    CmdOutput actual = commLs.execute(args, builder);
    expected.add("sub1  sub3  file1  ");
    expected.add("/sub1/: ");
    expected.add("  sub2  subFile1  ");
    expected.add("/sub1/sub2/: ");
    expected.add("  subFile2  ");
    expected.add("/sub3/: ");
    expected.add("  subFile3  ");
    assertEquals(expected, actual);
  }

  /**
   * Test -R with directory as argument.
   */
  @Test
  public void testRecursive2() throws WrongSyntaxException {
    Message args = new Message(new String[]{"-R", "/"});
    CmdOutput actual = commLs.execute(args, builder);
    expected.add("/: ");
    expected.add("  sub1  sub3  file1  ");
    expected.add("/sub1/: ");
    expected.add("  sub2  subFile1  ");
    expected.add("/sub1/sub2/: ");
    expected.add("  subFile2  ");
    expected.add("/sub3/: ");
    expected.add("  subFile3  ");
    assertEquals(expected, actual);
  }

  /**
   * Test -R with file as argument.
   */
  @Test
  public void testRecursive3() throws WrongSyntaxException {
    Message args = new Message(new String[]{"-R", "sub1/subFile1"});
    CmdOutput actual = commLs.execute(args, builder);
    expected.add("/sub1/subFile1");
    assertEquals(expected, actual);
  }

  /**
   * Test -R with mixed arguments.
   */
  @Test
  public void testRecursive4() throws WrongSyntaxException {
    Message args = new Message(new String[]{"-R", "file1", "sub1"});
    CmdOutput actual = commLs.execute(args, builder);
    expected.add("/file1");
    expected.add("/sub1/: ");
    expected.add("  sub2  subFile1  ");
    expected.add("/sub1/sub2/: ");
    expected.add("  subFile2  ");
    assertEquals(expected, actual);
  }

  /**
   * Test unexpected flags
   */
  @Test(expected = WrongSyntaxException.class)
  public void testFlagError() throws WrongSyntaxException {
    Message args = new Message(new String[]{"-RRR", "file1", "sub1"});
    commLs.execute(args, builder);
  }

  @After
  public void tearDown() {
    ((MockController) system).clear();
  }

}
