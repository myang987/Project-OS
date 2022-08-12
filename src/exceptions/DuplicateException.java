package exceptions;

/**
 * This exception is thrown when attempting to create a new file or directory in
 * somewhere already has a file or directory of the same name.
 */
public class DuplicateException extends JShellException {

  public DuplicateException(String message) {
    super(message);
  }

  public DuplicateException(String message, Throwable cause) {
    super(message, cause);
  }
}
