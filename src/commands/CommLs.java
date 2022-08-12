package commands;

import driver.Controller;
import exceptions.FileNotExistException;
import exceptions.NotADirectoryException;
import exceptions.WrongSyntaxException;
import io.Directory;
import io.File;
import io.FolderElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.CombinedCumulativeFlagHandler;
import util.DisjointMutexFlagHandler;
import util.Flag;
import util.IAcceptsFlags;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * Syntax: 1. ls [-R] [PATH ...]
 * <p>
 * if -R is present, recursively list all subdirectories. If no path is given,
 * prints the contents of the current directory. Otherwise, for each path p, the
 * order listed:
 * <ol>
 *  <li>If p specifies a file, prints p</li>
 *  <li>If p specifies a directory, prints p, a colon, then the contents of that
 *  directory, then an extra new line.</li>
 *  <li>If p does not exist, prints a error message.</li>
 * </ol>
 */
public class CommLs extends AbstractCommand implements IAcceptsFlags {

  public static final String alias = "ls";
  private static CommLs instance;

  /**
   * The file system that this command belongs to.
   */
  private final Controller controller;
  /**
   * A pathInterpreter that helps locate places in the file system.
   */
  private final PathInterpreter pathInterpreter;

  /**
   * Sole constructor
   */
  protected CommLs(Controller controller) {
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
  public static CommLs getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommLs(controller);
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
    CmdOutput output = builder.build();
    ArrayList<String> options = getFlagValues(args);
    boolean recur = options.contains("R");
    if (args.length() == 0) {
      populateWithDirectory(output, controller.getWorkingDir(), 0, recur);
      output.removeFirstString();
    } else {
      for (String arg : args) {
        Path path = new Path(arg);
        FolderElement target;
        try {
          target = pathInterpreter.toFolderElement(path);
          if (target == null) {
            throw new FileNotExistException(
                path + ": No such file or directory");
          }
        } catch (FileNotExistException | NotADirectoryException e) {
          output.setBufferedException(e);
          return output;
        }
        if (target instanceof File) {
          String pathStr = target.getPathToThis().toString();
          // Does not include the last slash of the path
          output.add(pathStr.substring(0, pathStr.length() - 1));
        } else {
          populateWithDirectory(output, (Directory) target, 2, recur);
        }
      }
    }
    return output;
  }

  protected ArrayList<String> getFlagValues(Message args)
      throws WrongSyntaxException {
    Flag f = constructFlags()[0];
    ArrayList<String> values = new ArrayList<>();
    while (f.extractValue(args)) {
      if (!f.getValue().equals("R")) {
        throw new WrongSyntaxException("ls: unknown flag "
            + "\"-" + f.getValue() + "\"");
      }
      if (!values.contains(f.getValue())) {
        values.add(f.getValue());
      }
    }
    return values;
  }

  protected void populateWithDirectory(CmdOutput output, Directory dir,
      int indent, boolean recursively) {
    output.add(dir.getPathToThis().toString() + ": ");
    StringBuilder sb =
        new StringBuilder(new String(new char[indent]).replace("\0", " "));
    Queue<Directory> subDirs = new LinkedList<>();
    for (FolderElement fe : dir) {
      sb.append(fe.getName()).append("  ");
      if (recursively && fe instanceof Directory) {
        subDirs.add((Directory) fe);
      }
    }
    output.add(sb.toString());
    for (Directory subDir : subDirs) {
      populateWithDirectory(output, subDir, 2, true);
    }
  }

  @Override
  public Flag[] constructFlags() {
    return new Flag[]{new Flag("-", new CombinedCumulativeFlagHandler())};
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. ls [-R] [PATH ...]\n"
            + "\n"
            + "If -R is present, recursively list all subdirectories.\n"
            + "If no path is given, prints the contents of the current "
            + "directory. Otherwise,\n"
            + "for each path p, the order listed:\n"
            + "\n"
            + " 1. If p specifies a file, prints p.\n"
            + " 2. If p specifies a directory, prints p, a colon, then the "
            + "contents of that\n"
            + " directory, then an extra new line.\n"
            + " 3. If p does not exist, prints a error message.";
    return new Message(manual);
  }
}
