package commands;

import driver.Controller;
import exceptions.WrongSyntaxException;
import io.OutputHandler;
import java.util.HashMap;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;

/**
 * Syntax: 1. man CMD [CMD2 ...]
 * <p>
 * This command prints out the manual of the specified [command]
 */
public class CommMan extends AbstractCommand {

  public static final String alias = "man";
  private static CommMan instance;

  /**
   * A outputHandler object that helps print out manuals
   */
  private final OutputHandler outputHandler;
  /**
   * The same hashmap as in the Controller class, used to call the getManual
   * method in each command class.
   */
  private final HashMap<String, AbstractCommand> commandsHashMap;

  /**
   * Sole constructor
   */
  protected CommMan(Controller controller,
      HashMap<String, AbstractCommand> commandsHashMap) {
    this.outputHandler = controller.getPrinter();
    this.commandsHashMap = commandsHashMap;
    controller.registerCommand(this);
  }

  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller the controller that this command belongs to.
   * @return the sole instance of this class
   */
  public static CommMan getInstance(Controller controller,
      HashMap<String, AbstractCommand> commandsHashMap) {
    if (instance == null) {
      instance = new CommMan(controller, commandsHashMap);
    }
    return instance;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  /**
   * Executes this command.
   * <p>
   * Receive a command input from user and print {@code commandInput.getManual()}
   * the manual of the respective commandInput functions.
   *
   * @param args a Message containing the arguments from user
   * @return command manual defined in the getManual() of each respective
   * function
   * @throws WrongSyntaxException if the specified command does not exist
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException {
    builder.withLineSeparator("\n\n\n");
    CmdOutput output = builder.build();
    if (args.length() != 1) {
      throw new WrongSyntaxException("man: Wrong number of arguments");
    }
    String arg = args.getFirstString();
    AbstractCommand commandRequested = commandsHashMap.get(arg);
    if (commandRequested == null) {
      output.setBufferedException(
          new WrongSyntaxException("Unknown command: " + arg));
      return output;
    } else {
      output
          .add("Manual for " + arg + ": \n" + commandRequested.getManual());
    }
    return output;
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual = "Syntax: 1. man CMD [CMD2 ...]\n\n"
        + "Print documentation for CMD (s).";
    return new Message(manual);
  }
}
