package test.commands;

import commands.CommTree;
import driver.Controller;
import exceptions.DuplicateException;
import exceptions.IllegalNameException;
import io.Directory;
import io.OutputHandler;
import test.MockController;
import util.PathInterpreter;
import util.CmdOutput.OutputBuilder;
import driver.Controller;
import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.JShellException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.FolderElement;
import io.OutputHandler;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.MockController;
import util.CmdOutput.OutputBuilder;
import util.CmdOutput;
import util.Message;
import util.Path;
import util.PathInterpreter;


public class CommTreeTest {
  private Controller system;
  private OutputBuilder builder;
  private CommTree commTree;
  private CmdOutput expected;
  
  /**
   * root: [sub1: [subFile1, sub2: [subFile2, sub3: [subFile3]]], file1]
   */
  @Before
  public void setUp() throws DuplicateException, IllegalNameException {
    system = new MockController();
    builder = new OutputBuilder(new OutputHandler(new PathInterpreter(system)));
    commTree = new MockCommTree(system);
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
   * Test display the entire file system as a tree properly.
   */
  @Test
  public void testTreeDisplayProperly() throws JShellException {
    Message args = new Message(new String[]{""});
    CmdOutput actual= commTree.execute(args, builder);
    expected.add("/");
    expected.add("\tsub1");
    expected.add("\t\tsub2");
    expected.add("\t\t\tsubFile2");
    expected.add("\t\tsubFile1");
    expected.add("\tsub3");
    expected.add("\t\tsubFile3");
    expected.add("\tfile1");
    assertEquals(expected,actual);
  }
  
  /**
   * Test tree throws WrongSyntaxException when takes in one or more 
   * input parameter
   */
  @Test(expected = WrongSyntaxException.class)
  public void testTreeSynError() throws JShellException {
    Message args = new Message(new String[]{"123"});
    commTree.execute(args, builder);
  }
  
  @After
  public void tearDown() {
    ((MockController) system).clear();
  }
}
