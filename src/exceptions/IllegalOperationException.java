package exceptions;

public class IllegalOperationException extends JShellException {

  public IllegalOperationException(String message) {
    super(message);
  }

  public IllegalOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}
