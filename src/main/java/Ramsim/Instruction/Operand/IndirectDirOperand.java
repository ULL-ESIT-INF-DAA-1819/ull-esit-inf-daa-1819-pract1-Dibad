package Ramsim.Instruction.Operand;

import Ramsim.Memory.Memory;

public class IndirectDirOperand<T> extends Operand<T> {
  private int index_;
  private Memory<T> memory_;

  public IndirectDirOperand(int index, Memory<T> memory) {
    index_ = index;
    memory_ = memory;
  }

  @Override
  public T getValue() {
    return memory_.get((int)memory_.get(index_));
  }

  @Override
  public String toString() {
    return String.format("*%d", index_);
  }
}
