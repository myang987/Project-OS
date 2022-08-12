package io;

import exceptions.IllegalNameException;
import java.io.Serializable;
import java.util.Objects;
import util.Path;

/**
 * A general designation for both {@code Directory} and {@code File}. Any thing
 * in the system should ultimately be an instance of this class.
 */
public abstract class FolderElement implements Serializable {

  private static final long serialVersionUID = 1715768898743266173L;
  /**
   * Identifier of this folder element. For a file path to be considered valid,
   * that part of it must equals to {@code name}.
   */
  protected String name;

  /**
   * Parent directory of this folder element
   */
  protected Directory parentDir;

  /**
   * A FileManager dedicated to create and delete files and directories.
   */
  protected FileManager fileManager;

  /**
   * Default constructor
   */
  public FolderElement() {
  }

  public FolderElement(String name, Directory parentDir,
      FileManager fileManager) {
    this.name = name;
    this.parentDir = parentDir;
    this.fileManager = fileManager;
  }

  /**
   * Getters and Setters of this class
   */
  public String getName() {
    return name;
  }

  void setName(String name) {
    this.name = name;
  }

  public void renameTo(String name) throws IllegalNameException {
    fileManager.rename(this, name);
  }

  /**
   * Returns the parent directory of this directory.
   *
   * @return the parent directory of this directory.
   */
  public Directory getParentDir() {
    return parentDir;
  }

  public void setParentDir(Directory parentDir) {
    this.parentDir = parentDir;
  }

  /**
   * @return An absolute path from root to this folder element.
   */
  public Path getPathToThis() {
    FolderElement current = this;
    Directory parent = this.getParentDir();
    StringBuilder sb = new StringBuilder("/");
    // Parent of root is root.
    while (parent != current) {
      sb.insert(0, "/" + current.getName());
      current = parent;
      parent = parent.getParentDir();
    }
    return new Path(sb.toString());
  }

  public abstract FolderElement copy();

  /**
   * Returns {@code true} iff {@code o} is a FolderElement, and it shares {@code
   * name} with this instance.
   *
   * @param o Object to be compared
   * @return {@code true} if this instance and o have the same {@code name}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FolderElement)) {
      return false;
    }
    FolderElement that = (FolderElement) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  /**
   * @return {@code name} of this instance
   */
  @Override
  public String toString() {
    return name;
  }
}
