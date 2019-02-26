package Ramsim.Io;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/**
  * Provides the necessary methods for reading a file from the filesystem and
  * loading its content to a RAM tape
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  *
  * @see Unit
  */
public class InputUnit extends Unit {
  int readingHead_ = 0;

  public InputUnit(String filepath) throws IllegalStateException {
    super(filepath);
    loadFileToTape();
  }

  /**
    * Scan through each element in the file and add it to the tape
    */
  public void loadFileToTape() throws IllegalStateException {
    try(Scanner fileReader = new Scanner(new File(filepath_))) {
      while (fileReader.hasNextInt()) {
        tape_.add(fileReader.nextInt());
      }

    } catch (IOException e) {
      throw new IllegalStateException("Error reading the input file!", e);
    }
  }

  /**
    * Get the value pointed by the head of the tape and move to the next element
    */
  public int read() throws IndexOutOfBoundsException {
    return tape_.get(readingHead_++);
  }

  @Override
  public int peek() {
    return tape_.get(readingHead_);
  }
}
