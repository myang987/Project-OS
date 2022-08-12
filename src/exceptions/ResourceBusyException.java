package exceptions;

/**
 * This exception is thrown when trying to modify a directory or file while it
 * is currently used by another process, such as when trying to move or delete
 * an ancestral directory of the working directory.
 */
public class ResourceBusyException extends JShellException {

  public ResourceBusyException(String message) {
    super(message);
  }

  public ResourceBusyException(String message, Throwable cause) {
    super(message, cause);
  }
}
