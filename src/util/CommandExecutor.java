package util;

import commands.AbstractCommand;
import exceptions.JShellException;
import io.OutputHandler;
import util.CmdOutput.OutMode;
import util.CmdOutput.OutputBuilder;

/**
 * This class represents a preprocessor of all commands. It cleanses the common
 * arguments sections of all commands before passing the argument message to the
 * command instance, thus makes the implementation of each command simpler.
 * <p>
 * This implementation checks and strips any redirection flags and preset the
 * {@code OutputBuilder} so that it would already had the correct redirection
 * information before being passed to the commands instance.
 */
public class CommandExecutor implements IAcceptsFlags {

  private final OutputHandler outputHandler;

  public CommandExecutor(OutputHandler outputHandler) {
    this.outputHandler = outputHandler;
  }

  /**
   * Scans {@code args} for redirection flags' key and value and removes the
   * read sections in {@code args}. It also constructs and sets the redirection
   * information of a {@code CmdOutput.OutputBuilder} and pass it to the command
   * instance.
   *
   * @param command The command to be invoked.
   * @param args    The unprocessed argument {@code Message}.
   * @throws Exception When an exception occurs inside of the command
   *                   execution.
   */
  public void invoke(AbstractCommand command, Message args)
      throws Exception {
    args.removeFirstString();
    OutputBuilder builder = new OutputBuilder(outputHandler);
    Flag[] flags = constructFlags();
    for (Flag flag : flags) {
      if (flag.extractValue(args)) {
        builder.redirectTo(new Path(flag.getValue()));
        switch (flag.getKey()) {
          case (">"):
            builder.withOutputMethod(OutMode.OVERWRITE_FILE);
            break;
          case (">>"):
            builder.withOutputMethod(OutMode.APPEND_TO_FILE);
            break;
          default:
            // This should never happen.
            throw new JShellException("Flag error during command execution "
                + "preparation.");
        }
      }
    }
    CmdOutput output = command.execute(args, builder);
    output.flush();
    Exception e = output.getBufferedException();
    if (e != null) {
      throw e;
    }
  }

  @Override
  public Flag[] constructFlags() {
    return new Flag[]{
        new Flag(">", new DisjointMutexFlagHandler()),
        new Flag(">>", new DisjointMutexFlagHandler())
    };
  }

}
