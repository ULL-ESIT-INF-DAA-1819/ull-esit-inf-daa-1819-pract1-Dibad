/**
  * Core class of the RAM Simulator. Implements the actual workload of the RAM
  * Machine, connecting all the componentes together and fetching and executing
  * the instructions from memory.
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  *
  */
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
  // Components
  private Memory<Integer> dataMemory_;
  private ArrayList<Opcode> programMemory_;
  private HashMap<String, Integer> tags_;
  private InputUnit input_;
  private OutputUnit output_;

  private InstructionSet instructions_;

  // Alu attributes
  private Opcode opcode_;
  private int ip_ = 0; // Instruction pointer
  private int executedInstructions_ = 0;

  // Aliases
  public static final int ACC = 0; // ACC == Index 0 (R0)

  // Flags
  boolean debug_; // If true, print more information in case of error
  boolean halt_;  // If true, stop the RAM Machine


  /**
    * The constructor takes as an argument a list of all the components that
    * interact with the Alu. Also a debug flag.
    */
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
    instructions_ = new InstructionSet();

    debug_ = debug;
  }

  /**
    * Simulates the two phases of a Cpu cycle: Fetching and Execution
    */
  public void cycle() throws IndexOutOfBoundsException {
    fetch();
    instructions_.execute();
    if (debug_)
      printDebugState();
  }

  /**
    * Fetch from memory the instruction pointed by the IP, and increment the
    * IP it after that.
    */
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

  /**
    * @return true or false whether the Alu is halted or not
    */
  public boolean isHalted() {
    return halt_;
  }

  /**
    * Ask the alu to halt, stopping all execution
    */
  public void halt() {
    instructions_.halt();
  }

  /**
    * Group of statements that print information about the inner state of the
    * Alu and its componentes
    */
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


  /**
    * The Instruction Set of the RAM Machine Simulator.
    * The advantage of implementing it as a nested class is to allow adding more
    * instructions or overriding existing ones by creating a child class that
    * 'extends InstructionSet'
    * This pattern allows to hide the implementation details of the
    * instruction set by just providing a public 'execute()' method
    */
  class InstructionSet {
    /**
      * Get the corresponding function for the given opcode ID
      */
    public void execute() {
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

    /**
      * Load the operand to R0 (ACC)
      */
    private void load() {
      dataMemory_.put(ACC, (int)opcode_.getValue());
    }

    /**
      * Store R0 (ACC) in the operand
      */
    private void store() {
      dataMemory_.put(opcode_.getRegisterIndex(), dataMemory_.get(ACC));
    }

    /**
      * Sum the ACC with the operand and store the result in the ACC
      */
    private void add() {
      dataMemory_.put(ACC, (int)opcode_.getValue() + dataMemory_.get(ACC));
    }

    /**
      * Sub the operator from the ACC and store the result in the ACC
      */
    private void sub() {
      dataMemory_.put(ACC, dataMemory_.get(ACC) - (int)opcode_.getValue());
    }

    /**
      * Multiply the operator with the ACC and store the result in the ACC
      */
    private void mul() {
      dataMemory_.put(ACC, (int)opcode_.getValue() * dataMemory_.get(ACC));
    }

    /**
      * Divide the ACC by the operator and store the result in the ACC
      */
    private void div() {
      dataMemory_.put(ACC, dataMemory_.get(ACC) / (int)opcode_.getValue());
    }

    /**
      * Read a value from the input tape and store it in the operand
      */
    private void read() {
      int index = opcode_.getRegisterIndex();
      if (index == Alu.ACC)
        throw new IllegalArgumentException(
          "READ Instruction can't be used with the ACC register!");

      dataMemory_.put(opcode_.getRegisterIndex(), input_.read());
    }

    /**
      * Write a value from the operand to the output tape
      */
    private void write() {
      int index = Alu.ACC + 1; // Initialized as never the same value than the ACC
      try { // Workaround for silently catch exception when WRITE 0, which should work
        index = opcode_.getRegisterIndex();
      } catch (IllegalArgumentException e) {}

      if (index == Alu.ACC)
        throw new IllegalArgumentException(
          "WRITE Instruction can't be used with the ACC register!");

      output_.write((int)opcode_.getValue());
    }

    /**
      * Modify IP to point to the instruction identified by the operand tag
      */
    private void jump() {
      try {
        ip_ = tags_.get((String)opcode_.getValue());
      } catch (ClassCastException e) {
        throw new IllegalArgumentException(
          "JUMP instruction must have a tag as argument!", e);
      }
    }

    /**
      * Modify IP to point to the instruction identified by the operand tag if
      * the ACC is equal to 0
      */
    private void jzero() {
      if (dataMemory_.get(ACC) == 0)
        jump();
    }

    /**
      * Modify IP to point to the instruction identified by the operand tag if
      * the ACC is greater than 0
      */
    private void jgtz() {
      if (dataMemory_.get(ACC) > 0)
        jump();
    }

    /**
      * Print output tape to file and stop the program execution
      */
    public void halt() {
      output_.storeTapeToFile();
      System.out.println(String.format("FINISHED: %d instructions executed",
                                       executedInstructions_));
      halt_ = true;
    }
  }
}
