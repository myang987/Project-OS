package commands;

import driver.Controller;
import exceptions.FileNotExistException;
import exceptions.JShellException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.File;
import io.FolderElement;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.DisjointMutexFlagHandler;
import util.Flag;
import util.IAcceptsFlags;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * Syntax: search path ... -type [f|d] -name expression
 * <p>
 * This command searches all folder elements of type file or directory of name
 * {@code expression} in {@code path}s and prints their absolute path.
 */
public class CommSearch extends AbstractCommand implements IAcceptsFlags {

  public static final String alias = "search";
  private static CommSearch instance;

  private final PathInterpreter pathInterpreter;

  protected CommSearch(Controller controller) {
    this.pathInterpreter = controller.getPathInterpreter();
    controller.registerCommand(this);
  }

  public static CommSearch getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommSearch(controller);
    }
    return instance;
  }

  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws WrongSyntaxException, FileNotExistException, NotADirectoryException {
    Flag[] flags = constructFlags();
    for (Flag flag : flags) {
      if (!flag.extractValue(args)) {
        throw new WrongSyntaxException(flag.getKey() + ": flag value expected");
      }
    }
    // TODO: rewrite this part if someone comes up with new methods.
    FolderElement dummy;
    if (flags[0].getValue().equals("f")) {
      dummy = new File(null, null, null, null);
    } else if (flags[0].getValue().equals("d")) {
      dummy = new Directory(null, null);
    } else {
      throw new WrongSyntaxException("-type " + flags[0].getValue() + ": "
          + "unknown parameter");
    }
    String name = checkValidity(flags[1].getValue());
    CmdOutput output = builder.build();
    for (String str : args) {
      FolderElement fe;
      try {
        fe = pathInterpreter.toFolderElement(new Path(str));
        if (fe == null) {
          throw new FileNotExistException(str + ": does not exist");
        }
      } catch (JShellException jse) {
        output.setBufferedException(jse);
        return output;
      }
      if (fe.getClass() == dummy.getClass() && fe.getName().equals(name)) {
        output.add(fe.getPathToThis().toString());
      }
      if (fe instanceof Directory) {
        populateOutput(output, name, (Directory) fe, dummy);
      }
    }
    return output;
  }

  private void populateOutput(CmdOutput output, String name, Directory dir,
      FolderElement dummy) {
    for (FolderElement fe : dir) {
      if (fe.getClass() == dummy.getClass() && fe.getName().equals(name)) {
        output.add(fe.getPathToThis().toString());
      }
      if (fe instanceof Directory) {
        populateOutput(output, name, (Directory) fe, dummy);
      }
    }
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

  public Flag[] constructFlags() {
    return new Flag[]{
        new Flag("-type", new DisjointMutexFlagHandler()),
        new Flag("-name", new DisjointMutexFlagHandler())
    };
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: search path ... -type [f|d] -name expression\n"
            + "\n"
            + "This command searches all folder "
            + "elements of type file or directory of name\n"
            + "{@code expression} in {@code path}s and prints "
            + "their absolute path.\n";
    return new Message(manual);
  }

  @Override
  public String getAlias() {
    return alias;
  }
}
