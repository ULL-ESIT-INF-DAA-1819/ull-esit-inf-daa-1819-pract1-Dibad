package Ramsim.Memory;

import java.util.HashMap;

public class Memory<T> {
  private HashMap<Integer, T> data_ = new HashMap<Integer, T>();

  public T get(int index) throws IllegalArgumentException {
    T value = data_.get(index);

    if (value == null)
      throw new IllegalArgumentException("Trying to acces a null register!");

    return value;
  }

  public void put(int index, T value) {
    data_.put(index, value);
  }

  public String toString() {
    return data_.toString();
  }
}
