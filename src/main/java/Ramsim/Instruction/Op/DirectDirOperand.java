package Ramsim.Instruction.Op;

import Ramsim.Mem.Memory;


public class DirectDirOperand<T> extends Operand<T> {
  private int index_;
  private Memory<T> memory_;

  public DirectDirOperand(int index, Memory<T> memory) {
    index_ = index;
    memory_ = memory;
  }

  @Override
  public T getValue() {
    return memory_.get(index_);
  }
}
