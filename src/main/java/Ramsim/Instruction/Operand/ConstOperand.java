/**
  * Operator that works with constants ('1 - numeric', 'tag - string', ...)
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  *
  * @see Operand
  */
package Ramsim.Instruction.Operand;

public class ConstOperand<T> extends Operand<T> {
  private T constValue_;

  public ConstOperand(T constValue) {
    constValue_ = constValue;
  }

  /** @return Constants don't point to any register, so an exception is raised.
    */
  @Override
  public int getRegisterIndex() {
    throw new IllegalArgumentException(
      "Constant Operands doesn't have a register index!!");
  }

  /** @return The value assign to the constant operator
    */
  @Override
  public T getValue() {
    return constValue_;
  }

  @Override
  public String toString() {
    return String.format("= %s", constValue_.toString());
  }
}
