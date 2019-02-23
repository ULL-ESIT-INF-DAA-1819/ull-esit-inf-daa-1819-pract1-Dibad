package Ramsim;

import java.util.Hashtable;

public class Memory<T> {

  private Hashtable<Integer, T> data_ = new Hashtable<Integer, T>();

  public T get(int index) { return data_.get(index); }
  public void put(int index, T value) { data_.put(index, value); }
}
