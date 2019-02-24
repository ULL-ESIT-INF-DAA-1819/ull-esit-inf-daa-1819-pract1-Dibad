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

  public void clear() {
    data_.clear();
  }

  public String toString() {
    String output = "";
    for (var k : data_.entrySet()) {
      output = output.concat(String.format("[R%d]=%d, ", k.getKey(), k.getValue()));
    }
    return output;
  }
}
