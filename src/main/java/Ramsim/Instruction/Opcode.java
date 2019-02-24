package Ramsim.Instruction;

import java.util.ArrayList;

import Ramsim.Instruction.Operand.Operand;

public class Opcode {
  private InstructId id_;
  private final ArrayList<Operand<?>> argument_;

  private int lineNum_;

  public Opcode(InstructId id, ArrayList<Operand<?>> arg) {
    this(id, arg, -1);
  }

  public Opcode(InstructId id, ArrayList<Operand<?>> arg, int lineNum) {
    id_ = id;
    argument_ = arg;
    lineNum_ = lineNum;
  }

  public InstructId getId() {
    return id_;
  }

  public int getLineNum() {
    return lineNum_;
  }

  public Object getValue() { // Alias for 1st argument
    return argument_.get(0).getValue();
  }

  public int getRegisterIndex() { // Alias for 1st argument
    return argument_.get(0).getRegisterIndex();
  }

  public String toString() {
    return String.format("%s%s", id_.toString(), argument_.toString());
  }
}
