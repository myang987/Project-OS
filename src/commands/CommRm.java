package commands;

import driver.Controller;
import exceptions.FileNotExistException;
import exceptions.JShellException;
import exceptions.NotADirectoryException;
import exceptions.ResourceBusyException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.FolderElement;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * Syntax: 1. rm DIR
 * <p>
 * removes the DIR from the file system. This also removes all the children of
 * DIR (i.e. it acts recursively).
 */

public class CommRm extends AbstractCommand {

  public static final String alias = "rm";
  private static CommRm instance;
  /**
   * A pathInterpreter that helps locate places in the file system.
   */
  private final PathInterpreter pathInterpreter;
  private final Controller controller;


  /**
   * Sole constructor
   */
  public CommRm(Controller controller) {
    this.controller = controller;
    this.pathInterpreter = controller.getPathInterpreter();
    controller.registerCommand(this);
  }

  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller the controller that this command belongs to.
   * @return the sole instance of this class
   */
  public static CommRm getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommRm(controller);
    }
    return instance;
  }

  /**
   * Executes this command.
   *
   * @param args a Message containing the arguments from user
   * @throws WrongSyntaxException   if user input is not in accordance with the
   *                                syntax.
   * @throws NotADirectoryException if either of the paths tries to navigate
   *                                through a file in the middle of the path.
   * @throws FileNotExistException  if either of the paths tries to navigate
   *                                through a nonexistent directory.
   */
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws JShellException, WrongSyntaxException, FileNotExistException,
      ResourceBusyException {
    if (args.length() != 1) {
      if (args.length() < 1) {
        throw new WrongSyntaxException("mkdir: Too few "
            + "arguments");
      } else {
        throw new WrongSyntaxException("mkdir: Too many "
            + "arguments");
      }
    }
    String pathToRm = args.getFirstString();
    Path path = new Path(pathToRm, "/");
    FolderElement element = pathInterpreter.toFolderElement(path);
    if (element == null) {
      throw new FileNotExistException("No such file or directory");
    } else if (element instanceof Directory) {
      checkOperation((Directory) element);
    }
    Directory parent = element.getParentDir();
    parent.removeElement(element.getName());
    return builder.isIgnored(true).build();
  }

  /*
   * Check if this move is legal.
   * Illegal moves include moving an ancestor of the working directory, or
   * move a directory into itself.
   */
  private void checkOperation(Directory target)
      throws ResourceBusyException {
    if (!target.isDescendantOf(controller.getWorkingDir())) {
      throw new ResourceBusyException("rm: cannot remove the directory you "
          + "are currently in");
    }
  }

  @Override
  public Message getManual() {
    String manual = "Syntax: rm DIR\n\n"
        + "removes the DIR from the file system. This also removes all\n"
        + "the children of DIR (i.e. it acts recursively).";
    return new Message(manual);
  }


  @Override
  public String getAlias() {
    return alias;
  }

}
