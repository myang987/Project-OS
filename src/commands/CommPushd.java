package commands;

import driver.Controller;
import exceptions.FileNotExistException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.FolderElement;
import java.util.Stack;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * Syntax: 1. pushd DIR
 * <p>
 * Saves the current working directory by pushing onto directory stack and then
 * changes the new current working directory to DIR. The push is consistent as
 * per the LIFO behavior of a stack. The pushd command saves the old current
 * working directory in directory stack so that it can be returned to at any
 * time (via the popd command). The size of the directory stack is dynamic and
 * dependent on the pushd and the popd commands.
 */
public class CommPushd extends AbstractCommand {

  public static final String alias = "pushd";
  private static CommPushd instance;

  /**
   * The file system that this command belongs to.
   */
  private final Controller controller;
  /**
   * The stack of paths to directory that this instance shares with commPopd.
   */
  private final Stack<Path> pathStack;

  private final PathInterpreter pathInterpreter;

  /**
   * Sole constructor
   */
  private CommPushd(Controller controller, Stack<Path> pathStack) {
    this.controller = controller;
    this.pathStack = pathStack;
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
  public static CommPushd getInstance(Controller controller,
      Stack<Path> pathStack) {
    if (instance == null) {
      instance = new CommPushd(controller, pathStack);
    }
    return instance;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  /**
   * Executes this command.
   *
   * @param args a Message containing the arguments from user
   * @throws WrongSyntaxException   if the user doesn't specify a path, or
   *                                specifies more than one path.
   * @throws NotADirectoryException if one of the paths tries to navigate
   *                                through a file in the middle, or the target
   *                                is not a directory.
   * @throws FileNotExistException  if one of the paths tries to navigate
   *                                through a nonexistent directory.
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException, NotADirectoryException, FileNotExistException {
    if (args.length() != 1) {
      throw new WrongSyntaxException("pushd: wrong number of arguments");
    }
    String arg = args.getFirstString();
    FolderElement target = pathInterpreter.toFolderElement(new Path(arg));
    if (!(target instanceof Directory)) {
      throw new NotADirectoryException("pushd: path is not a directory");
    }
    Path oldPath = controller.getWorkingDir().getPathToThis();
    pathStack.push(oldPath);
    controller.setWorkingDir((Directory) target);
    System.out
        .println("Old directory: " + oldPath.toString() + " pushed to stack.");
    return builder.isIgnored(true).build();
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual = "Syntax: 1. pushd DIR\n"
        + "\n"
        + "Saves the current working directory by pushing onto directory stack "
        + "and then \n"
        + "changes the new current working directory to DIR. The push is "
        + "consistent\n"
        + "as per the LIFO behavior of a stack. The pushd command saves the "
        + "old current \n"
        + "working directory in directory stack so that it can be returned to "
        + "at any    \n"
        + "time (via the popd command). The size of the directory stack is "
        + "dynamic and  \n"
        + "dependent on the pushd and the popd commands.";
    return new Message(manual);
  }
}
