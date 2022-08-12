package commands;

import driver.Controller;
import exceptions.WrongSyntaxException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;

/**
 * Syntax: 1. {@code history [number]}
 * <p>
 * This command prints out recent commands, one command per line. 1. cd .. 2.
 * mkdir textFolder 3. echo "Hello World" 4. fsjhdfks 5. history
 * <p>
 * The above output from history has two columns. The first column is numbered
 * such that the line with the highest number is the most recent command. The
 * most recent command is history. The second column contains the actual
 * command. Any syntactical errors typed by the user is also recorded. By
 * specifying a number ({@literal >=0}) after the command truncates the output.
 * For instance, the following command outputs the 3 most recent inputs: history
 * 3 And the output will be as follows: 4. fsjhdfks 5. history 6. history 3
 */
public class CommHistory extends AbstractCommand {

  public static final String alias = "history";
  private static CommHistory instance;

  /**
   * A list that contains all input histories.
   */
  private final ArrayList<String> history;

  /**
   * Set the commandsHashMap in Controller to ctrl
   */
  private CommHistory(Controller controller, ArrayList<String> history) {
    this.history = history;
    controller.registerCommand(this);
  }

  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller The controller that this command belongs to.
   * @param history    The initial history array list.
   * @return The sole instance of this class.
   */
  public static CommHistory getInstance(Controller controller,
      ArrayList<String> history) {
    if (instance == null) {
      instance = new CommHistory(controller, history);
    }
    return instance;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  /**
   * Executes this command
   * <p>
   * Checks if the input is an Integer using {@code Integer.parseInt()}. Return
   * 'true' if the input is an integer, 'false' if it is not.
   *
   * @param args a Message containing the arguments from user
   * @throws WrongSyntaxException if the argument does not contain an integer
   * @throws WrongSyntaxException if too many or little arguments
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException {
    if (args.length() > 1) {
      throw new WrongSyntaxException("history: too many arguments");
    }
    CmdOutput output = builder.build();
    String arg = args.getFirstString();
    if (arg.equals("")) {
      populateWithHistory(output, history.size());
    } else {
      if (!Pattern.matches("[0-9]+", arg)) {
        throw new WrongSyntaxException(
            "history: [number] argument is not a positive integer");
      }
      populateWithHistory(output, Integer.parseInt(arg));
    }
    return output;
  }

  public void recordInput(String input) {
    history.add(input);
  }

  /**
   * Prints {@code numberToDisplay} amount of previous commands for the user to
   * see, limiting the number to show to be the given specific amount from user
   * or the max amount contained in the list of commands, to avoid List index
   * exception. {@code index} is shown in front of the command to indicate
   * history number
   *
   * @param numberToDisplay the amount of historical commands to display
   * @return an command output object for printing.
   */
  private void populateWithHistory(CmdOutput output, int numberToDisplay) {
    numberToDisplay = Math.min(history.size(), numberToDisplay);
    int index = 1 + history.size() - numberToDisplay;

    for (int i = numberToDisplay; i > 0; i--) {
      output.add(index + ". " + history.get(history.size() - i));
      index++;
    }
  }

  ArrayList<String> getHistory() {
    return history;
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. history [number]\n"
            + "\n"
            + "This command prints out recent commands, one command per line. "
            + "1. cd .. 2. \n"
            + "mkdir textFolder 3. echo \"Hello World\" 4. fsjhdfks 5. history\n"
            + "\n"
            + "The above output from history has two columns. The first column "
            + "is numbered\n"
            + "such that the line with the highest number is the most recent "
            + "command. The \n"
            + "most recent command is history. The second column contains the "
            + "actual\n"
            + "command. Any syntactical errors typed by the user is also "
            + "recorded. By\n"
            + "specifying a number (>=0) after the command truncates the "
            + "output. For\n"
            + "instance, the following command outputs the 3 most recent "
            + "inputs: history 3\n"
            + "And the output will be as follows: 4. fsjhdfks 5. history 6. "
            + "history 3";
    return new Message(manual);
  }
}
