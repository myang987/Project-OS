package commands;

import driver.Controller;
import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.IllegalOperationException;
import exceptions.NotADirectoryException;
import exceptions.ResourceBusyException;
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
 * Syntax: 1. mv PATH_1 PATH_2
 * <p>
 * Moves the specified file or directory (and all sub elements) of PATH_1 to the
 * destination in PATH_2.
 * <p>
 * If PATH_2 ends with a different name, move the file or directory and rename
 * it.
 */
public class CommMv extends AbstractCommand {

  public static final String alias = "mv";
  private static CommMv instance;

  protected final PathInterpreter pathInterpreter;
  protected final Controller controller;

  /**
   * Sole constructor
   */
  protected CommMv(Controller controller) {
    this.pathInterpreter = controller.getPathInterpreter();
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
  public static CommMv getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommMv(controller);
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
      FileNotExistException, DuplicateException, IllegalNameException,
      IllegalOperationException, ResourceBusyException {
    if (args.length() != 2) {
      throw new WrongSyntaxException("mv: Unknown syntax");
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
      moveItem(original, (Directory) target, original.getName());
    } else if (target == null) {
      target = pathInterpreter.createDummyAt(arg2);
      moveItem(original, target.getParentDir(), target.getName());
    } else {
      throw new DuplicateException(arg2 + ": already exists");
    }
    return builder.isIgnored(true).build();
  }

  /**
   * Moves {@code oldName} file or directory located at {@code source} to the
   * directory {@code dest} with the name {@code newName}.
   *
   * @param original   the target file/directory to be moved.
   * @param destParent the parent directory of the destination
   * @param newName    the new name of the file or directory at the destination
   * @throws DuplicateException        if the destination already contains a
   *                                   file/directory with the desired name.
   * @throws IllegalNameException      if the new name at destination contains
   *                                   illegal characters.
   * @throws IllegalOperationException when trying to move a directory into
   *                                   itself.
   */
  private void moveItem(FolderElement original, Directory destParent,
      String newName)
      throws IllegalOperationException, IllegalNameException,
      DuplicateException, ResourceBusyException {
    if (original instanceof Directory) {
      checkOperation((Directory) original, destParent);
    }
    Directory originParent = original.getParentDir();
    originParent.removeElement(original.getName());
    original.renameTo(newName);
    destParent.insertElement(original);
  }

  /*
   * Check if this move is legal.
   * Illegal moves include moving an ancestor of the working directory, or
   * move a directory into itself.
   */
  private void checkOperation(Directory original, Directory destParent)
      throws ResourceBusyException, IllegalOperationException {
    Directory workingDir = controller.getWorkingDir();
    if (!original.isDescendantOf(workingDir)) {
      throw new ResourceBusyException("mv: cannot move the directory you "
          + "are currently in");
    } else if (destParent == original
        || destParent.isDescendantOf(original)) {
      throw new IllegalOperationException("mv: cannot move directory into "
          + "itself");
    }
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. mv PATH_1 PATH_2\n"
            + "\n"
            + "Moves the specified file or directory (and all sub elements) of PATH_1 to the "
            + "destination in PATH_2.\n"
            + "If PATH_2 ends with a different name, move the file or directory and rename "
            + "it.\n";
    return new Message(manual);
  }

}
