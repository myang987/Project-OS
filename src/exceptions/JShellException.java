package exceptions;

/**
 * This exception is a super class of all the possible exceptions thrown by
 * commands and helper classes in JShell to distinguish them from other Java
 * exceptions.
 */
public class JShellException extends Exception {

  public JShellException(String message) {
    super(message);
  }

  public JShellException(String message, Throwable cause) {
    super(message, cause);
  }
}
