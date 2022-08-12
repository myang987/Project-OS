package exceptions;

/**
 * This exception is thrown when a path tries to navigate through a file that is
 * not at the end of it, when attempting to create a new file or directory
 * inside a file, or when attempting to change directory to a file.
 */
public class NotADirectoryException extends JShellException {

  public NotADirectoryException(String message) {
    super(message);
  }

  public NotADirectoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
