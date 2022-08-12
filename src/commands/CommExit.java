package commands;

import driver.Controller;
import exceptions.WrongSyntaxException;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;

/**
 * Syntax: 1. exit
 * <p>
 * Quits the program.
 */
public class CommExit extends AbstractCommand {

  public static final String alias = "exit";
  private static CommExit instance;

  protected CommExit(Controller controller) {
    controller.registerCommand(this);
  }

  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller the controller that this command belongs to.
   * @return the sole instance of this class
   */
  public static CommExit getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommExit(controller);
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
   * @throws WrongSyntaxException if user input is not in accordance with the
   *                              syntax.
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException {
    if (args.length() != 0) {
      throw new WrongSyntaxException("exit: too many arguments");
    }
    System.out.print("exiting shell");
    return builder.isIgnored(true).build();
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual = "Syntax: 1. exit\n"
        + "\n"
        + "Quits the program.";
    return new Message(manual);
  }
}
