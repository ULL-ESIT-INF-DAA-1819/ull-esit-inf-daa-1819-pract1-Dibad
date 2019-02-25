/**
  * Identifiers for instructions in the RAM Machine Instruction Set
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  */
package Ramsim.Instruction;

public enum InstructId {
  LOAD,
  STORE,
  ADD,
  SUB,
  MUL,
  DIV,
  READ,
  WRITE,
  JUMP,
  JZERO,
  JGTZ,
  HALT
}
