package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class represents a list of strings that are convenient to read one by
 * one.
 */
public class Message implements Iterable<String> {

  /**
   * An ArrayList that contains a list of Strings as this instance's contents
   */
  protected final ArrayList<String> messages;

  public Message() {
    messages = new ArrayList<>();
  }

  /**
   * Initializes this message with only one String. {@code str} is added only if
   * it is neither null nor empty, otherwise it is equivalent to calling the
   * default constructor.
   *
   * @param str A string to be inserted to this message
   */
  public Message(String str) {
    this();
    if (str != null && !str.equals("")) {
      this.messages.add(str);
    }
  }

  /**
   * Initializes this message with an array of Strings {@code in}, each element
   * that is not null nor empty string in {@code in} will be added to this
   * message.
   *
   * @param in An array of Strings to be inserted to this message
   */
  public Message(String[] in) {
    this.messages = new ArrayList<>();
    for (String str : in) {
      if (str != null && !str.equals("")) {
        this.messages.add(str);
      }
    }
  }

  /**
   * Add another line of string to the end of this Message if {@code str} is not
   * null, does nothing otherwise.
   *
   * @param str the new string to be added
   */
  public void add(String str) {
    if (str != null) {
      messages.add(str);
    }
  }

  public String getFirstString() {
    return messages.size() > 0 ? messages.get(0) : "";
  }

  public void removeFirstString() {
    messages.remove(0);
  }

  public int length() {
    return messages.size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Message)) {
      return false;
    }
    Message message = (Message) o;
    return messages.equals(message.messages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messages);
  }

  /**
   * Returns a string that contains all strings in {@code messages}, one in a
   * line. Those strings with no newline characters at the end are appended by
   * one before returning.
   *
   * @return One string containing all strings in {@code messages}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String str : messages) {
      sb.append(str).append(" ");
//      sb.append("][");    for debug
    }
    if (messages.size() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  @Override
  public Iterator<String> iterator() {
    return messages.iterator();
  }
}
