package test.commands;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import commands.CommSearch;
import driver.Controller;
import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.JShellException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.OutputHandler;
import java.io.FileNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.MockController;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.PathInterpreter;


public class CommSearchTest {

  private Controller system;
  private OutputBuilder builder;
  private CommSearch commSearch;
  private CmdOutput expected;

  /**
   * root: [sub1: [sampleFile, sub2: [sampleFile, sub3: [sampleFile]]], file1]
   */
  @Before
  public void setUp() throws DuplicateException, IllegalNameException {
    system = new MockController();
    builder = new OutputBuilder(new OutputHandler(new PathInterpreter(system)));
    commSearch = new MockCommSearch(system);
    Directory root = system.getRootDir();
    Directory sub1 = root.createDirectory("sub1");
    Directory sub3 = root.createDirectory("sub3");
    Directory sub2 = sub1.createDirectory("sub2");
    root.createFile("file1");
    sub1.createFile("sampleFile1");
    sub3.createFile("sampleFile1");
    sub2.createFile("sampleFile");
    expected = builder.build();
  }

  /**
   * Test search throws WrongSyntaxException when missing parameters
   */
  @Test(expected = WrongSyntaxException.class)
  public void testSearchSynError() throws JShellException {
    Message args = new Message(new String[]{"/sub1/sub2"});
    commSearch.execute(args, builder);
  }

  /**
   * Test search execute properly when only one path is provided and the input
   * file does not exist
   */
  @Test
  public void testSearchOnePathDNE() throws JShellException {
    Message args = new Message(
        new String[]{"/sub1", "-type", "d", "-name", "\"sub10\""});
    CmdOutput actual = commSearch.execute(args, builder);
    assertEquals(expected, actual);
  }


  /**
   * Test search execute properly when only one path is provided and the input
   * file exist
   */
  @Test
  public void testSearchOnePath() throws JShellException {
    Message args = new Message(
        new String[]{"/sub1", "-type", "d", "-name", "\"sub2\""});
    expected.add("/sub1/sub2/");
    CmdOutput actual = commSearch.execute(args, builder);
    assertEquals(expected, actual);
  }


  /**
   * Test search execute properly when multiple path is provided and the input
   * file exist in every path
   */
  @Test
  public void testSearchMulPath() throws JShellException {
    Message args = new Message(
        new String[]{"/sub1", "/sub3", "-type", "f", "-name",
            "\"sampleFile1\""});
    expected.add("/sub1/sampleFile1/");
    expected.add("/sub3/sampleFile1/");
    CmdOutput actual = commSearch.execute(args, builder);
    assertEquals(expected, actual);
  }

  /**
   * Test search execute properly when multiple path is provided and the input
   * file does not exist in any path
   */
  @Test
  public void testSearchMulPathDNE() throws JShellException {
    Message args = new Message(
        new String[]{"/sub3", "/sub1/sub2", "-type", "f", "-name",
            "\"sampleFile1\""});
    expected.add("/sub3/sampleFile1/");
    CmdOutput actual = commSearch.execute(args, builder);
    assertEquals(expected, actual);
  }

  /**
   * Test multiple paths are given, but some of them that are not the first
   * one are invalid.
   */
  @Test
  public void testSearchSomeInvalidPaths() throws JShellException {
    Message args = new Message(new String[]{"sub3", "sub4", "-type", "f",
        "-name", "\"sampleFile1\""});
    expected.add("/sub3/sampleFile1/");
    CmdOutput actual = commSearch.execute(args, builder);
    FileNotExistException ex =
        (FileNotExistException) actual.getBufferedException();
    assertNotNull(ex);
    actual.setBufferedException(null);
    assertEquals(expected, actual);
  }


  @After
  public void tearDown() {
    ((MockController) system).clear();
  }
}
