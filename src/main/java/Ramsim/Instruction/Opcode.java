package Ramsim.Instruction;

import java.util.ArrayList;

import Ramsim.Instruction.Operand.Operand;

public class Opcode {
  private InstructId id_;
  private final ArrayList<Operand<?>> argument_;

  public Opcode(InstructId id, ArrayList<Operand<?>> arg) {
    id_ = id;
    argument_ = arg;
  }

  public InstructId getId() {
    return id_;
  }

  public Object getValue() { // Alias
    return argument_.get(0).getValue();
  }

  public Operand<?> getArgument(int index)  {
    return argument_.get(index);
  }

  public String toString() {
    return String.format("%s%s", id_.toString(), argument_.toString());
  }
}
