package Ramsim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryTest {

  private Memory<Integer> memory_;

  @BeforeEach
  public void setUp() throws Exception {
    memory_ = new Memory<Integer>();
  }

  @Test
  public void testReplaceValue() {

    assertTrue(memory_.get(2) == null); // Initially all Registers are null

    memory_.put(2, 1); // Put 1 in R2
    assertTrue(memory_.get(2) == 1);

    memory_.put(2, 5); // Now put 5 in R2
    assertTrue(memory_.get(2) == 5); // The value of R2 should be the new one
  }
}
