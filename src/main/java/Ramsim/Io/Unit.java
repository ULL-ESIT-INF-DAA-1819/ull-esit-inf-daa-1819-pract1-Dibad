package Ramsim.Io;

import java.util.ArrayList;

public abstract class Unit {
  protected ArrayList<Integer> tape_ = new ArrayList<Integer>();

  protected String filepath_;

  public Unit(String filepath) {
    filepath_ = filepath;
  }

  public void changeFilepath(String filepath) {
    filepath_ = filepath;
  }

  @Override
  public String toString() {
    return tape_.toString();
  }
}
