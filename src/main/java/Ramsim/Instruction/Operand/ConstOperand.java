package Ramsim.Instruction.Operand;

public class ConstOperand<T> extends Operand<T> {
  private T constValue_;

  public ConstOperand(T constValue) {
    constValue_ = constValue;
  }

  @Override
  public int getRegisterIndex() {
    throw new IllegalArgumentException("Illegal operation!");
  }

  @Override
  public T getValue() {
    return constValue_;
  }

  @Override
  public String toString() {
    return String.format("= %s", constValue_.toString());
  }
}
