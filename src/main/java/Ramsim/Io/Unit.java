/**
  * Class that provides basic structure for the interaction between files and
  * the RAM tap
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  *
  */
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

  /**
    * @return the element that is currently pointed by the head of the tape
    * (without returning it)
    */
  public abstract int peek();

  @Override
  public String toString() {
    return tape_.toString();
  }
}
