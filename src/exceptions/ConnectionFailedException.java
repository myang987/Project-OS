package exceptions;

public class ConnectionFailedException extends JShellException {

  public ConnectionFailedException(String message) {
    super(message);
  }

  public ConnectionFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
