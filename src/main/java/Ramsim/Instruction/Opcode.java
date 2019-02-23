package Ramsim.Instruction;

import Ramsim.Instruction.Op.Operand;

public class Opcode {
  private int identifier_;
  private Operand<?> argument_;

  public Opcode(int id, Operand<?> arg) {
    identifier_ = id;
    argument_ = arg;
  }

  public int getId() {
    return identifier_;
  }

  public Object getArgument() {
    return argument_.getValue();
  }
}
