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
  private ArrayList<Opcode> programMemory_;
  private HashMap<String, Integer> tags_;
  private InputUnit input_;
  private OutputUnit output_;
  private Alu alu_;

  private ArrayList<Operand<?>> args_;

  @BeforeEach
  public void resetState() {
    dataMemory_ = new Memory<Integer>();
    programMemory_ = new ArrayList<Opcode>();
    tags_ = new HashMap<String, Integer>();
    input_ = new InputUnit("input.txt");
    output_ = new OutputUnit("output.txt");
    alu_ = new Alu(dataMemory_, programMemory_, tags_, input_, output_, false);

    args_ = new ArrayList<Operand<?>>();

    // Fill memory with basic values (Ri= i * 2)
    dataMemory_.put(0, 5);  // Reset ACC to 5
    dataMemory_.put(1, 2);
    dataMemory_.put(2, 4);
    dataMemory_.put(3, 6);
    dataMemory_.put(4, 8);
    dataMemory_.put(5, 10);
    dataMemory_.put(6, 12);
    dataMemory_.put(7, 15);
  }

  @Nested
  @DisplayName("LOAD")
  class Load {
    @Test
    public void testLoadConst() {
      // LOAD =2: Put constant 2 in ACC (R0)
      args_.add(new ConstOperand<>(2));
      var opcode = new Opcode(InstructId.LOAD, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=2

      assertTrue(dataMemory_.get(Alu.ACC) == 2);
    }

    @Test
    public void testLoadDirect() {
      // Load 2: Put value of R2 in ACC
      args_.add(new DirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.LOAD, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=R2=4

      assertTrue(dataMemory_.get(Alu.ACC) == 4);
    }

    @Test
    public void testLoadIndirect() {
      // Load *2: Put value of the Register pointed by R2 in ACC
      args_.add(new IndirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.LOAD, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=(R2=4 -> R4=8)

      assertTrue(dataMemory_.get(Alu.ACC) == 8);
    }
  }


  @Nested
  @DisplayName("STORE")
  class Store {
    @Test
    public void testStoreConst() {
      // STORE =1: Almacena el ACC en la constante 1 [DEBE FALLAR]
      args_.add(new ConstOperand<>(1));
      var opcode = new Opcode(InstructId.STORE, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }

    @Test
    public void testStoreDirect() {
      // STORE 1: Almacena el ACC en R1
      args_.add(new DirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.STORE, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // R1=ACC=5

      assertTrue(dataMemory_.get(1) == dataMemory_.get(Alu.ACC));
    }

    @Test
    public void testStoreIndirect() {
      // STORE *1: Almacena el ACC en el Registro apuntado por R1
      args_.add(new IndirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.STORE, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // (R1=2 -> R2)=ACC=5

      assertTrue(dataMemory_.get(2) == dataMemory_.get(Alu.ACC));
    }
  }

  @Nested
  @DisplayName("ADD")
  class Add {
    @Test
    public void testAddConstant() {
      // Add =2: Suma 2 al valor del ACC y almacena el resultado en el ACC
      args_.add(new ConstOperand<>(2));
      var opcode = new Opcode(InstructId.ADD, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 + 2

      assertTrue(dataMemory_.get(Alu.ACC) == 7);
    }

    @Test
    public void testAddDirect() {
      // Add 2: Suma el valor del Registro R2 al ACC y almacena el resultado en
      // el ACC
      args_.add(new DirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.ADD, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 + R2=4

      assertTrue(dataMemory_.get(Alu.ACC) == 9);
    }

    @Test
    public void testAddIndirect() {
      // Add *2: Suma el valor del Registro apuntado por R2 al ACC y almacena el
      // resultado en el ACC
      args_.add(new IndirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.ADD, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 + (R2=4 -> R4=8)

      assertTrue(dataMemory_.get(Alu.ACC) == 13);
    }
  }

  @Nested
  @DisplayName("SUB")
  class Sub {
    @Test
    public void testSubConstant() {
      // Sub =3: Subtract the constant 3 to the ACC and store the result in the
      // ACC
      args_.add(new ConstOperand<>(3));
      var opcode = new Opcode(InstructId.SUB, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 - 3

      assertTrue(dataMemory_.get(Alu.ACC) == 2);
    }

    @Test
    public void testSubDirect() {
      // Sub 3: Subtract the content of R3 to the ACC and store the result in
      // the ACC
      args_.add(new DirectDirOperand<>(3, dataMemory_));
      var opcode = new Opcode(InstructId.SUB, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 - R3=6

      assertTrue(dataMemory_.get(Alu.ACC) == -1);
    }

    @Test
    public void testSubIndirect() {
      // Sub *3: Subtract the content of the Register pointed by R3 to the ACC
      // and store the result in the ACC
      args_.add(new IndirectDirOperand<>(3, dataMemory_));
      var opcode = new Opcode(InstructId.SUB, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 - (R3=6 -> R6=12)

      assertTrue(dataMemory_.get(Alu.ACC) == -7);
    }
  }

  @Nested
  @DisplayName("MUL")
  class Mul {
    public void testMulConstant() {
      // Mul =3: Multiply the constant 3 with the ACC and store the result in
      // the ACC
      args_.add(new ConstOperand<>(3));
      var opcode = new Opcode(InstructId.MUL, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 * 3

      assertTrue(dataMemory_.get(Alu.ACC) == 15);
    }

    public void testMulDirect() {
      // Mul 3: Multiply the content of R3 with the ACC and store the result in
      // the ACC
      args_.add(new DirectDirOperand<>(3, dataMemory_));
      var opcode = new Opcode(InstructId.MUL, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=3 * R3=6

      assertTrue(dataMemory_.get(Alu.ACC) == 18);
    }

    public void testMulIndirect() {
      // Mul*3: Multiply the content of the Register pointed by R3 with the ACC
      // and store the result in the ACc
      args_.add(new IndirectDirOperand<>(3, dataMemory_));
      var opcode = new Opcode(InstructId.MUL, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 * (R3=6 -> R6=12)

      assertTrue(dataMemory_.get(Alu.ACC) == 70);
    }
  }

  @Nested
  @DisplayName("DIV")
  class Div {
    public void testDivConstant() {
      // Div =1: Divide the constant 1 by the ACC and store the result in the
      // ACC
      args_.add(new ConstOperand<>(1));
      var opcode = new Opcode(InstructId.DIV, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 / 1

      assertTrue(dataMemory_.get(Alu.ACC) == 5);
    }

    public void testDivDirect() {
      // Div 1: Divide the content of R1 by the ACC and store the result in the
      // ACC
      args_.add(new DirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.DIV, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 / R1=2

      assertTrue(dataMemory_.get(Alu.ACC) == 2);
    }

    public void testDivIndirect() {
      // Div *1: Divide the content of the Register pointed by R1 by the ACC and
      // store the result in the ACC
      args_.add(new IndirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.DIV, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 / (R1=2 -> R2=4)

      assertTrue(dataMemory_.get(Alu.ACC) == 0);
    }
  }
}
