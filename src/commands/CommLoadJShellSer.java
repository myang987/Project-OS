package commands;

import driver.Controller;
import exceptions.JShellException;
import exceptions.WrongSyntaxException;
import io.Directory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Stack;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;

public class CommLoadJShellSer extends CommLoadJShell {

  public static final String alias = "loadJShell";
  private static CommLoadJShell instance;

  protected CommLoadJShellSer(Controller controller, ArrayList<String> history,
      PathInterpreter pathInterpreter, Stack<Path> pathStack) {
    super(controller, history, pathInterpreter, pathStack);
    controller.registerCommand(this);
  }

  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller the controller that this command belongs to.
   * @return the sole instance of this class
   */
  public static CommLoadJShell getInstance(Controller controller,
      ArrayList<String> history, PathInterpreter pathInterpreter,
      Stack<Path> pathStack) {
    if (instance == null) {
      instance = new CommLoadJShellSer(controller, history, pathInterpreter,
          pathStack);
    }
    return instance;
  }

  @Override
  public String getAlias() {
    return super.getAlias();
  }

  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws JShellException {
    if (controller.isModified()) {
      throw new JShellException("You have already modified this system, "
          + "please only load save files from a new session.");
    }
    if (args.length() != 1) {
      throw new WrongSyntaxException("saveJShell: Wrong number of arguments");
    }
    String loadPath = args.getFirstString();
    ArrayList<?> saveState = deserialize(loadPath);

    controller.setRootDir((Directory) saveState.get(0));

    controller.setWorkingDir((Directory) pathInterpreter.toFolderElement(
        new Path(((String) saveState.get(1)))));

    ArrayList<String> savedHistory = (ArrayList<String>) saveState.get(2);
    history.addAll(0, savedHistory);

    Stack<Path> savedPaths = (Stack<Path>) saveState.get(3);
    for (Path path : savedPaths) {
      pathStack.push(path);
    }

    return builder.isIgnored(true).build();
  }

  private ArrayList<?> deserialize(String path) {
    ArrayList<?> saveState = null;
    try {
      FileInputStream fileIn = new FileInputStream(path);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      saveState = (ArrayList<?>) in.readObject();
      in.close();
      fileIn.close();
    } catch (IOException i) {
      i.printStackTrace();
    } catch (ClassNotFoundException c) {
      c.printStackTrace();
      System.out.println("An error has occurred during deserialization.");
    }
    return saveState;
  }

  @Override
  public Message getManual() {
    return super.getManual();
  }
}
