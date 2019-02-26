package Ramsim.Io;

import java.io.FileWriter;
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
public class OutputUnit extends Unit {

  public OutputUnit(String filepath) {
    super(filepath);
  }

  /**
    * Iterate through each element of the tape and write it to the file
    */
  public void storeTapeToFile() throws IllegalStateException {
    try(FileWriter fileWriter = new FileWriter(filepath_)) {
      for (var value : tape_)
        fileWriter.write(String.format("%d ", value));

    } catch (IOException e) {
      throw new IllegalStateException("Error writing to the output file!", e);
    }
  }

  /**
    * Store the value at the end of the tape. The head should always points to
    * the last element
    */
  public void write(int value) {
    tape_.add(value);
  }

  @Override
  public int peek() {
    return tape_.get(tape_.size() - 1);
  }
}
