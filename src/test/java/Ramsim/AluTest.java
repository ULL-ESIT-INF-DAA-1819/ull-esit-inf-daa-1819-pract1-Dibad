package Ramsim;

import java.util.HashMap;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import Ramsim.Alu.Alu;
import Ramsim.Memory.Memory;
import Ramsim.Io.InputUnit;
import Ramsim.Io.OutputUnit;
import Ramsim.Instruction.Opcode;
import Ramsim.Instruction.InstructId;
import Ramsim.Instruction.Operand.*;


public class AluTest {

  private Memory<Integer> dataMemory_;
  private Memory<Opcode> programMemory_;
  private HashMap<String, Integer> tags_;
  private InputUnit input_;
  private OutputUnit output_;
  private Alu alu_;

  private ArrayList<Operand<?>> args_;

  @BeforeEach
  public void setUp() throws Exception {
    dataMemory_ = new Memory<Integer>();
    programMemory_ = new Memory<Opcode>();
    tags_ = new HashMap<String, Integer>();
    input_ = new InputUnit("input.txt");
    output_ = new OutputUnit("output.txt");
    alu_ = new Alu(dataMemory_, programMemory_, tags_, input_, output_, true);
    args_ = new ArrayList<Operand<?>>();
  }

  @Test
  public void testLoadConst() {
    // LOAD =2: Put constant 2 in ACC (R0)
    args_.add(new ConstOperand<>(2));
    var opcode = new Opcode(InstructId.LOAD, args_);

    programMemory_.put(0, opcode);
    alu_.cycle();

    assertTrue(dataMemory_.get(Alu.ACC) == 2);
  }

  @Test
  public void testLoadDirect() {
    // Load 2: Put value of R2 in ACC
    args_.add(new DirectDirOperand<>(2, dataMemory_));
    var opcode = new Opcode(InstructId.LOAD, args_);

    dataMemory_.put(2, 5); // R2=5

    programMemory_.put(0, opcode);
    alu_.cycle();

    assertTrue(dataMemory_.get(Alu.ACC) == 5);
  }

  @Test
  public void testLoadIndirect() {
    // Load *2: Put value of the Register pointed by R2 in ACC
    args_.add(new IndirectDirOperand<>(2, dataMemory_));
    var opcode = new Opcode(InstructId.LOAD, args_);

    dataMemory_.put(2, 4); // R2=4
    dataMemory_.put(4, 9); // R4=9

    programMemory_.put(0, opcode);
    alu_.cycle();

    assertTrue(dataMemory_.get(Alu.ACC) == 9);
  }

  @Test
  public void testStoreConst() {
    // STORE =1: Almacena el ACC en la constante 1 [DEBE FALLAR]
    args_.add(new ConstOperand<>(1));
    var opcode = new Opcode(InstructId.STORE, args_);

    dataMemory_.put(Alu.ACC, 5); // ACC=5

    programMemory_.put(0, opcode);
    assertThrows(IllegalArgumentException.class, () -> {
      alu_.cycle();
    });
  }

  @Test
  public void testStoreDirect() {
    // STORE 1: Almacena el ACC en R1
    args_.add(new DirectDirOperand<>(1, dataMemory_));
    var opcode = new Opcode(InstructId.STORE, args_);

    dataMemory_.put(Alu.ACC, 3);

    programMemory_.put(0, opcode);
    alu_.cycle();

    assertTrue(dataMemory_.get(1) == dataMemory_.get(Alu.ACC));
  }

  @Test
  public void testStoreIndirect() {
    // STORE *1: Almacena el aCC en el Registro apuntado por R1
    args_.add(new IndirectDirOperand<>(1, dataMemory_));
    var opcode = new Opcode(InstructId.STORE, args_);

    dataMemory_.put(Alu.ACC, 3); // R0=5
    dataMemory_.put(1, 4);       // R1=4

    programMemory_.put(0, opcode);
    alu_.cycle();

    assertTrue(dataMemory_.get(4) == dataMemory_.get(Alu.ACC));
  }
}
