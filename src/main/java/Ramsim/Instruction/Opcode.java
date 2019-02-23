package Ramsim.Instruction;

import Ramsim.Instruction.Op.Operand;

public class Opcode {
  private InstructId id_;
  private Operand<?> argument_;

  public Opcode(InstructId id, Operand<?> arg) {
    id_ = id;
    argument_ = arg;
  }

  public InstructId getId() {
    return id_;
  }

  public Object getArgument() {
    return argument_.getValue();
  }
}
