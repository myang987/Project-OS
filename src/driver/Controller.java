package driver;

import commands.AbstractCommand;
import commands.CommCat;
import commands.CommCd;
import commands.CommCp;
import commands.CommCurl;
import commands.CommEcho;
import commands.CommExit;
import commands.CommHistory;
import commands.CommLoadJShellSer;
import commands.CommLs;
import commands.CommMan;
import commands.CommMkdir;
import commands.CommMv;
import commands.CommPopd;
import commands.CommPushd;
import commands.CommPwd;
import commands.CommRm;
import commands.CommSaveJShellSer;
import commands.CommSearch;
import commands.CommTree;
import exceptions.JShellException;
import io.Directory;
import io.FileEditor;
import io.FileManager;
import io.InputParser;
import io.InputReader;
import io.OutputHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import util.CommandExecutor;
import util.ErrorHandler;
import util.Message;
import util.Path;
import util.PathInterpreter;

public class Controller {

  /**
   * The default helper objects of this Controller.
   */
  private InputParser defaultInputParser;
  private OutputHandler defaultOutputHandler;
  private InputReader inputReader;
  private ErrorHandler errorHandler;
  private FileEditor defaultFileEditor;
  private FileManager defaultFileManager;
  private PathInterpreter pathInterpreter;
  private CommandExecutor defaultCommandExecutor;

  /**
   * The HashMap that maps a string to the correspond AbstractCommand object.
   */
  private final HashMap<String, AbstractCommand> commandsHashMap = new HashMap<>();
  private final ArrayList<String> histories = new ArrayList<>();

  /**
   * The root directory of this Controller.
   */
  private Directory rootDir;

  /**
   * The working directory of this Controller. To change this field, one must
   * use the method {@code setPathToWorkingDir} with a valid path to a new
   * directory.
   */
  private Directory workingDir;

  private boolean isRunning = false;
  private boolean isModified = false;

  /**
   * The sole constructor.
   */
  public Controller() {
  }

  /**
   * Setups this Controller so that the helper objects and {@code
   * commandsHashMap} are initialized.
   *
   * @return 0 if successful, 1 otherwise.
   */
  int setup() {

    // Setup the helper instances
    defaultInputParser = new InputParser();
    pathInterpreter = new PathInterpreter(this);
    errorHandler = new ErrorHandler();
    defaultFileEditor = new FileEditor();
    defaultFileManager = new FileManager(defaultFileEditor);
    defaultOutputHandler = new OutputHandler(pathInterpreter);
    inputReader = new InputReader(defaultInputParser);
    defaultCommandExecutor = new CommandExecutor(defaultOutputHandler);

    // Setup commands
    CommCat.getInstance(this);
    CommCd.getInstance(this);
    CommEcho.getInstance(this);
    CommExit.getInstance(this);
    CommLs.getInstance(this);
    CommMkdir.getInstance(this);
    CommPwd.getInstance(this);

    // These three share one stack to store paths on stack
    Stack<Path> pathStack = new Stack<>();
    CommPopd.getInstance(this, pathStack);
    CommPushd.getInstance(this, pathStack);

    CommMv.getInstance(this);
    CommCp.getInstance(this);
    CommRm.getInstance(this);
    CommTree.getInstance(this);
    CommSearch.getInstance(this);
    CommCurl.getInstance(this);

    // These three share one array list to store history
    CommHistory.getInstance(this, histories);
    CommSaveJShellSer.getInstance(this, histories, pathStack);
    CommLoadJShellSer.getInstance(this, histories, pathInterpreter, pathStack);

    CommMan.getInstance(this, commandsHashMap);

    // Setup the root directory
    rootDir = new Directory("/", defaultFileManager);
    workingDir = rootDir;

    return 0;
  }

  /**
   * Registers {@code command} to commandsHashMap.
   *
   * @param command a command to be registered
   */
  public void registerCommand(AbstractCommand command) {
    this.commandsHashMap.put(command.getAlias(), command);
  }

  /**
   * Starts this Controller.
   *
   * @return 0 if this Controller quits normally, 1 otherwise.
   */
  int run() {
    isRunning = true;
    while (isRunning) {
      Message userInput = inputReader.getUserInput("/#: ");
      histories.add(userInput.toString());
      String commandName = userInput.getFirstString();
      AbstractCommand commandInvoked = commandsHashMap.get(commandName);
      if (commandInvoked == null) {
        System.out.println("Command not found.");
      } else {
        try {
          defaultCommandExecutor.invoke(commandInvoked, userInput);
          if (commandName.equals("exit")) {
            isRunning = false;
          }
        } catch (JShellException jse) {
          errorHandler.resolve(jse);
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println();
        }
        isModified = true;
      }
    }
    return 0;
  }

  int shut() {
    return 0;
  }

  public Directory getRootDir() {
    return rootDir;
  }

  public Directory getWorkingDir() {
    return workingDir;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public boolean isModified() {
    return isModified;
  }

  public InputParser getDefaultInputParser() {
    return defaultInputParser;
  }

  public OutputHandler getPrinter() {
    return defaultOutputHandler;
  }

  public InputReader getInputReader() {
    return inputReader;
  }

  public ErrorHandler getErrorHandler() {
    return errorHandler;
  }

  public FileEditor getDefaultFileEditor() {
    return defaultFileEditor;
  }

  public FileManager getDefaultFileManager() {
    return defaultFileManager;
  }

  public PathInterpreter getPathInterpreter() {
    return pathInterpreter;
  }

  public void setWorkingDir(Directory workingDir) {
    this.workingDir = workingDir;
  }

  public void setRootDir(Directory rootDir) {
    this.rootDir = rootDir;
  }
}
