package util;

import exceptions.WrongSyntaxException;

public interface FlagHandler {

  boolean populate(Flag flag, Message args) throws WrongSyntaxException;

  default WrongSyntaxException missingValue() {
    return new WrongSyntaxException("flag key detected, but no value present");
  }

}
