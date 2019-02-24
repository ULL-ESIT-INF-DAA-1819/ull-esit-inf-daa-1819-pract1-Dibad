package Ramsim.Instruction.Operand;

public abstract class Operand<T> {
  public abstract int getIndex();
  public abstract T getValue();
  public abstract String toString();
}
