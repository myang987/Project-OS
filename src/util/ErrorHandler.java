package util;

import exceptions.JShellException;

public class ErrorHandler {

  public void resolve(JShellException jse) {
    System.err.println("JShell: " + jse);
  }

}
