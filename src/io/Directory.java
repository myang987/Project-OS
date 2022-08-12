package io;

import exceptions.DuplicateException;
import exceptions.IllegalNameException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * One of the two sub-classes of {@code FolderElement}.
 * <p>
 * This class represents a directory that maintains a list of its contents. They
 * can be other directories or files.
 */
public class Directory extends FolderElement implements
    Iterable<FolderElement>, Serializable {

  private static final long serialVersionUID = 5779317454779697768L;
  /**
   * List of files and directories that are directly in this directory.
   */
  final ArrayList<FolderElement> contents;

  /**
   * Constructs a new Directory of name {@code name}, and parent Directory
   * {@code parentDir}.
   *
   * @param name      the name of this instance
   * @param parentDir the parent directory of this instance
   */
  public Directory(String name, Directory parentDir, FileManager fileManager) {
    this.contents = new ArrayList<>();
    this.name = name;
    this.parentDir = parentDir;
    this.fileManager = fileManager;
  }

  /**
   * Constructs a directory with itself as its parent directory. Such directory
   * is typically used as a root directory.
   *
   * @param name the name of this instance
   */
  public Directory(String name, FileManager fileManager) {
    this.contents = new ArrayList<>();
    this.name = name;
    this.parentDir = this;
    this.fileManager = fileManager;
  }

  /**
   * Returns whether {@code that} is an ancestor of this directory. An ancestor
   * is a directory that has this directory as a sub directory either directly
   * or non-directly.
   * <p>
   * This implementation is based on the fact that the absolute paths of the
   * ancestor must be strictly a substring of the descendant's. Back tracing is
   * inefficient, because each equals() call performs a recursive comparison.
   *
   * @return {@code true} if {@code that} is an ancestor of this directory.
   */
  public boolean isDescendantOf(FolderElement that) {
    if (that instanceof File) {
      return false;
    }
    Iterator<String> itr1 = this.getPathToThis().iterator();
    Iterator<String> itr2 = that.getPathToThis().iterator();
    while (itr1.hasNext() && itr2.hasNext()) {
      String s1 = itr1.next();
      String s2 = itr2.next();
      if (!s1.equals(s2)) {
        return false;
      }
    }
    return itr1.hasNext();
  }

  /**
   * Creates a new directory under this directory.
   * <p>
   * Creates a directory named {@code name} and add it to this instance's {@code
   * contents}. Returns an error message and abort creation if there is already
   * a directory or file of the same name.
   *
   * @param name The name of the new directory
   * @return the newly created directory
   * @throws DuplicateException if directory of same name already exists
   */
  public Directory createDirectory(String name)
      throws DuplicateException, IllegalNameException {
    return (Directory) fileManager.createNew(this, 'd', name);
  }

  /**
   * Creates a new file under this directory.
   * <p>
   * Creates a file named {@code name} and add it to this instance's {@code
   * contents}. Returns an error message and abort creation if there is already
   * a directory or file of the same name.
   *
   * @param name The name of the new file
   * @return the newly created file
   * @throws DuplicateException if file of same name already exists
   */
  public File createFile(String name)
      throws DuplicateException, IllegalNameException {
    return (File) fileManager.createNew(this, 'f', name);
  }

  /**
   * Adds a folder element {@code fe} to this directory if there's no
   * duplication, and it's name is legal.
   *
   * @param fe the folder element to be added.
   * @throws DuplicateException   if there's already another element of the same
   *                              name exists.
   * @throws IllegalNameException if the name of {@code fe} contains illegal
   *                              characters.
   */
  public void insertElement(FolderElement fe)
      throws DuplicateException, IllegalNameException {
    fileManager.insert(fe, this);
  }

  /**
   * Removes a file or directory with the name {@code name} from this
   * directory's contents array.
   * <p>
   * This method does not clear the contents of the element removed. To fully
   * delete a sub directory, call #removeAll on that directory.
   *
   * @param name the name of the element to be removed.
   */
  public void removeElement(String name) {
    for (FolderElement fe : contents) {
      if (fe.getName().equals(name)) {
        contents.remove(fe);
        break;
      }
    }
  }

  /**
   * Completely clear this directory and all sub-directories of any depth in
   * it.
   */
  public void removeAll() {
    fileManager.clearDirectory(this);
  }

  /**
   * Returns a reference to the FolderElements in this instance's contents that
   * has the name {@code name}. If no such element exists, returns null.
   *
   * @param name the name of the File or Directory to be searched
   * @return a reference to the File or Directory named {@code name}, null if it
   * doesn't exist.
   */
  public FolderElement getElementByName(String name) {
    for (FolderElement fe : contents) {
      if (fe.name.equals(name)) {
        return fe;
      }
    }
    return null;
  }

  /**
   * Returns a deep copy of this directory. The returned object has the same
   * name, contents, and file manager with this file, but its parent directory
   * is undefined.
   * <p>
   * Remember to assign a parent directory to the copied directory afterwards.
   *
   * @return a deep copy of this directory but with undefined parent directory.
   */
  @Override
  public Directory copy() {
    return deepCopy();
  }

  private Directory deepCopy() {
    return (Directory) fileManager.getCopy(this);
  }

  /**
   * For two directories to be considered equal, they must have the same {@code
   * name} and {@code contents}. Differences in {@code parentDir} do not affect
   * the equality of directories. This comparison is performed recursively,
   * which means any differences in sub directories or files of any depth WILL
   * make them not equal.
   *
   * @param o Object to be compared
   * @return {@code true} if this directory and {@code o} contains identical sub
   * directories and files.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Directory)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Directory that = (Directory) o;
    return contents.size() == that.contents.size()
        && contents.containsAll(that.contents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), contents);
  }

  @Override
  public String toString() {
    return "Directory{" +
        "name='" + name + '\'' +
        ", contents=" + contents +
        ", parentDir=" + parentDir.getName() +
        '}';
  }

  @Override
  public Iterator<FolderElement> iterator() {
    return contents.iterator();
  }

}
