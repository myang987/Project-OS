package io;

import exceptions.DuplicateException;
import exceptions.IllegalNameException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Creates and deletes files and directories from given directory.
 */
public class FileManager implements Serializable {

  private static final long serialVersionUID = 7519355568675272762L;
  /**
   * FileEditor assigned to newly created files.
   */
  private final FileEditor fileEditor;

  private final char[] illegalChars = {'!', '@', '#', '$', '%', '^', '&',
      '*', '(', ')', '{', '}', '~', '|', '<', '>', '?', '.', '/'};

  /**
   * Creates an FileManager that creates files and directories with {@code fe}
   * and this FileManager to be created as their modifier.
   *
   * @param fileEditor FileEditor that will be used to create new files
   */
  public FileManager(FileEditor fileEditor) {
    this.fileEditor = fileEditor;
  }

  /**
   * Creates a new file or directory under the given directory.
   * <p>
   * Creates a new file under {@code oldDir} if {@code type} is 'f', or a new
   * directory if {@code type} is 'd'. The new folder element will be named as
   * {@code name}.
   * <p>
   * Does nothing and returns an ErrorMessage if {@code type} is of any other
   * character, or if there's already a file or directory that has the same name
   * with the file or directory to be created.
   *
   * @param oldDir The directory under which a new folder element will be
   *               created.
   * @param type   The type of the folder element to be created, either 'f' for
   *               File or 'd' for Directory.
   * @param name   The name of the folder element to be created.
   * @return the newly created file or directory
   * @throws IllegalArgumentException if type is neither 'f' or 'd'.
   */
  public FolderElement createNew(Directory oldDir, char type, String name)
      throws IllegalArgumentException, DuplicateException, IllegalNameException {
    if (containsIllegalCharacters(name)) {
      throw new IllegalNameException("File name cannot contain any of the "
          + "following characters: ! @ # $ % ^ & * ( ) { } ~ | < > ?");
    }
    Collection<FolderElement> folderList = oldDir.contents;
    FolderElement newFE;
    if (type == 'd') {
      newFE = new Directory(name, oldDir, this);
    } else if (type == 'f') {
      newFE = new File(name, oldDir, this, fileEditor);
    } else {
      throw new IllegalArgumentException("Cannot create folder element of "
          + "unknown type " + type);
    }
    for (FolderElement element : folderList) {
      if (element.equals(newFE)) {
        throw new DuplicateException(name + ": File or directory already "
            + "exists");
      }
    }
    folderList.add(newFE);
    return newFE;
  }

  public void rename(FolderElement fe, String name)
      throws IllegalNameException {
    if (containsIllegalCharacters(name)) {
      throw new IllegalNameException("File name cannot contain any of the "
          + "following characters: ! @ # $ % ^ & * ( ) { } ~ | < > ?");
    }
    fe.name = name;
  }

  /**
   * @param target The folder element to be copied.
   * @return a copy of {@code target}.
   */
  public FolderElement getCopy(FolderElement target) {
    if (target instanceof Directory) {
      Directory old = (Directory) target;
      Directory copied = new Directory(old.name, this);
      for (FolderElement sub : old.contents) {
        FolderElement subCopied = sub.copy();
        subCopied.setParentDir(copied);
        try {
          copied.insertElement(subCopied);
        } catch (DuplicateException | IllegalNameException e) {
          // This should never happen.
          e.printStackTrace();
        }
      }
      return copied;
    } else {
      // This part is currently unused
      File old = (File) target;
      File copied = new File(old.name, null, this, fileEditor);
      copied.overwriteContentsAs(old.getContents());
      return copied;
    }
  }

  /**
   * Adds an element {@code fe} to the directory {@code dir}.
   *
   * @param fe  The folder element to be added to dir.
   * @param dir The target directory to add fe.
   * @throws DuplicateException   If there is another folder element in dir
   *                              named same as fe.
   * @throws IllegalNameException if the name of fe is illegal.
   */
  public void insert(FolderElement fe, Directory dir)
      throws DuplicateException, IllegalNameException {
    if (dir.contents.contains(fe)) {
      throw new DuplicateException(
          "Cannot insert element " + fe + " to " + dir + ": Duplicated name.");
    } else if (containsIllegalCharacters(fe.getName())) {
      throw new IllegalNameException("File name cannot contain any of the "
          + "following characters: ! @ # $ % ^ & * ( ) { } ~ | < > ?");
    }
    dir.contents.add(fe);
    fe.setParentDir(dir);
  }

  /**
   * Clears {@code target} and any sub directory of any depth.
   *
   * @param target The directory to be cleared.
   */
  public void clearDirectory(Directory target) {
    for (Iterator<FolderElement> itr = target.iterator(); itr.hasNext(); ) {
      FolderElement fe = itr.next();
      if (fe instanceof Directory) {
        clearDirectory((Directory) fe);
      }
      itr.remove();
    }
    target.contents.clear();
  }

  private boolean containsIllegalCharacters(String name) {
    for (char c : illegalChars) {
      if (name.indexOf(c) != -1) {
        return true;
      }
    }
    return false;
  }
}
