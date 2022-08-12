package exceptions;

/**
 * This exception is thrown when trying to create a file or directory with
 * illegal character in its name.
 */
public class IllegalNameException extends JShellException {

  public IllegalNameException(String message) {
    super(message);
  }

  public IllegalNameException(String message, Throwable cause) {
    super(message, cause);
  }

}
