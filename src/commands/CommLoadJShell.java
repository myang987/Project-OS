package commands;

import driver.Controller;
import exceptions.JShellException;
import java.util.ArrayList;
import java.util.Stack;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;
import util.PathInterpreter;

/**
 * This is an abandoned implementation of command loadJShell that utilize json
 * formatter.
 */

/**
 * Syntax: 1. loadJShell FileName
 * <p>
 * load the contents of the FileName and reinitialize everything that was saved
 * previously into the FileName.
 */
public class CommLoadJShell extends AbstractCommand {

  public static final String alias = "loadJShell";
  private static CommLoadJShell instance;

  protected Controller controller;
  protected ArrayList<String> history;
  protected PathInterpreter pathInterpreter;
  protected Stack<Path> pathStack;

  protected CommLoadJShell(Controller controller, ArrayList<String> history,
      PathInterpreter pathInterpreter, Stack<Path> pathStack) {
    this.controller = controller;
    this.history = history;
    this.pathInterpreter = pathInterpreter;
    this.pathStack = pathStack;
    controller.registerCommand(this);
  }

//  /**
//   * Returns a instance of this class. This method always return the same
//   * instance on every call.
//   *
//   * @param controller the controller that this command belongs to.
//   * @return the sole instance of this class
//   */
//  public static CommLoadJShell getInstance(Controller controller,
//      ArrayList<String> history, PathInterpreter pathInterpreter,
//      Stack<Path> pathStack) {
//    if (instance == null) {
//      instance = new CommLoadJShell(controller, commHistory, pathInterpreter,
//          pathStack);
//    }
//    return instance;
//  }

  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws JShellException {
//    if (commHistory.getHistory().size() > 1) {
//      System.out.print("You have already modified this system, please "
//          + "only load save files from a new session.");
//      return builder.isIgnored(true).build();
//    }
//    String loadPath = args.nextString();
//    if (loadPath.equals("") || !args.nextString().equals("")) {
//      throw new WrongSyntaxException("saveJShell: Wrong number of arguments");
//    }
//    JsonObject saveState;
//    try {
//      FileReader fr = new FileReader(loadPath);
//      saveState = (JsonObject) Jsoner.deserialize(fr);
//    } catch (FileNotFoundException e) {
//      System.out.print("Cannot find save file at" + loadPath);
//      return builder.isIgnored(true).build();
//    } catch (JsonException e) {
//      e.printStackTrace();
//      System.out.print("An error has occurred while parsing json file.");
//      return builder.isIgnored(true).build();
//    }
//    Directory rootDir = controller.getRootDir();
//    JsonObject contentMap = (JsonObject) saveState.get("/");
//    restoreDirectory(rootDir, contentMap);
//    restoreWorkingDir(saveState);
//    restoreHistory(saveState, loadPath);
//    restorePathStack(saveState);
//    System.out.print("Work state loaded successfully.");
    return builder.isIgnored(true).build();
  }

//  private void restoreDirectory(Directory dir, JsonObject contentMap)
//      throws DuplicateException, IllegalNameException {
//    for (String key : contentMap.keySet()) {
//      if (key.endsWith("/")) {
//        Directory newDir = dir
//            .createDirectory(key.substring(0, key.length() - 1));
//        restoreDirectory(newDir, (JsonObject) contentMap.get(key));
//      } else {
//        File newFile = dir.createFile(key);
//        newFile.overwriteContentsAs((String) contentMap.get(key));
//      }
//    }
//  }

//  private void restoreWorkingDir(JsonObject saveState)
//      throws NotADirectoryException, FileNotExistException {
//    Path pathToWorkingDir = new Path((String) saveState.get("workingDir"));
//    FolderElement fe = pathInterpreter.toFolderElement(pathToWorkingDir);
//    if (!(fe instanceof Directory)) {
//      throw new NotADirectoryException("loadJShell: Well this should not "
//          + "happen.");
//    }
//    controller.setWorkingDir((Directory) fe);
//  }

//  private void restoreHistory(JsonObject saveState, String loadingPath) {
//    ArrayList<String> history = commHistory.getHistory();
//    history.clear();
//    JsonArray savedHistory = (JsonArray) saveState.get("history");
//    for (Object o : savedHistory) {
//      if (o instanceof String) {
//        // This should always check
//        history.add((String) o);
//      }
//    }
//    history.add(alias + " " + loadingPath);
//  }

//  private void restorePathStack(JsonObject saveState) {
//    JsonArray paths = (JsonArray) saveState.get("pathStack");
//    for (Object o : paths) {
//      if (o instanceof String) {
//        // This should always check
//        Path p = new Path((String) o);
//        pathStack.push(p);
//      }
//    }
//  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. loadJShell FileName\n"
            + "\n"
            + "load the contents of the FileName\n"
            + "may be a full path. As with Unix, .. means a parent directory "
            + "and reinitialize everything that was saved previously into the FileName.\n";
    return new Message(manual);
  }

  @Override
  public String getAlias() {
    return alias;
  }
}
