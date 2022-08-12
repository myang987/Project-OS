package util;

import driver.Controller;
import exceptions.FileNotExistException;
import exceptions.NotADirectoryException;
import io.Directory;
import io.File;
import io.FileEditor;
import io.FileManager;
import io.FolderElement;
import java.util.Iterator;

/**
 * This class is a helper class that interprets any given path into a reference
 * to a File or Directory in the system.
 */
public class PathInterpreter {

  /**
   * The file system to be managed by this object
   */
  private final Controller controller;

  /**
   * Constructs a new instance with {@code controller} as the monitored file
   * system and {@code pathSeparator} as the separator of paths.
   *
   * @param controller the file system to be monitored
   */
  public PathInterpreter(Controller controller) {
    this.controller = controller;
  }

  /**
   * Returns a reference to a File or Directory targeted by {@code path}.
   * Returns null if no such FolderElement can be found.
   *
   * @param path a relative or absolute path, with each section of it separated
   *             by {@code pathSeparator}.
   * @return The File or Directory targeted by {@code path}. null if such
   * element doesn't exist.
   * @throws NotADirectoryException if there is a file in the middle of {@code
   *                                path}.
   * @throws FileNotExistException  if the file {@code path} is pointing to does
   *                                not exist.
   */
  public FolderElement toFolderElement(Path path)
      throws NotADirectoryException, FileNotExistException {
    FolderElement element;
    Iterator<String> itr = path.iterator();
    if (path.isAbsolute()) {
      element = findNextRecursive(controller.getRootDir(), itr, false);
    } else {
      element = findNextRecursive(controller.getWorkingDir(), itr, false);
    }
    return element;
  }

  /**
   * This method first tries to locate the folder element pointing by {@code
   * path} and return it. If it fails to do so, it creates a dummy folder
   * element that has the intended name and parent directory given by {@code
   * path}. The actual parent directory won't have this dummy as a child, but
   * users can access it afterwards.
   *
   * @return The File or Directory targeted by {@code path}. If such element
   * does not exist, returns a temporary directory that has its parent directory
   * and name set to the correct value, had it really been created.
   */
  public FolderElement createDummyAt(Path path)
      throws NotADirectoryException, FileNotExistException {
    FolderElement element;
    Iterator<String> itr = path.iterator();
    if (path.isAbsolute()) {
      element = findNextRecursive(controller.getRootDir(), itr, true);
    } else {
      element = findNextRecursive(controller.getWorkingDir(), itr, true);
    }
    return element;
  }

  private FolderElement interpretSymbol(FolderElement start, String symbol) {
    if (symbol.equals("")) {
      return start;
    }
    if (symbol.equals(".")) {
      return start;
    }
    if (symbol.equals("..")) {
      return start.getParentDir();
    }
    return null;
  }


  private FolderElement findNextRecursive(FolderElement start,
      Iterator<String> itr, boolean createDummy)
      throws NotADirectoryException, FileNotExistException {
    if (!itr.hasNext()) {
      return start;
    }
    if (start instanceof File) {
      throw new NotADirectoryException("path : Not a directory");
    }
    if (start == null) {
      throw new FileNotExistException("path : No such file or directory");
    }
    String nextSection = itr.next();
    FolderElement nextTarget = interpretSymbol(start, nextSection);
    if (nextTarget == null) {
      nextTarget = ((Directory) start).getElementByName(nextSection);
    }
    FolderElement nextRecursive = findNextRecursive(nextTarget, itr,
        createDummy);
    if (nextRecursive == null && createDummy) {
      nextRecursive = new Directory(nextSection, (Directory) start,
          new FileManager(new FileEditor()));
    }
    return nextRecursive;
  }
}
