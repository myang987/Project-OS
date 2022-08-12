package commands;

import driver.Controller;
import exceptions.EmptyStackException;
import exceptions.FileNotExistException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.Directory;
import java.util.Stack;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * Syntax: 1. popd
 * <p>
 * Removes the top entry from the directory stack, and cd into it. The removal
 * is consistent as per the LIFO behavior of a stack. The popd command removes
 * the top most directory from the directory stack and makes it the current
 * working directory. If there is no directory onto the stack, gives appropriate
 * error message.
 */
public class CommPopd extends AbstractCommand {

  public static final String alias = "popd";
  private static CommPopd instance;

  /**
   * The stack of paths to directory that this instance shares with commPopd.
   */
  private final Stack<Path> pathStack;

  private final Controller controller;

  private final PathInterpreter pathInterpreter;

  /**
   * Sole constructor
   */
  private CommPopd(Controller controller, Stack<Path> pathStack) {
    this.pathStack = pathStack;
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
  public static CommPopd getInstance(Controller controller,
      Stack<Path> pathStack) {
    if (instance == null) {
      instance = new CommPopd(controller, pathStack);
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
   * @throws EmptyStackException    if the directory stack is empty.
   * @throws NotADirectoryException never.
   * @throws FileNotExistException  never.
   * @throws WrongSyntaxException   if user gives any argument.
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder) throws
      EmptyStackException, NotADirectoryException, FileNotExistException,
      WrongSyntaxException {
    if (args.length() != 0) {
      throw new WrongSyntaxException("popd: Too many argument");
    }
    try {
      Path changeTo = pathStack.pop();
      controller
          .setWorkingDir((Directory) pathInterpreter.toFolderElement(changeTo));
      System.out.println("Returning to " + changeTo);
    } catch (java.util.EmptyStackException e) {
      throw new exceptions
          .EmptyStackException("popd: directory stack empty", e);
    }
    return builder.isIgnored(true).build();
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. popd\n"
            + "\n"
            + "Removes the top entry from the directory stack, and cd into it. "
            + "The removal\n"
            + "is consistent as per the LIFO behavior of a stack. The popd "
            + "command removes\n"
            + "the top most directory from the directory stack and makes it "
            + "the current\n"
            + "working directory. If there is no directory onto the stack, "
            + "gives appropriate\n"
            + "error message.";
    return new Message(manual);
  }
}
