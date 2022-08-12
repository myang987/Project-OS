package commands;

import exceptions.JShellException;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;

public abstract class AbstractCommand {

  public abstract CmdOutput execute(Message args, OutputBuilder outputBuilder)
      throws JShellException;

  public abstract Message getManual();

  public abstract String getAlias();

}
