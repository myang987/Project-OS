package commands;

import driver.Controller;
import exceptions.WrongSyntaxException;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;

/**
 * Syntax: 1. {@code echo STRING [> OUTFILE] 2. echo STRING >> OUTFILE}
 * <p>
 * If OUTFILE is not provided, prints STRING on the shell. Otherwise, puts
 * STRING into file OUTFILE. STRING is a string of characters surrounded by
 * double quotation marks.
 * <p>
 * The first syntax creates a new file if OUTFILE does not exists and erases the
 * old contents if OUTFILE already exists. In either case, the only thing in
 * OUTFILE is STRING.
 * <p>
 * The second syntax appends instead of overwrites.
 */
public class CommEcho extends AbstractCommand {

  public static final String alias = "echo";
  private static CommEcho instance;

  /**
   * Construct a new instance of this command.
   *
   * @param controller the file system that this command belongs to
   */
  private CommEcho(Controller controller) {
    controller.registerCommand(this);
  }

  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller the controller that this command belongs to.
   * @return the sole instance of this class
   */
  public static CommEcho getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommEcho(controller);
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
    if (args.length() != 1) {
      throw new WrongSyntaxException("echo: wrong number of arguments");
    }
    CmdOutput output = builder.build();
    String text = checkValidity(args.getFirstString());
    output.add(text);
    return output;
  }

  private String checkValidity(String org) throws WrongSyntaxException {
    if (org.length() < 2 || !org.startsWith("\"") || !org.endsWith("\"")) {
      throw new WrongSyntaxException("Invalid string: " + org);
    }
    String middle = org.substring(1, org.length() - 1);
    if (middle.contains("\"")) {
      throw new WrongSyntaxException("Invalid string: " + org);
    }
    return middle;
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. echo STRING [> OUTFILE] 2. echo >> OUTFILE\n"
            + "\n"
            + "If OUTFILE is not provided, prints STRING on the shell. "
            + "Otherwise, puts\n"
            + "STRING into file OUTFILE. STRING is a string of characters "
            + "surrounded by\n"
            + "double quotation marks.\n"
            + "\n"
            + "The first syntax creates a new file if OUTFILE does not exists "
            + "and erases the\n"
            + "old contents if OUTFILE already exists. In either case, the only "
            + "thing in\n"
            + "OUTFILE is STRING.\n"
            + "\n"
            + "The second syntax appends instead of overwrites.";
    return new Message(manual);
  }
}
