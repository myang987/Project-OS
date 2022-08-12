package util;

import exceptions.WrongSyntaxException;
import java.util.Iterator;

/**
 * This class is an implementation of {@code FlagHandler} that matches the
 * behavior of a mutex flag, particularly, a disjoint flag, which means the key
 * and the value are separated by a whitespace in the plain text argument,
 * therefore consequently are in different entries in argument {@code Message}.
 */
public class DisjointMutexFlagHandler implements FlagHandler {

  public DisjointMutexFlagHandler() {
  }

  /**
   * Populates {@code flag} with {@code args}, and delete the processed sections
   * in {@code args}.
   * <p>
   * This flag handler iterates through {@code args} and set the {@code value}
   * of {@code flag} to the very last occurrence in {@code args}. Each matching
   * pair of {@code key} and {@code value} is removed during the iteration.
   *
   * @param flag The flag to be populated.
   * @param args The list of arguments to be scanned.
   * @return {@code true} if {@code flag} is modified, and at least one pair of
   * {@code key} and {@code value} is removed.
   * @throws WrongSyntaxException If flag key is present but no value is given.
   */
  @Override
  public boolean populate(Flag flag, Message args) throws WrongSyntaxException {
    Iterator<String> itr = args.iterator();
    boolean found = false;
    while (itr.hasNext()) {
      String str = itr.next();
      if (str.equals(flag.getKey())) {
        itr.remove();
        if (itr.hasNext()) {
          flag.setValue(itr.next());
          itr.remove();
          found = true;
        } else {
          throw missingValue();
        }
      }
    }
    return found;
  }

}
