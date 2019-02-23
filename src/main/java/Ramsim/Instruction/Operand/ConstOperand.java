package Ramsim.Instruction.Operand;

public class ConstOperand<T> extends Operand<T> {
  private T constValue_;

  public ConstOperand(T constValue) {
    constValue_ = constValue;
  }

  @Override
  public T getValue() {
    return constValue_;
  }

  @Override
  public String toString() {
    return constValue_.toString();
  }
}
