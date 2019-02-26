/**
  * Provides a simpler interface for using a HashMap as some sort of "memory".
  * It's indexed by Integers by default
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  *
  */
package Ramsim.Memory;

import java.util.HashMap;

public class Memory<T> {
  private HashMap<Integer, T> data_ = new HashMap<Integer, T>();

  /**
    * Get the value for the key in the HashMap corresponding with the index.
    * IMPORTANT: If the key doesn't exists, it will THROW an exception!
    */
  public T get(int index) throws IllegalArgumentException {
    T value = data_.get(index);

    if (value == null)
      throw new IllegalArgumentException("Trying to acces a null register!");

    return value;
  }

  /**
    * Stores the (key, value) pair in memory
    */
  public void put(int index, T value) {
    data_.put(index, value);
  }

  /**
    * Deletes all entries from the HashMap
    */
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
