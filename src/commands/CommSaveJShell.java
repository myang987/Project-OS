package commands;

import driver.Controller;
import exceptions.JShellException;
import java.util.ArrayList;
import java.util.Stack;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;

/**
 * This is an abandoned implementation of command saveJShell that utilize json
 * formatter.
 */

/**
 * Syntax: 1. saveJShell FileName
 * <p>
 * save the current JShell status into the FileName.
 */
public class CommSaveJShell extends AbstractCommand {

  public static final String alias = "saveJShell";
  private static CommSaveJShell instance;

  protected Controller controller;

  protected ArrayList<String> history;

  protected Stack<Path> pathStack;

  protected CommSaveJShell(Controller controller, ArrayList<String> history,
      Stack<Path> pathStack) {
    this.controller = controller;
    this.history = history;
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
//  public static CommSaveJShell getInstance(Controller controller,
//      CommHistory commHistory,
//      Stack<Path> pathStack) {
//    if (instance == null) {
//      instance = new CommSaveJShell(controller, commHistory, pathStack);
//    }
//    return instance;
//  }

  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws JShellException {
//    String savePath = args.nextString();
//    if (savePath.equals("") || !args.nextString().equals("")) {
//      throw new WrongSyntaxException("saveJShell: Wrong number of arguments");
//    }
//    JsonObject saveState = buildJsonSaveFile();
//    try {
//      java.io.File saveFile = new java.io.File(savePath);
//      saveFile.createNewFile();
//      FileWriter fw = new FileWriter(savePath, false);
//      fw.write(saveState.toJson());
//      fw.close();
//      System.out.print("Work state saved to " + savePath);
//    } catch (IOException e) {
//      e.printStackTrace();
//      System.out.println("An error has occurred while creating save file.");
//    }
    return builder.isIgnored(true).build();
  }

//  private JsonObject buildJsonSaveFile() {
//    JsonObject saveJson = new JsonObject();
//    saveJson.put("/", createDirectoryJsonObject(controller.getRootDir()));
//    saveJson.put("workingDir",
//        controller.getWorkingDir().getPathToThis().toString());
//
//    JsonArray histories = new JsonArray();
//    histories.addAll(commHistory.getHistory());
//    saveJson.put("history", histories);
//
//    JsonArray paths = new JsonArray();
//    for (Path p : pathStack) {
//      paths.add(p.toString());
//    }
//    saveJson.put("pathStack", paths);
//    return saveJson;
//  }

//  /**
//   * Returns a json array representing {@code dir}. Each element in {@code dir}
//   * is an json object of the format {name: {@code contents}}. For directories,
//   * the {@code contents} is another json array; for files, it is the text in
//   * it.
//   *
//   * @param dir the directory to be written into a json array
//   * @return a json array representing {@code dir}
//   */
//  private JsonObject createDirectoryJsonObject(Directory dir) {
//    JsonObject jsonObject = new JsonObject();
//    Message contentsMessage = dir.getContentsAsMessage(false);
//    String subElementName = contentsMessage.nextString();
//    while (!subElementName.equals("")) {
//      FolderElement fe = dir.getElementOfName(subElementName);
//      if (fe instanceof Directory) {
//        jsonObject
//            .put(subElementName + "/",
//                createDirectoryJsonObject((Directory) fe));
//      } else {
//        jsonObject.put(subElementName, ((File) fe).getContents());
//      }
//      subElementName = contentsMessage.nextString();
//    }
//    return jsonObject;
//  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. saveJShell FileName"
            + "\n"
            + "save the current JShell status into the FileName.\n";
    return new Message(manual);
  }

  @Override
  public String getAlias() {
    return alias;
  }
}
