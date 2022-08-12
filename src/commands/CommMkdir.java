package commands;

import driver.Controller;
import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.FolderElement;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * Syntax: 1. mkdir DIR1 ...
 * <p>
 * This command can take in more than one DIR. Create directories, each of which
 * may be relative to the current directory or may be a full path. It will
 * continue to create directories based on the arguments until you hit the first
 * invalid argument. (i.e.If creating DIR1 results in any kind of error, it does
 * not proceed with creating DIR 2. However, if DIR1 was created successfully,
 * and DIR2 creation results in an error, then it gives back an error specific
 * to DIR2. etc)
 */
public class CommMkdir extends AbstractCommand {

  public static final String alias = "mkdir";
  private static CommMkdir instance;

  /**
   * A pathInterpreter that helps locate places in the file system.
   */
  private final PathInterpreter pathInterpreter;

  /**
   * Sole constructor
   */
  private CommMkdir(Controller controller) {
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
  public static CommMkdir getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommMkdir(controller);
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
   * @throws WrongSyntaxException   if user input is not in accordance with the
   *                                syntax.
   * @throws NotADirectoryException if either of the paths tries to navigate
   *                                through a file in the middle of the path.
   * @throws FileNotExistException  if either of the paths tries to navigate
   *                                through a nonexistent directory.
   * @throws DuplicateException     if the location for creating the directories
   *                                already contains a file or directory of the
   *                                same name.
   * @throws IllegalNameException   if the name of the file contains an illegal
   *                                character.
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException, NotADirectoryException,
      FileNotExistException, DuplicateException, IllegalNameException {
    if (args.length() == 0) {
      throw new WrongSyntaxException("mkdir: Too few arguments");
    }
    for (String argsListPath : args) {
      Path path = new Path(argsListPath);
      FolderElement dummy = pathInterpreter.createDummyAt(path);
      dummy.getParentDir().createDirectory(dummy.getName());
    }
    System.out.println("mkdir: Task completed.");
    return builder.isIgnored(true).build();
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. mkdir DIR1 ...\n"
            + "\n"
            + "This command can take in more than one DIR. Create directories, "
            + "each of which\n"
            + "may be relative to the current directory or may be a full path. \n"
            + "It will continue to create directories based on the arguments until you hit the\n"
            + "first invalid argument\n"
            + "(i.e. If creating DIR1 results in any kind of error, \n"
            + "it does not proceed with creating DIR 2.\n"
            + "However, if DIR1 was created successfully, and DIR2 creation "
            + "results in an \n"
            + "error, then it gives back an error specific to DIR2.)";
    return new Message(manual);
  }
}
