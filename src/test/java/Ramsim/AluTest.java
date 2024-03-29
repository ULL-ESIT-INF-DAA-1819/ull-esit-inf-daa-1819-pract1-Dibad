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
    input_ = new InputUnit("src/test/java/Ramsim/testinput.txt");
    output_ = new OutputUnit("src/test/java/Ramsim/testoutput.txt");
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
      // STORE =1: Put the value of the ACC in the constant 1 [MUST FAIL]
      args_.add(new ConstOperand<>(1));
      var opcode = new Opcode(InstructId.STORE, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }

    @Test
    public void testStoreDirect() {
      // STORE 1: Store the ACC in R1
      args_.add(new DirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.STORE, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // R1=ACC=5

      assertTrue(dataMemory_.get(1) == dataMemory_.get(Alu.ACC));
    }

    @Test
    public void testStoreIndirect() {
      // STORE *1: Store the aCC in the register pointed by R1
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
      // ADD =2: Suma 2 al valor del ACC y almacena el resultado en el ACC
      args_.add(new ConstOperand<>(2));
      var opcode = new Opcode(InstructId.ADD, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 + 2

      assertTrue(dataMemory_.get(Alu.ACC) == 7);
    }

    @Test
    public void testAddDirect() {
      // ADD 2: Suma el valor del Registro R2 al ACC y almacena el resultado en
      // el ACC
      args_.add(new DirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.ADD, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 + R2=4

      assertTrue(dataMemory_.get(Alu.ACC) == 9);
    }

    @Test
    public void testAddIndirect() {
      // ADD *2: Suma el valor del Registro apuntado por R2 al ACC y almacena el
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
      // SUB =3: Subtract the constant 3 to the ACC and store the result in the
      // ACC
      args_.add(new ConstOperand<>(3));
      var opcode = new Opcode(InstructId.SUB, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 - 3

      assertTrue(dataMemory_.get(Alu.ACC) == 2);
    }

    @Test
    public void testSubDirect() {
      // SUB 3: Subtract the content of R3 to the ACC and store the result in
      // the ACC
      args_.add(new DirectDirOperand<>(3, dataMemory_));
      var opcode = new Opcode(InstructId.SUB, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 - R3=6

      assertTrue(dataMemory_.get(Alu.ACC) == -1);
    }

    @Test
    public void testSubIndirect() {
      // SUB *3: Subtract the content of the Register pointed by R3 to the ACC
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
    @Test
    public void testMulConstant() {
      // MUL =3: Multiply the constant 3 with the ACC and store the result in
      // the ACC
      args_.add(new ConstOperand<>(3));
      var opcode = new Opcode(InstructId.MUL, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 * 3

      assertTrue(dataMemory_.get(Alu.ACC) == 15);
    }

    @Test
    public void testMulDirect() {
      // MUL 3: Multiply the content of R3 with the ACC and store the result in
      // the ACC
      args_.add(new DirectDirOperand<>(3, dataMemory_));
      var opcode = new Opcode(InstructId.MUL, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 * R3=6

      assertTrue(dataMemory_.get(Alu.ACC) == 30);
    }

    @Test
    public void testMulIndirect() {
      // MUL *3: Multiply the content of the Register pointed by R3 with the ACC
      // and store the result in the ACc
      args_.add(new IndirectDirOperand<>(3, dataMemory_));
      var opcode = new Opcode(InstructId.MUL, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 * (R3=6 -> R6=12)

      assertTrue(dataMemory_.get(Alu.ACC) == 60);
    }
  }

  @Nested
  @DisplayName("DIV")
  class Div {
    @Test
    public void testDivConstant() {
      // DIV =1: Divide the constant 1 by the ACC and store the result in the
      // ACC
      args_.add(new ConstOperand<>(1));
      var opcode = new Opcode(InstructId.DIV, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 / 1

      assertTrue(dataMemory_.get(Alu.ACC) == 5);
    }

    @Test
    public void testDivDirect() {
      // DIV 1: Divide the content of R1 by the ACC and store the result in the
      // ACC
      args_.add(new DirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.DIV, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 / R1=2

      assertTrue(dataMemory_.get(Alu.ACC) == 2);
    }

    @Test
    public void testDivIndirect() {
      // DIV *1: Divide the content of the Register pointed by R1 by the ACC and
      // store the result in the ACC
      args_.add(new IndirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.DIV, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // ACC=5 / (R1=2 -> R2=4)

      assertTrue(dataMemory_.get(Alu.ACC) == 1);
    }
  }

  @Nested
  @DisplayName("READ")
  class Read {
    @Test
    public void testReadConstant() {
      // READ =2: Read a value from the input tape and store it in the
      // constant [MUST FAIL]
      args_.add(new ConstOperand<>(2));
      var opcode = new Opcode(InstructId.READ, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }

    @Test
    public void testReadDirect() {
      // READ 2: Read a value from the input tape and store it in the Register
      // R2
      args_.add(new DirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.READ, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // R2=READ=1

      assertTrue(dataMemory_.get(2) == 1);
    }

    @Test
    public void testReadIndirect()  {
      // READ *2: Read a value from the input tape and store it in the Register
      // pointed by R2
      args_.add(new IndirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.READ, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // (R2=4 -> R4)=READ=1

      assertTrue(dataMemory_.get(4) == 1);
    }

    @Test
    public void testReadToAccDirect() {
      // READ 0: Read a value from the input tape and store it in the ACC
      // [MUST FAIL]
      args_.add(new DirectDirOperand<>(0, dataMemory_));
      var opcode = new Opcode(InstructId.READ, args_);

      programMemory_.add(opcode);

      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }

    @Test
    public void testReadToAccIndirect() {
      // READ *1: Read a value from the input tape and store it in the Register
      // pointed by R1 (which will be the ACC)
      args_.add(new IndirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.READ, args_);

      dataMemory_.put(1, 0);

      programMemory_.add(opcode);

      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }
  }

  @Nested
  @DisplayName("WRITE")
  class Write {
    @Test
    public void testWriteConstant() {
      // WRITE =2: Write the constant 1 to the output tape
      args_.add(new ConstOperand<>(2));
      var opcode = new Opcode(InstructId.WRITE, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // WRITE=2

      assertTrue(output_.peek() == 2);
    }

    @Test
    public void testWriteDirect() {
      // WRITE 2: Write the content of R2 to the output tape
      args_.add(new DirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.WRITE, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // WRITE=R2=4

      assertTrue(output_.peek() == 4);
    }

    @Test
    public void testWriteIndirect() {
      // WRITE *2: Write the content of the Register pointed by R2 to the output
      // tape
      args_.add(new IndirectDirOperand<>(2, dataMemory_));
      var opcode = new Opcode(InstructId.WRITE, args_);

      programMemory_.add(opcode);
      alu_.cycle(); // WRITE=(R2=4 -> R4)=8

      assertTrue(output_.peek() == 8);
    }

    @Test
    public void testWriteFromAccDirect() {
      // WRITE 0: Write the content of the ACC to the output tape [MUST FAIL]
      args_.add(new DirectDirOperand<>(0, dataMemory_));
      var opcode = new Opcode(InstructId.WRITE, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }

    @Test
    public void testWriteFromAccIndirect() {
      // WRITE *1: Write the content of the Register pointed by R1 (which will
      // be the ACC) to the output tape [MUST FAIL]
      args_.add(new IndirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.WRITE, args_);

      dataMemory_.put(1, 0);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }
  }

  @Nested
  @DisplayName("JUMP")
  class Jump {
    @Test
    public void testJumpConstant() {
      // JUMP start: Modify IP to point to the instruction with tag 'start'
      args_.add(new ConstOperand<>("start"));
      var opcode = new Opcode(InstructId.JUMP, args_);

      tags_.put("start", 0);  // start -> 0

      programMemory_.add(opcode);
      assertAll(() -> {
        alu_.cycle(); // IP should point to 0 again
        alu_.cycle(); // IP should point to 0 again
        alu_.cycle(); // IP should point to 0 again
      });
    }

    @Test
    public void testJumpDirect() {
      // JUMP 1: Jump only works with tags, MUST FAIL
      args_.add(new DirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.JUMP, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }

    @Test
    public void testJumpIndirect() {
      // JUMP *1: Jump only works with tags, MUST FAIL
      args_.add(new IndirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.JUMP, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }
  }

  @Nested
  @DisplayName("JZERO")
  class Jzero {
    @BeforeEach
    public void setAccToZero() {
      dataMemory_.put(Alu.ACC, 0);
    }

    @Test
    public void testJzeroConstant() {
      // JZERO start: Jump to the instruction pointed by the tag if the ACC
      // value is 0
      args_.add(new ConstOperand<>("start"));
      var opcode = new Opcode(InstructId.JZERO, args_);

      tags_.put("start", 0); // start=0

      programMemory_.add(opcode);
      assertAll(() -> {
        alu_.cycle(); // ACC=0 -> Jumps to start=0
        alu_.cycle(); // ACC=0 -> Jumps to start=0
        alu_.cycle(); // ACC=0 -> Jumps to start=0
      });

      dataMemory_.put(Alu.ACC, 5);  // ACC=5

      assertThrows(IndexOutOfBoundsException.class, () -> {
        alu_.cycle(); // ACC=5 -> Don't jump
        alu_.cycle(); // No more instructions, throw OutOfBounds
      });
    }

    @Test
    public void testJzeroDirect() {
      // JZERO 1: Jzero only works with tags, MUST FAIL
      args_.add(new DirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.JZERO, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }

    @Test
    public void testJzeroIndirect() {
      // JZERO *1: Jzero only works with tags, MUST FAIL
      args_.add(new IndirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.JZERO, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }
  }

  @Nested
  @DisplayName("JGTZ")
  class Jgtz {
    @Test
    public void testJgtzConstant() {
      // JGTZ start: Jump to tag if ACC is greater than 0
      args_.add(new ConstOperand<>("start"));
      var opcode = new Opcode(InstructId.JGTZ, args_);

      tags_.put("start", 0);

      programMemory_.add(opcode);
      assertAll(() -> {
        alu_.cycle(); // ACC=5 > 0. Jump to start=0
        alu_.cycle(); // ACC=5 > 0. Jump to start=0
        alu_.cycle(); // ACC=5 > 0. Jump to start=0
      });

      dataMemory_.put(Alu.ACC, 0);
      assertThrows(IndexOutOfBoundsException.class, () -> {
        alu_.cycle(); // ACC=0. Don't jump
        alu_.cycle(); // No more instructions. Throw OutOfBounds
      });
    }

    @Test
    public void testJzeroDirect() {
      // JGTZ 1: Jgtz only works with tags, MUST FAIL
      args_.add(new DirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.JGTZ, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }

    @Test
    public void testJzeroIndirect() {
      // JGTZ *1: Jgtz only works with tags, MUST FAIL
      args_.add(new IndirectDirOperand<>(1, dataMemory_));
      var opcode = new Opcode(InstructId.JGTZ, args_);

      programMemory_.add(opcode);
      assertThrows(IllegalArgumentException.class, () -> {
        alu_.cycle();
      });
    }
  }
}
