package Ramsim.Alu;

import java.util.NoSuchElementException;

import Ramsim.Mem.MemoryManager;
import Ramsim.Instruction.Opcode;
import Ramsim.Io.InputUnit;
import Ramsim.Io.OutputUnit;

public class Alu {
  private int ip_ = 0; // Instruction pointer

  // Components
  private MemoryManager memory_;
  private InputUnit input_;
  private OutputUnit output_;
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
    switch(opcode_.getId()) {
      case 0:
        load();
        break;

      case 1:
        store();
        break;

      case 2:
        add();
        break;

      case 3:
        sub();
        break;

      case 4:
        // mul();
        break;

      case 5:
        // div();
        break;

      case 6:
        read();
        break;

      case 7:
        write();
        break;

      case 8:
        //jump();
        break;

      case 9:
        //jzero();
        break;

      case 10:
        //jgtz();
        break;

      case 11:
        //halt();
        break;

      default:
        throw new NoSuchElementException
          ("Cannot find an instruction associated with opcode" +
           opcode_.getId() + "!");
    }
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

  private void write() {
    output_.write((int)opcode_.getArgument());
  }
}
