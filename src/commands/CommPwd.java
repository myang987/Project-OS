package commands;

import driver.Controller;
import exceptions.WrongSyntaxException;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;

/**
 * Syntax: 1. pwd
 * <p>
 * Prints the current working directory (including the whole path).
 */
public class CommPwd extends AbstractCommand {

  public static final String alias = "pwd";
  private static CommPwd instance;

  /**
   * The file system that this command belongs to.
   */
  private final Controller controller;

  /**
   * Sole constructor
   */
  private CommPwd(Controller controller) {
    this.controller = controller;
    controller.registerCommand(this);
  }

  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller the controller that this command belongs to.
   * @return the sole instance of this class
   */
  public static CommPwd getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommPwd(controller);
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
   * @param args args a Message containing the arguments from user
   * @throws WrongSyntaxException is user gives any argument
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException {
    if (args.length() != 0) {
      throw new WrongSyntaxException("pwd: Too many arguments");
    }
    CmdOutput output = builder.build();
    Path path = controller.getWorkingDir().getPathToThis();
    output.add(path.toString());
    return output;
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. pwd\n"
            + "\n"
            + "Prints the current working directory (including the whole path).";
    return new Message(manual);
  }
}
