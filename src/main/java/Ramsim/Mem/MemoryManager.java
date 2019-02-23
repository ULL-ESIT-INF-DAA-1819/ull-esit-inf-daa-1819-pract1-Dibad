package Ramsim.Mem;

import Ramsim.Instruction.Opcode;

public class MemoryManager {
  private Memory<Integer> dataMemory_ = new Memory<Integer>();
  private Memory<Opcode> programMemory_ = new Memory<Opcode>();

  public int getAcc() { return dataMemory_.get(0); } // ACC is R0
  public void setAcc(int value) { dataMemory_.put(0, value); } // ACC is R0

  public int getRegister(int index) { return dataMemory_.get(index); }
  public void putInRegister(int index, int value) { dataMemory_.put(index, value); }

  public Opcode getInstruction(int index) { return programMemory_.get(index); }
  public void putInRegister(int index, Opcode inst) { programMemory_.put(index, inst); }
}
