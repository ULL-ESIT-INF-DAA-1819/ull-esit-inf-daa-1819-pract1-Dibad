package Ramsim.Io;

import java.io.FileWriter;
import java.io.IOException;

public class OutputUnit extends Unit {

  public OutputUnit(String filepath) {
    super(filepath);
  }

  public void storeTapeToFile() throws IllegalStateException {
    try(FileWriter fileWriter = new FileWriter(filepath_)) {
      for (var value : tape_)
        fileWriter.write(String.format("%d ", value));

    } catch (IOException e) {
      throw new IllegalStateException("Error writing to the output file!", e);
    }
  }

  public void write(int value) {
    tape_.add(value);
  }

  @Override
  public int peek() {
    return tape_.get(tape_.size() - 1);
  }
}
