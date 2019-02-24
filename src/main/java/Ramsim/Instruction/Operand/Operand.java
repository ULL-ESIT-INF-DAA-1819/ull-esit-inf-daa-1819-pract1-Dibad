package Ramsim.Instruction.Operand;

public abstract class Operand<T> {

  public abstract int getRegisterIndex();
  public abstract T getValue();

  public abstract String toString();
}
