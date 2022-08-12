package io;

import java.io.Serializable;
import java.util.Objects;

/**
 * One of the two sub-classes of {@code FolderElement}.
 * <p>
 * This class represents a simple plain text file and stores a single string as
 * its contents.
 */
public class File extends FolderElement implements Serializable {

  private static final long serialVersionUID = 2485114444758456300L;
  /**
   * A string that shows the contents of the the file.
   */
  private String contents;
  /**
   * A FileEditor dedicated to modify the contents of a file..
   */
  private final FileEditor fileEditor;

  /**
   * Constructs a new File with name {@code name} and FileEditor {@code fe}.
   *
   * @param name The name of this file
   */
  public File(String name, Directory parentDir,
      FileManager fileManager, FileEditor fileEditor) {
    this.name = name;
    this.parentDir = parentDir;
    this.contents = "";
    this.fileManager = fileManager;
    this.fileEditor = fileEditor;
  }

  /**
   * Overwrite the contents of a file.
   * <p>
   * Deletes the contents of the file, then write a new string {@code c} into
   * it.
   *
   * @param c The string to be written into the file.
   */
  public void overwriteContentsAs(String c) {
    fileEditor.modFile(this, 'o', c);
  }

  /**
   * Append the contents of the file with a new string.
   * <p>
   * Append a new string {@code c} to the end of the contents of the file.
   *
   * @param c The string that the file is going to append with.
   */
  public void appendToContents(String c) {
    fileEditor.modFile(this, 'a', c);
  }

  /**
   * @return {@code contents} of this file
   */
  public String getContents() {
    return contents;
  }

  void setContents(String contents) {
    this.contents = contents;
  }

  /**
   * Returns a copy of this file. The returned file has the same name, contents,
   * and file editor with this file, but its parent directory is undefined.
   * <p>
   * Remember to assign a parent directory to the copied file afterwards.
   *
   * @return a copy of this file but with undefined parent directory.
   */
  public File copy() {
    File copy = new File(name, null, fileManager, fileEditor);
    copy.overwriteContentsAs(contents);
    return copy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof File)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    File file = (File) o;
    return Objects.equals(contents, file.contents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), contents);
  }
}
