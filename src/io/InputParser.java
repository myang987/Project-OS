package io;

import util.Message;

/**
 * This class Transfers the String input from user into a message of class
 * Message.
 */

public class InputParser {

  /**
   * Transfers the input of user of type string into a message of class
   * Message.
   * <p>
   * Splits the input {@code str} by whitespaces and transfer the split string[]
   * into a Message.
   *
   * @param str The input string to be transferred
   * @return A Message separating {@code str} by whitespaces as its entries
   */
  public Message parse(String str) {
    Message message = new Message();
    boolean duringSection = false;
    boolean duringString = false;
    StringBuilder sb = new StringBuilder();
    for (char c : str.toCharArray()) {
      if (c != ' ' || duringString) {
        duringSection = true;
        if (c == '\"') {
          duringString = !duringString;
        }
        sb.append(c);
      } else if (duringSection) {
        duringSection = false;
        message.add(sb.toString());
        sb = new StringBuilder();
      }
    }
    if (sb.length() > 0) {
      message.add(sb.toString());
    }
//		System.out.println(message);
    return message;
  }
}	  

