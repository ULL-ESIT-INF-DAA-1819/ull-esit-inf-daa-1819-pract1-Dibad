package Ramsim.Io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.lang.IllegalStateException;

public class InputUnit {

  int pos_ = 0;
  Scanner fileReader_;

  public InputUnit(String filepath) throws FileNotFoundException {
    fileReader_ = new Scanner(new File(filepath));
  }

  public int nextValue() throws IllegalStateException {
    ++pos_;
    try {
      return fileReader_.nextInt();
    } catch(NoSuchElementException e) {
      throw new IllegalStateException(
          String.format("Error reading the input tape at position %d", pos_),
          e);
    }
  }
}
