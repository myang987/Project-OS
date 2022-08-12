package test.commands;

import commands.AbstractCommand;
import exceptions.JShellException;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;

public class MockEmptyCommand extends AbstractCommand {

  @Override
  public CmdOutput execute(Message args, OutputBuilder outputBuilder)
      throws JShellException {
    return outputBuilder.build();
  }

  @Override
  public Message getManual() {
    return null;
  }

  @Override
  public String getAlias() {
    return null;
  }
}
