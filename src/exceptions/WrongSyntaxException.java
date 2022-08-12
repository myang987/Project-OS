package exceptions;

/**
 * This exceptions is thrown when unexpected arguments are received from the
 * user. It can be a wrong number of arguments, or one argument in a wrong
 * format.
 */
public class WrongSyntaxException extends JShellException {

  public WrongSyntaxException(String message) {
    super(message);
  }

  public WrongSyntaxException(String message, Throwable cause) {
    super(message, cause);
  }
}
