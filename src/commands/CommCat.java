package commands;

import driver.Controller;
import exceptions.FileNotExistException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.File;
import io.FolderElement;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.ErrorHandler;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * Syntax: cat FILE1 [FILE2 ...]
 * <p>
 * This command displays the contents of FILE1 and other files (File2 ...)
 * concatenated in the shell. There are three line breaks to separate the
 * contents of one file from another.
 */
public class CommCat extends AbstractCommand {

  public static final String alias = "cat";
  private static CommCat instance;

  private final PathInterpreter pathInterpreter;
  private final ErrorHandler errorHandler;

  /**
   * Sole constructor
   */
  private CommCat(Controller controller) {
    this.pathInterpreter = controller.getPathInterpreter();
    this.errorHandler = controller.getErrorHandler();
    controller.registerCommand(this);
  }

  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller the controller that this command belongs to.
   * @return the sole instance of this class
   */
  public static CommCat getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommCat(controller);
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
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException {
    if (args.length() < 1) {
      throw new WrongSyntaxException("cat: Path expected");
    }
    builder.withLineSeparator("\n\n\n");
    CmdOutput output = builder.build();
    for (String str : args) {
      try {
        output.add(grabFileAt(new Path(str)));
      } catch (FileNotExistException | NotADirectoryException e) {
        errorHandler.resolve(e);
      }
    }
    return output;
  }

  /**
   * Displays the contents of the file targeted by {@code path}.
   *
   * @param path the path to the file to be displayed
   * @throws NotADirectoryException if the path tries to navigate through a file
   *                                in the middle.
   * @throws FileNotExistException  if the path tries to navigate through a
   *                                nonexistent directory, or the target file
   *                                does not exist.
   */
  private String grabFileAt(Path path)
      throws NotADirectoryException, FileNotExistException {
    FolderElement target = pathInterpreter.toFolderElement(path);
    if (!(target instanceof File)) {
      throw new FileNotExistException(path + ": Not a file");
    }
    return ((File) target).getContents();
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: cat FILE1 [FILE2 ...]\n"
            + "\n"
            + "This command displays the contents of FILE1 and other files "
            + "(File2 ...)\n"
            + "concatenated in the shell. There are three line breaks to "
            + "separate the\n"
            + "contents of one file from another.";
    return new Message(manual);
  }
}
