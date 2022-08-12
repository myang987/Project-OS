package util;

import exceptions.DuplicateException;
import exceptions.FileNotExistException;
import exceptions.IllegalNameException;
import exceptions.NotADirectoryException;
import io.OutputHandler;
import java.util.Objects;

/**
 * This class is the standard output returned by a command. It contains
 * instructions on how output handler should handle this output.
 */
public class CmdOutput extends Message {

  /**
   * The output handler of this {@code CmdOutput} instance.
   */
  private OutputHandler outputHandler;
  /**
   * Set this to true if output handler should completely ignore this.
   */
  private boolean noOutPut;
  /**
   * The method that {@code outputHandler} should present the information to the
   * user.
   */
  private OutMode outMode;
  /**
   * The Path that tells output handler where it should store the output. This
   * field is completely ignored by output handler if {@code isRedirected} is
   * false.
   */
  private Path redirectTo;
  /**
   * This string is printed between each of the two items of this object.
   */
  private String lineSeparator;

  /**
   * This field contains an exception that is temporarily caught when the
   * command must return something before throwing it.
   */
  private Exception bufferedException;


  private CmdOutput() {
    super();
  }

  /**
   * Flushes the {@code message} in this {@code CmdOutput} with the specified
   * {@code outMode} and (if applies) location using {@code outputHandler}.
   *
   * @throws NotADirectoryException When {@code redirectTo} tries to navigate
   *                                through a file that is not at the end of it,
   *                                or when attempting to create a new file or
   *                                directory inside a file.
   * @throws DuplicateException     When attempting to create a new file or
   *                                directory in somewhere already has a file or
   *                                directory of the same name.
   * @throws IllegalNameException   When trying to create a file or directory
   *                                with illegal character in its name.
   * @throws FileNotExistException  When a path tries to navigate through a non
   *                                existing file or directory that is not at
   *                                the end of the path.
   */
  public void flush()
      throws NotADirectoryException, DuplicateException,
      IllegalNameException, FileNotExistException {
    outputHandler.handle(this);
  }

  public boolean isNoOutPut() {
    return noOutPut;
  }

  public OutMode getOutMode() {
    return outMode;
  }

  public Path getRedirectTo() {
    return redirectTo;
  }

  public String getLineSeparator() {
    return lineSeparator;
  }

  public Exception getBufferedException() {
    return bufferedException;
  }

  public void setBufferedException(Exception bufferedException) {
    this.bufferedException = bufferedException;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CmdOutput)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    CmdOutput output = (CmdOutput) o;
    return noOutPut == output.noOutPut &&
        outputHandler.equals(output.outputHandler) &&
        outMode == output.outMode &&
        Objects.equals(redirectTo, output.redirectTo) &&
        lineSeparator.equals(output.lineSeparator) &&
        Objects.equals(bufferedException, output.bufferedException);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(super.hashCode(), outputHandler, noOutPut, outMode, redirectTo,
            lineSeparator, bufferedException);
  }

  @Override
  public String toString() {
    return super.toString() + "\nCmdOutput{" +
        "noOutPut=" + noOutPut +
        ", redirected=" + outMode +
        ", redirectTo=" + redirectTo +
        ", lineSeparator='" + lineSeparator + '\'' +
        ", bufferedException=" + bufferedException +
        '}';
  }

  public static class OutputBuilder {

    /**
     * Required fields
     */
    private final OutputHandler outputHandler;

    /**
     * Optional fields
     */
    private boolean noOutPut;
    private OutMode outMode;
    private Path redirectTo;
    private String lineSeparator;
    private Exception bufferedException;

    public OutputBuilder(OutputHandler outputHandler) {
      noOutPut = false;
      outMode = OutMode.PRINT;
      redirectTo = null;
      lineSeparator = "\n";
      bufferedException = null;
      this.outputHandler = outputHandler;
    }

    public OutputBuilder isIgnored(boolean isIgnored) {
      this.noOutPut = isIgnored;
      return this;
    }

    public OutputBuilder withOutputMethod(OutMode mode) {
      this.outMode = mode;
      return this;
    }

    public OutputBuilder redirectTo(Path path) {
      this.redirectTo = path;
      return this;
    }

    public OutputBuilder withLineSeparator(String separator) {
      this.lineSeparator = separator;
      return this;
    }

    public OutputBuilder bufferException(Exception e) {
      this.bufferedException = e;
      return this;
    }

    public CmdOutput build() {
      CmdOutput output = new CmdOutput();
      output.noOutPut = noOutPut;
      output.outMode = outMode;
      output.redirectTo = redirectTo;
      output.lineSeparator = lineSeparator;
      output.bufferedException = bufferedException;
      output.outputHandler = outputHandler;
      return output;
    }

  }

  public enum OutMode {
    /**
     * Print into the console
     */
    PRINT,

    /**
     * Append {@code message} to a file, create the file if necessary.
     */
    APPEND_TO_FILE,

    /**
     * Overwrite a file with {@code message}, create the file if it does not
     * exist.
     */
    OVERWRITE_FILE
  }
}
