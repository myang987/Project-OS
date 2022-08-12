package exceptions;

/**
 * This exception is thrown when a path tries to navigate through a non existing
 * file or directory that is not at the end of the path.
 */
public class FileNotExistException extends JShellException {

  public FileNotExistException(String message) {
    super(message);
  }

  public FileNotExistException(String message, Throwable cause) {
    super(message, cause);
  }
}
