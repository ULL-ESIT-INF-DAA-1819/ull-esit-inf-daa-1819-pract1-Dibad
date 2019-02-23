package Ramsim.Io;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class InputUnit extends Unit {
  int readingHead_ = 0;

  public InputUnit(String filepath) throws IllegalStateException {
    super(filepath);
    loadFileToTape();
  }

  public void loadFileToTape() throws IllegalStateException {
    try(Scanner fileReader = new Scanner(new File(filepath_))) {
      while (fileReader.hasNextInt()) {
        tape_.add(fileReader.nextInt());
      }

    } catch (IOException e) {
      throw new IllegalStateException("Error reading the input file!", e);
    }
  }

  public int read() throws IndexOutOfBoundsException {
    return tape_.get(readingHead_++);
  }
}
