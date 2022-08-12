package util;

import exceptions.WrongSyntaxException;

/**
 * The {@code Flag} class represents flag pairs in arguments. A flag consists of
 * a final {@code key} and a mutable {@code value}. It also has a {@code
 * FlagHandler} to help to scan the given argument message and populate this
 * flag.
 * <p>
 * There are two types of flags, mutex (short for mutually exclusive) flags, and
 * cumulative flags.
 * <p>
 * Mutex flags always admit the very last value in a list of arguments. For
 * example, {@code "-key 1 -key 2"} is equivalent to just {@code "-key 2"}.
 * <p>
 * Cumulative flags receive a collection of values. For example, for the
 * argument {@code "-key 1 -key 2 -key 3"}, a cumulative flag should retrieve
 * all three values {@code 1 2 3}. However, this class can only store one value
 * at a time, therefore users must maintain a collection of values outside of
 * this class by themselves.
 */
public class Flag {

  private final FlagHandler handler;

  private final String key;

  private String value;

  /**
   * Construct a new flag with identifier {@code key} and a flag handler {@code
   * handler}.
   *
   * @param key     The key of this flag.
   * @param handler The handler of this flag.
   */
  public Flag(String key, FlagHandler handler) {
    this.key = key;
    this.handler = handler;
  }

  /**
   * Extracts the values in args that fits this flag, and returns the index of
   * key in {@code args}.
   * <p>
   * This method invokes {@code handler} and set this flag's {@code value}
   * according to the type of handler this flag is assigned with. The
   * corresponding section(s) in {@code args} will be removed after this call.
   * <p>
   * Different implementation of {@code handler} will affect the behavior of
   * this method. For example, {@code DisjointMutexFlagHandler}s scan through
   * the entire {@code args} and set this flag's {@code value} to the last
   * section that fits this flag's {@code key}, but {@code
   * PositionalFlagHandler}s (not yet implemented) stops at the first encounter
   * of the key.
   *
   * @param args The argument message to be scanned.
   * @return {@code true} if a value is found and this flag has been populated
   * @throws WrongSyntaxException If flag key is present but no value is given.
   */
  public boolean extractValue(Message args) throws WrongSyntaxException {
    return handler.populate(this, args);
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  /**
   * Sets the field {@code value} to parameter {@code value}. This overwrites
   * the previously added value.
   *
   * @param value The value to be assigned to this flag.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns whether this flag has a value assigned to it.
   *
   * @return {@code true} if the field {@code value} is not {@code null}.
   */
  public boolean isFilled() {
    return value != null;
  }
}
