package Ramsim.Memory;

import java.util.Hashtable;

public class Memory<T> {
  private Hashtable<Integer, T> data_ = new Hashtable<Integer, T>();

  public T get(int index) throws IllegalArgumentException {
    T value = data_.get(index);

    if (value == null)
      throw new IllegalArgumentException("Trying to acces a null register!");

    return value;
  }

  public void put(int index, T value) {
    data_.put(index, value);
  }
}
