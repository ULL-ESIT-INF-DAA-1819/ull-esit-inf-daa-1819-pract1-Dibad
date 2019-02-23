package Ramsim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import Ramsim.Mem.Memory;


public class MemoryTest {

  private Memory<Integer> memory_;

  @BeforeEach
  public void setUp() throws Exception {
    memory_ = new Memory<Integer>();
  }

  @Test
  public void testGetAndSetValue() {
    // Exception is thrown when trying to acces a null register
    assertThrows(IllegalArgumentException.class, () -> {
      memory_.get(2);
    });

    memory_.put(2, 1); // Put 1 in R2
    assertTrue(memory_.get(2) == 1);

    memory_.put(2, 5); // Now put 5 in R2
    assertTrue(memory_.get(2) == 5); // The value of R2 should be the new one
  }
}
