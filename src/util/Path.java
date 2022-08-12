package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;

/**
 * This class represents a path to a file or directory in the file system. It
 * reformats a string so that the first "/" is stripped, and the last optional
 * "/" is added. Whether the original string represents an absolute path is
 * maintained in a field separated from the actual text. It also has some useful
 * methods to retrieve frequently used sections of paths without the need to
 * deal with raw strings.
 */
public class Path implements Serializable, Iterable<String> {

  private static final long serialVersionUID = -805060300750773716L;
  /**
   * The text form of this Path. This String always has it leading {@code
   * SEPARATOR} stripped. If it does not have a trailing {@code SEPARATOR}, the
   * constructor automatically append one for it.
   * <p>
   * To determine if this is an absolute path, use {@code isAbsolute()}.
   */
  private final String text;
  /**
   * This String separates different sections of this Path. For a Path to be
   * considered an absolute path, its leading characters must equals this
   * String.
   */
  private final String SEPARATOR;
  /**
   * Whether this Path is an absolute Path.
   */
  private final boolean isAbsolute;

  /**
   * The default constructor
   */
  protected Path() {
    text = "";
    SEPARATOR = "";
    isAbsolute = false;
  }

  /**
   * Constructs a new Path, with path separator set to "/".
   *
   * @param text a path in plain text.
   */
  public Path(String text) {
    this(text, "/");
  }

  /**
   * Constructs a new Path.
   * <p>
   * This method reformats a string so that the first path separator (which
   * indicates the absoluteness of this path) is stripped, and the last optional
   * separator is added. Additionally, any consecutive separator is replaced
   * with only one of it, any "." section is omitted, and any ".." is cancelled
   * out with the previous section if possible. Whether the original string
   * represents an absolute path is maintained in a field separated from the
   * actual text.
   *
   * @param plainPath a path in plain text
   * @param SEPARATOR the String that separates {@code path} into different
   *                  names of directories and files.
   */
  public Path(String plainPath, String SEPARATOR) {
    Path temp = new Path(plainPath + SEPARATOR, SEPARATOR,
        plainPath.startsWith(SEPARATOR));
    ArrayList<String> sections = new ArrayList<>();
    Stack<Integer> namesAt = new Stack<>();
    // last is the index of the last proper name section
    int last = -1;
    for (String str : temp) {
      if (last >= 0 && str.equals("..")) {
        // Cancellation
        sections.remove(last);
        last = namesAt.pop();
      } else if (!str.equals("") && !str.equals(".")) {
        sections.add(str + SEPARATOR);
        if (!str.equals("..")) {
          // In this case, str is a proper name
          namesAt.push(last);
          last = sections.size() - 1;
        }
      }
    }
    StringBuilder sb = new StringBuilder();
    for (String str : sections) {
      sb.append(str);
    }
    this.text = (sb.length() == 0 ? "/" : sb.toString());
    this.isAbsolute = temp.isAbsolute;
    this.SEPARATOR = SEPARATOR;
  }

  /**
   * A private constructor, mainly used to test edge cases.
   *
   * @param text       a path in plain text
   * @param SEPARATOR  the String that separates {@code path} into different
   *                   names of directories and files.
   * @param isAbsolute whether this path should be absolute.
   */
  private Path(String text, String SEPARATOR, boolean isAbsolute) {
    this.text = text;
    this.SEPARATOR = SEPARATOR;
    this.isAbsolute = isAbsolute;
  }

  /**
   * Returns true if the original String path starts with SEPARATOR. That is,
   * the original String path is an absolute path.
   *
   * @return true if this Path is an absolute path, false otherwise.
   */
  public boolean isAbsolute() {
    return isAbsolute;
  }

  /**
   * Returns the String form of this Path.
   * <p>
   * Do not use this form as to test if this Path is absolute or not. Use {@code
   * isAbsolute()} instead.
   *
   * @return the plain text form of this Path.
   */
  public String getText() {
    return text;
  }

  /**
   * This method is used to test if this path is targeting at the root
   * directory.
   *
   * @return true if this path is created by a string that equals to {@code
   * SEPARATOR}, false otherwise.
   */
  private boolean isRootPath() {
    return this.equals(new Path("/", SEPARATOR, true));
  }

  /**
   * Returns true if this equals to {@code o}.
   * <p>
   * Two paths are equal if they are constructed by the same string, with two
   * exceptions: the difference in separator is not considered, and the trailing
   * separator is optional. The following strings generate paths equal to each
   * other:
   * <ul>
   *   <li>"com~utsc~lib"</li>
   *   <li>"com/utsc/lib"</li>
   *   <li>"com/utsc/lib/"</li>
   * </ul>
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Path)) {
      return false;
    }
    Path path1 = (Path) o;
    return isAbsolute == path1.isAbsolute &&
        text.equals(path1.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, isAbsolute);
  }

  /**
   * Returns the string representation of this path. It is assured that
   * constructing a new path with the returned string will result in a new path
   * that is equal to this path.
   *
   * @return the string representation of this path
   */
  @Override
  public String toString() {
    return isAbsolute ? (isRootPath() ? text : SEPARATOR + text) : text;
  }

  /**
   * Returns an iterator for iteration.
   *
   * @return A new iterator for this path.
   */
  @Override
  public Iterator<String> iterator() {
    return new PathIterator();
  }

  /**
   * This implementation skips the {@code SEPARATOR} in {@code text} of the
   * path.
   */
  private class PathIterator implements Iterator<String> {

    private int index;

    public PathIterator() {
      index = 0;
    }

    @Override
    public boolean hasNext() {
      return index < text.length() - SEPARATOR.length();
    }

    @Override
    public String next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      int start = index;
      int end = start + text.substring(index).indexOf(SEPARATOR);
      if (end < start) {
        end = text.length();
      }
      String nextSection = text.substring(start, end);
      index = end + SEPARATOR.length();
      if (index > text.length()) {
        index = text.length();
      }
      return nextSection;
    }
  }
}
