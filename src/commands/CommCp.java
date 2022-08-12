package commands;

import driver.Controller;
import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.FolderElement;
import java.util.Iterator;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * Syntax: 1. cp PATH_1 PATH_2
 * <p>
 * Creates a copy of specified file/directory at PATH_1 and pastes the copy at
 * the destination at PATH_2.
 */
public class CommCp extends AbstractCommand {

  public static final String alias = "cp";
  private static CommCp instance;

  private final PathInterpreter pathInterpreter;

  /**
   * Sole constructor
   */
  public CommCp(Controller controller) {
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
  public static CommCp getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommCp(controller);
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
   * @throws DuplicateException     if the destination already contains a
   *                                file/directory with the desired name.
   * @throws IllegalNameException   if the new name at destination contains
   *                                illegal characters.
   */
  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException, NotADirectoryException,
      FileNotExistException, DuplicateException, IllegalNameException {
    if (args.length() != 2) {
      throw new WrongSyntaxException("cp: Unknown syntax");
    }
    Iterator<String> itr = args.iterator();
    Path arg1 = new Path(itr.next());
    Path arg2 = new Path(itr.next());

    FolderElement original = pathInterpreter.toFolderElement(arg1);
    if (original == null) {
      throw new FileNotExistException(arg1 + ": file does not exist");
    }
    FolderElement target = pathInterpreter.toFolderElement(arg2);
    if (target instanceof Directory) {
      ((Directory) target).insertElement(original.copy());
    } else if (target == null) {
      target = pathInterpreter.createDummyAt(arg2);
      FolderElement copied = original.copy();
      copied.renameTo(target.getName());
      target.getParentDir().insertElement(copied);
    } else {
      throw new DuplicateException(arg2 + ": already exists");
    }
    return builder.isIgnored(true).build();
  }

  public Message getManual() {
    String manual =
        "Syntax: 1. cp PATH_1 PATH_2\n"
            + "Creates a copy of specified file/directory at PATH_1 and pastes the copy at\n"
            + "the destination at PATH_2.\n";
    return new Message(manual);
  }


}