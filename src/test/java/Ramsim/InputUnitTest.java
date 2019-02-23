package Ramsim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import Ramsim.Io.InputUnit;


public class InputUnitTest {

  @Test
  void testOpenInvalidFile() {
    assertThrows(FileNotFoundException.class, () -> {
      new InputUnit("thisfiledoesnotexists.txt");
    });
  }

  @Test
  void testReadValues() throws FileNotFoundException {
    InputUnit input = new InputUnit("src/test/java/Ramsim/input.txt"); // File content: 1 2 $
    assertTrue(input.nextValue() == 1);
    assertTrue(input.nextValue() == 2);
    assertThrows(IllegalStateException.class, () -> {
      input.nextValue(); // Read a $ should raise an exception
    });
  }
}
