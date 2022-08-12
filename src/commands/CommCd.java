package commands;

import driver.Controller;
import exceptions.FileNotExistException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.FolderElement;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;


/**
 * Syntax: 1. cd DIR
 * <p>
 * Changes directory to DIR, which may be relative to the current directory or
 * may be a full path. As with Unix, .. means a parent directory and a . means
 * the current directory. The root directory must be /, the forward slash.
 */
public class CommCd extends AbstractCommand {

  public static final String alias = "cd";
  private static CommCd instance;

  /**
   * The file system that this command belongs to.
   */
  private final Controller controller;

  private final PathInterpreter pathInterpreter;

  /**
   * Sole constructor
   */
  protected CommCd(Controller controller) {
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
  public static CommCd getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommCd(controller);
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
   * @throws NotADirectoryException if the path tries to navigate through a file
   *                                in the middle, or the target is not a
   *                                directory.
   * @throws FileNotExistException  if the path tries to navigate through a
   *                                nonexistent directory.
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException, NotADirectoryException,
      FileNotExistException {
    if (args.length() != 1) {
      throw new WrongSyntaxException("cd: wrong number of arguments");
    }
    String arg = args.getFirstString();
    Path path = new Path(arg);
    setWorkingDir(path);
    return builder.isIgnored(true).build();
  }

  /**
   * Updates {@code workingDir} to the directory {@code path} is targeting at.
   *
   * @param path the new path to the working directory
   * @throws NotADirectoryException if {@code path} tries to navigate through a
   *                                file in the middle of it, or if the
   *                                targeting folder element is not a
   *                                directory.
   * @throws FileNotExistException  if {@code path} tries to navigate through a
   *                                nonexistent folder element.
   */
  public void setWorkingDir(Path path)
      throws NotADirectoryException, FileNotExistException {
    FolderElement newDir =
        pathInterpreter.toFolderElement(path);
    if (!(newDir instanceof Directory)) {
      throw new NotADirectoryException("Cannot change directory to a file or "
          + "such directory does not exit");
    } else {
      controller.setWorkingDir((Directory) newDir);
    }
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. cd DIR\n"
            + "\n"
            + "Changes directory to DIR, which may be relative to the current "
            + "directory or\n"
            + "may be a full path. As with Unix, .. means a parent directory "
            + "and a . means\n"
            + "the current directory. The root directory must be /, the forward "
            + "slash.";
    return new Message(manual);
  }
}