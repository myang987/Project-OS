package util;

import exceptions.WrongSyntaxException;
import java.util.Iterator;

public class CombinedCumulativeFlagHandler implements FlagHandler {

  public CombinedCumulativeFlagHandler() {
  }

  @Override
  public boolean populate(Flag flag, Message args) throws WrongSyntaxException {
    String key = flag.getKey();
    for (Iterator<String> itr = args.iterator(); itr.hasNext(); ) {
      String section = itr.next();
      if (section.startsWith(key)) {
        if (section.length() == key.length()) {
          throw missingValue();
        }
        flag.setValue(section.substring(key.length()));
        itr.remove();
        return true;
      }
    }
    return false;
  }

}
