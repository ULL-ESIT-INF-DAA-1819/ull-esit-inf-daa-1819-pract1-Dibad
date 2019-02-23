package Ramsim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import Ramsim.Io.InputUnit;
import Ramsim.Io.OutputUnit;


public class UnitTest {

  @Test
  void testIllegalFiles() {
    assertThrows(IllegalStateException.class, () -> {
      new InputUnit("thisfiledoesnotexists.txt"); // Open inexistent file
    });

    assertThrows(IllegalStateException.class, () -> {
      OutputUnit output = new OutputUnit("/sbin/cantaccess.txt");
      output.storeTapeToFile();
    });
  }

  @Test
  void testReadAndWriteFile() {
    OutputUnit output = new OutputUnit("src/test/java/Ramsim/test.txt");
    output.write(1);
    output.write(2);
    output.storeTapeToFile();

    InputUnit input = new InputUnit("src/test/java/Ramsim/test.txt");
    assertTrue(input.read() == 1);
    assertTrue(input.read() == 2);

    assertThrows(IndexOutOfBoundsException.class, () -> {
      input.read();
    });
  }
}
