package Ramsim.Alu;

import Ramsim.Mem.MemoryManager;
import Ramsim.Instruction.Opcode;
import Ramsim.Io.InputUnit;

public class Alu {
  private int ip_ = 0; // Instruction pointer

  // Components
  private MemoryManager memory_;
  private InputUnit input_;
  private Opcode opcode_;

  public Alu(MemoryManager memory) {
    memory_ = memory;
  }

  public void cycle() {
    fetch();
    execute();
  }

  public void fetch() {
    opcode_ = memory_.getInstruction(ip_);
    ++ip_;
  }

  public void execute() {
    // Get function pointer from opcode ID
  }

  // RAM Simulator Instruction Set
  private void load() {
    memory_.setAcc((int)opcode_.getArgument());
  }

  private void store() {
    memory_.putInRegister((int)opcode_.getArgument(), memory_.getAcc());
  }

  private void add() {
    memory_.setAcc((int)opcode_.getArgument() + memory_.getAcc());
  }

  private void sub() {
    memory_.setAcc((int)opcode_.getArgument() + memory_.getAcc());
  }

  private void read() {
    memory_.putInRegister((int)opcode_.getArgument(), input_.read());
  }
}
