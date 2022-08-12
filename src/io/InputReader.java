package io;

import java.util.Scanner;
import util.Message;

/**
 * This class is a helper class that is dedicated to reading input from the
 * user.
 */
public class InputReader {

  private final Scanner sc = new Scanner(System.in);
  private final InputParser inputParser;


  /**
   * Constructs a new InputReader with default InputParser {@code ip} and
   * default outputHandler {@code pr}.
   *
   * @param inputParser InputParser that refactors the plain text.
   */
  public InputReader(InputParser inputParser) {
    this.inputParser = inputParser;
  }

  /**
   * This method gets user input without printing anything, and records input to
   * the history list in commHistory.
   *
   * @return the parsed message from user input
   */
  public Message getUserInput() {
    String input = sc.nextLine();
    return inputParser.parse(input);
  }

  /**
   * Prints {@code prompt}, then get and return the parsed command line input.
   *
   * @param prompt the string to be printed before user entering
   * @return the parsed message from user input
   */
  public Message getUserInput(String prompt) {
    System.out.print(prompt);
    return getUserInput();
  }

}
