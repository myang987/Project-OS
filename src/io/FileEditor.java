package io;

import java.io.Serializable;

/**
 * This class modifies the contents of a given file.
 */
public class FileEditor implements Serializable {

  private static final long serialVersionUID = -743313815130185422L;

  void modFile(File f, char mode, String str) throws IllegalArgumentException {
    if (mode == 'o') {
      f.setContents(str);
    } else if (mode == 'a') {
      f.setContents((f.getContents() == null ? "" : f.getContents()) + str);
    } else {
      throw new IllegalArgumentException("Unknown operation: " + mode);
    }
  }

}