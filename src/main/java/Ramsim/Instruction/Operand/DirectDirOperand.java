/**
  * Operator that works with registers in memory through 'direct addressing'.
  * This operator must hold a reference to the memory from where they should
  * retrieve the value.
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  *
  * @see Operand
  */
package Ramsim.Instruction.Operand;

import Ramsim.Memory.Memory;

public class DirectDirOperand<T> extends Operand<T> {
  private int index_;
  private Memory<T> memory_;

  /**
    * @param index the index in memory of the register (R0, R1, R2...)
    * @param memory reference to the memory for retrieving the value
    */
  public DirectDirOperand(int index, Memory<T> memory) {
    index_ = index;
    memory_ = memory;
  }

  @Override
  /**
    * @return the index of the register this operand points to
    */
  public int getRegisterIndex() {
    return index_;
  }

  @Override
  /**
    * @return the actual value stored in the register this operand points to
    */
  public T getValue() {
    return memory_.get(index_);
  }

  @Override
  public String toString() {
    return String.format("%d", index_);
  }
}
