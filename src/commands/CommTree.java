package commands;

import driver.Controller;
import exceptions.JShellException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.FolderElement;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;

/**
 * Syntax: tree
 * <p>
 * This command searches display the entire file system as a tree. Every level
 * of the tree are indented by a tab character.
 */

public class CommTree extends AbstractCommand {

  public static final String alias = "tree";
  private static CommTree instance;

  private final Controller controller;

  protected CommTree(Controller controller) {
    this.controller = controller;
    controller.registerCommand(this);
  }

  public static CommTree getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommTree(controller);
    }
    return instance;
  }

  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws JShellException {
    if (args.length() != 0) {
      throw new WrongSyntaxException("tree: Too many arguments");
    }
    CmdOutput output = builder.build();
    output.add(controller.getRootDir().getName());
    populateWithDirectory(output, controller.getRootDir(), 1);
    return output;
  }

  private void populateWithDirectory(CmdOutput output, Directory dir,
      int indent) {
    String indentation = new String(new char[indent]).replace("\0", "\t");
    for (FolderElement fe : dir) {
      String line = indentation + fe.getName();
      if (fe instanceof Directory) {
        output.add(line);
        populateWithDirectory(output, (Directory) fe, indent + 1);
      } else {
        output.add(line);
      }
    }
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: tree\n"
            + "\n"
            + "This command searches display the entire file system as a tree.\n"
            + "Every level of the tree are indented by a tab character.\n";
    return new Message(manual);
  }

  @Override
  public String getAlias() {
    return alias;
  }
}
