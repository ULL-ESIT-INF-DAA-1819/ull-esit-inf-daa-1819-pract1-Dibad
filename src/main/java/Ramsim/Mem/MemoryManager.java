package Ramsim.Mem;

import Ramsim.Instruction.Opcode;

public class MemoryManager {
  private Memory<Integer> dataMemory_ = new Memory<Integer>();
  private Memory<Opcode> programMemory_ = new Memory<Opcode>();

  private static final int ACC_INDEX = 0; // Which register corresponds to acc

  // Data memory operations
  public int getAcc() { return dataMemory_.get(ACC_INDEX); }
  public void setAcc(int value) { dataMemory_.put(ACC_INDEX, value); }

  public int getRegister(int index) { return dataMemory_.get(index); }
  public void putInRegister(int index, int value) { dataMemory_.put(index, value); }

  // Program memory operations
  public Opcode getInstruction(int index) { return programMemory_.get(index); }
  public void putInRegister(int index, Opcode inst) { programMemory_.put(index, inst); }
}
