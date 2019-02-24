package Ramsim.Alu;

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

  public void cycle() {
    fetch();
    execute();
    if (debug_)
      printDebugState();
  }

  public void fetch() {
    opcode_ = programMemory_.get(ip_);
    ++ip_;
  }

  public boolean isHalted() {
    return halt_;
  }

  public void printDebugState() {
    System.out.println(String.format("IP: %d", ip_ - 1));
    System.out.println("Opcode: " + opcode_);
    System.out.println("Data memory:\n" + dataMemory_);
    System.out.println("Input tape:\n" + input_);
    System.out.println("Output tape:\n" + output_);
    System.out.println();
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
      ("Cannot find an instruction associated with opcode" +
       opcode_.getId() + "!");
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
    dataMemory_.put(opcode_.getRegisterIndex(), input_.read());
  }

  private void write() {
    output_.write((int)opcode_.getValue());
  }

  private void jump() {
    ip_ = tags_.get((String)opcode_.getValue());
  }

  private void jzero() {
    if (dataMemory_.get(ACC) == 0)
      ip_ = tags_.get((String)opcode_.getValue());
  }

  private void jgtz() {
    if (dataMemory_.get(ACC) > 0)
      ip_ = tags_.get((String)opcode_.getValue());
  }

  public void halt() {
    output_.storeTapeToFile();
    halt_ = true;
  }
}
