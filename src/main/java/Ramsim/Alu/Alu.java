package Ramsim.Alu;

import java.lang.ClassCastException;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.ArrayList;

import Ramsim.Memory.Memory;
import Ramsim.Instruction.Opcode;
import Ramsim.Io.InputUnit;
import Ramsim.Io.OutputUnit;

public class Alu {
  // Component links
  private Memory<Integer> dataMemory_;
  private ArrayList<Opcode> programMemory_;
  private HashMap<String, Integer> tags_;
  private InputUnit input_;
  private OutputUnit output_;

  // Alu attributes
  private Opcode opcode_;
  private int ip_ = 0; // Instruction pointer
  private int executedInstructions_ = 0;

  // Aliases
  public static final int ACC = 0; // ACC == Index 0 (R0)

  // Flags
  boolean debug_;
  boolean halt_;


  // Constructor
  public Alu(Memory<Integer> dataMemory,
             ArrayList<Opcode> programMemory,
             HashMap<String, Integer> tags,
             InputUnit input,
             OutputUnit output,
             boolean debug) {

    dataMemory_ = dataMemory;
    programMemory_ = programMemory;
    tags_ = tags;
    input_ = input;
    output_ = output;
    debug_ = debug;
  }

  public void cycle() throws IndexOutOfBoundsException {
    fetch();
    execute();
    if (debug_)
      printDebugState();
  }

  public void fetch() {
    try {
      opcode_ = programMemory_.get(ip_);
      ++ip_;
      ++executedInstructions_;

    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("Tried to fetch instruction " + ip_
                                          + ", which isn't loaded in memory!");
    }
  }

  public boolean isHalted() {
    return halt_;
  }

  public void printDebugState() {
    System.out.println(String.format("\t<< %s >>", opcode_));
    System.out.print(" Line in code: " + opcode_.getLineNum());
    System.out.println(" \tTotal exec. instructions: " + executedInstructions_);
    System.out.println(" \n\t|| Registers ||");
    System.out.println(" IP: " + (ip_ - 1));
    System.out.println(" Data:\n  " + dataMemory_);
    System.out.println(" Input tape:\n  " + input_);
    System.out.println(" Output tape:\n  " + output_);
    System.out.println("\n");
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
      mul();
      break;

    case DIV:
      div();
      break;

    case READ:
      read();
      break;

    case WRITE:
      write();
      break;

    case JUMP:
      jump();
      break;

    case JZERO:
      jzero();
      break;

    case JGTZ:
      jgtz();
      break;

    case HALT:
      halt();
      break;

    default:
      throw new NoSuchElementException
      ("Cannot find instruction with opcode " + opcode_.getId());
    }
  }


  // RAM Simulator Instruction Set
  private void load() {
    dataMemory_.put(ACC, (int)opcode_.getValue());
  }

  private void store() {
    dataMemory_.put(opcode_.getRegisterIndex(), dataMemory_.get(ACC));
  }

  private void add() {
    dataMemory_.put(ACC, (int)opcode_.getValue() + dataMemory_.get(ACC));
  }

  private void sub() {
    dataMemory_.put(ACC, dataMemory_.get(ACC) - (int)opcode_.getValue());
  }

  private void mul() {
    dataMemory_.put(ACC, (int)opcode_.getValue() * dataMemory_.get(ACC));
  }

  private void div() {
    dataMemory_.put(ACC, dataMemory_.get(ACC) / (int)opcode_.getValue());
  }

  private void read() {
    int index = opcode_.getRegisterIndex();
    if (index == Alu.ACC)
      throw new IllegalArgumentException(
        "READ Instruction can't be used with the ACC register!");

    dataMemory_.put(opcode_.getRegisterIndex(), input_.read());
  }

  private void write() {
    int index = Alu.ACC + 1; // Never the same value as ACC
    try { // Workaround for silently catch exception when WRITE 0
      index = opcode_.getRegisterIndex();
    } catch (IllegalArgumentException e) {}

    if (index == Alu.ACC)
      throw new IllegalArgumentException(
        "WRITE Instruction can't be used with the ACC register!");

    output_.write((int)opcode_.getValue());
  }

  private void jump() {
    try {
      ip_ = tags_.get((String)opcode_.getValue());
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(
        "JUMP instruction must have a tag as argument!", e);
    }
  }

  private void jzero() {
    if (dataMemory_.get(ACC) == 0)
      jump();
  }

  private void jgtz() {
    if (dataMemory_.get(ACC) > 0)
      jump();
  }

  public void halt() {
    output_.storeTapeToFile();
    System.out.println(String.format("FINISHED: %d instructions executed",
                                     executedInstructions_));
    halt_ = true;
  }
}
