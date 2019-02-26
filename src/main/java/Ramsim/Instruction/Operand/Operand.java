package Ramsim.Instruction.Operand;

/**
  * This class provides an abstraction layer for operands.
  * Instructions don't need to know how operands work, just the value they
  * return.
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  */
public abstract class Operand<T> {

  public abstract int getRegisterIndex();
  public abstract T getValue();

  public abstract String toString();
}
