/**
  * This class represents how an instruction is stored in memory. Its composed
  * of an unique ID and a variable list of arguments which can be of different
  * types.
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  *
  * @see InstructId
  * @see Operand
  */
package Ramsim.Instruction;

import java.util.ArrayList;

import Ramsim.Instruction.Operand.Operand;

public class Opcode {
  private InstructId id_;
  private final ArrayList<Operand<?>> argument_;

  private int lineNum_; // Line in code where this instruction appears

  /**
    * @param id unique identifier of the instruction
    * @param args list of arguments handled by the instruction
    */
  public Opcode(InstructId id, ArrayList<Operand<?>> arg) {
    this(id, arg, -1);
  }

  /**
    * @param lineNume line of code where this instruction appears
    */
  public Opcode(InstructId id, ArrayList<Operand<?>> arg, int lineNum) {
    id_ = id;
    argument_ = arg;
    lineNum_ = lineNum;
  }

  /**
    * @return identifier of the instruction
    */
  public InstructId getId() {
    return id_;
  }

  /**
    * @return the number where this instruction appeared
    */
  public int getLineNum() {
    return lineNum_;
  }

  /**
    * Alias for accessing the 1st operand of the instruction
    * @return value of the operand
    */
  public Object getValue() { // Alias for acessing the 1st argument
    return argument_.get(0).getValue();
  }

  /**
    * Alias for accessing the 1st operand of the instruction
    * @return register index of the operand
    */
  public int getRegisterIndex() { // Alias for accesing the 1st argument
    return argument_.get(0).getRegisterIndex();
  }

  public String toString() {
    return String.format("%s%s", id_.toString(), argument_.toString());
  }
}
