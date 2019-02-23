package Ramsim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import Ramsim.Instruction.Operand.*;
import Ramsim.Memory.Memory;


public class OperandTest {
  private Memory<Integer> memory_;
  private static Operand<?> op_;

  @BeforeEach
  public void setUp() throws Exception {
    memory_ = new Memory<Integer>();
    memory_.put(0, 2);
    memory_.put(1, 4);
    memory_.put(2, 6);
  }

  @Test
  public void testConstOperand() {
    // Get value of constant 10
    op_ = new ConstOperand<Integer>(10);
    assertTrue(op_.getValue().equals(10));

    // Now operand is a constant string ("tag")
    op_ = new ConstOperand<String>("tag");
    assertTrue(op_.getValue().equals("tag"));
  }

  @Test
  public void testDirectDirOperand() {
    // Get value of R0 in memory (which is 2)
    op_ = new DirectDirOperand<Integer>(0, memory_);
    assertTrue(op_.getValue().equals(2));
  }

  @Test
  public void testIndirectDirOperand() {
    // Get value of R2 through R0: R0 = 2 -> R2 = 6
    op_ = new IndirectDirOperand<Integer>(0, memory_);
    assertTrue(op_.getValue().equals(6));
  }
}
