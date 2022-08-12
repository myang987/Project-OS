package commands;

import driver.Controller;
import exceptions.JShellException;
import exceptions.WrongSyntaxException;
import io.Directory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;
import util.Path;

public class CommSaveJShellSer extends CommSaveJShell {

  private static CommSaveJShellSer instance;

  protected CommSaveJShellSer(Controller controller,
      ArrayList<String> history, Stack<Path> pathStack) {
    super(controller, history, pathStack);
  }

  public static CommSaveJShellSer getInstance(Controller controller,
      ArrayList<String> history, Stack<Path> pathStack) {
    if (instance == null) {
      instance = new CommSaveJShellSer(controller, history, pathStack);
    }
    return instance;
  }

  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws JShellException {
    if (args.length() != 1) {
      throw new WrongSyntaxException("saveJShell: wrong number of arguments");
    }
    Directory root = controller.getRootDir();
    String workDir = controller.getWorkingDir().getPathToThis().toString();
    Stack<Path> paths = pathStack;
    ArrayList<Serializable> saveState = new ArrayList<>();
    saveState.add(root);
    saveState.add(workDir);
    saveState.add(history);
    saveState.add(paths);

    serialize(saveState, args.getFirstString());
    return builder.isIgnored(true).build();
  }

  private void serialize(ArrayList<Serializable> objects, String savePath) {
    try {
      FileOutputStream fileOut =
          new FileOutputStream(savePath);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(objects);
      out.close();
      fileOut.close();
      System.out.println("Serialized data is saved in " + savePath);
    } catch (IOException i) {
      i.printStackTrace();
      System.out.println("An error has occurred while saving work state.");
    }
  }
}
