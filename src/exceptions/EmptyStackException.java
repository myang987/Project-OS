package exceptions;

/**
 * This exception is thrown if popd command is executed while directory stack is
 * empty.
 */
public class EmptyStackException extends JShellException {

  public EmptyStackException(String message) {
    super(message);
  }

  public EmptyStackException(String message, Throwable cause) {
    super(message, cause);
  }
}
