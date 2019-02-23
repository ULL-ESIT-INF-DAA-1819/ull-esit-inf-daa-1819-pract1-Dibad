package Ramsim.Alu;

import java.util.NoSuchElementException;

import Ramsim.Memory.MemoryManager;
import Ramsim.Instruction.Opcode;
import Ramsim.Io.InputUnit;
import Ramsim.Io.OutputUnit;

public class Alu {
  // Component links
  private MemoryManager memory_;
  private InputUnit input_;
  private OutputUnit output_;

  // Alu attributes
  private Opcode opcode_;
  private int ip_ = 0; // Instruction pointer

  public Alu(MemoryManager memory,
             InputUnit input,
             OutputUnit output) {
    memory_ = memory;
    input_ = input;
    output_ = output;
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
    switch (opcode_.getId()) {
    case LOAD:
      load();
      break;

    case STORE:
      store();
      break;

    case ADD:
      add();
      break;

    case SUB:
      sub();
      break;

    case MUL:
      // mul();
      break;

    case DIV:
      // div();
      break;

    case READ:
      read();
      break;

    case WRITE:
      write();
      break;

    case JUMP:
      //jump();
      break;

    case JZERO:
      //jzero();
      break;

    case JGTZ:
      //jgtz();
      break;

    case HALT:
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
