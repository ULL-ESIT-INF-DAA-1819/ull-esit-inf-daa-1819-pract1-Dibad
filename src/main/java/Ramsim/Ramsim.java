package Ramsim;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import Ramsim.Memory.MemoryManager;
import Ramsim.Io.InputUnit;
import Ramsim.Io.OutputUnit;
import Ramsim.Instruction.Operand.*;
import Ramsim.Instruction.Opcode;
import Ramsim.Alu.Alu;
import Ramsim.Instruction.InstructId;

public class Ramsim {
  // Ram Simulator components
  private MemoryManager memory_;
  private InputUnit input_;
  private OutputUnit output_;
  private Alu alu_;

  // Attributes
  private boolean quit_ = false;

  public Ramsim(String programFilePath, String inputFilePath, String outputFilePath) {
    memory_ = new MemoryManager();
    input_ = new InputUnit(inputFilePath);
    output_ = new OutputUnit(outputFilePath);
    alu_ = new Alu(memory_, input_, output_);

    loadProgram(programFilePath);
  }

  public void execute() {
    while (!quit_) {
      try {
        alu_.cycle();

      } catch (RuntimeException e) { // Handle exceptions better
        System.out.println(e);
        quit_ = true;
      }
    }
  }

  private void loadProgram(String programFilePath) {
    try(Scanner fileReader = new Scanner(new File(programFilePath))) {

      int lineNumber = 0;
      while (fileReader.hasNextLine()) {
        String line = fileReader.nextLine();

        line = line.replaceAll("#[^\n]*", ""); // Remove # comments

        if (!line.isEmpty()) {
          // Split line by whitespaces
          ArrayList<String> splitted = new
          ArrayList<String>(Arrays.asList(line.trim().split("\\s+")));

          // Check if first element is a tag and save it
          if (splitted.get(0).endsWith(":")) {
            memory_.setTag(splitted.get(0), lineNumber);
            splitted.remove(0);
          }

          // Get instruction id
          InstructId id = InstructId.valueOf(splitted.get(0).toUpperCase());
          splitted.remove(0);

          // Get list of operands
          ArrayList<Operand<?>> op = new ArrayList<Operand<?>>();

          if (!splitted.isEmpty()) {
            for (var arg : splitted) {
              if (arg.startsWith("=")) { // DIRECT DIR
                int index = Integer.parseInt(arg.substring(1)); // Remove =
                op.add(new DirectDirOperand<Integer>(index, memory_.dataMemory_));

              } else if (arg.startsWith("*")) { // INDIRECT DIR
                int index = Integer.parseInt(arg.substring(1)); // Remove *
                op.add(new IndirectDirOperand<Integer>(index,
                                                       memory_.dataMemory_));

              } else { // CONSTANT
                try {
                  // Try to add as Integer
                  op.add(new
                         ConstOperand<Integer>(Integer.parseInt(arg)));

                } catch (NumberFormatException e) {
                  // Add as string (label)
                  op.add(new ConstOperand<String>(arg));
                }
              }
            }
          }

          // Save instruction in program memory
          memory_.putInRegister(lineNumber, new Opcode(id, op));
          ++lineNumber;
        }
      }

      System.out.println("Program loaded!\n" + memory_.programMemory_.toString());
      System.out.println("Tags:\n" + memory_.tags_);

    } catch (IOException e) { // Handle exceptions better
      e.printStackTrace();
    }
  }
}
