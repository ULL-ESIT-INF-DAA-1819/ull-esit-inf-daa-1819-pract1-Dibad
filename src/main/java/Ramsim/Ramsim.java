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
        e.printStackTrace();
        quit_ = true;
      }
    }
  }

  private void loadProgram(String programFilePath) {
    try(Scanner fileReader = new Scanner(new File(programFilePath))) {

      int lineNumber = 0;
      while (fileReader.hasNextLine()) {
        String line = fileReader.nextLine();

        System.out.println(line);
        line = line.replaceAll("#[^\n]*", ""); // Remove # comments

        if (!line.isEmpty()) {
          // Split line by whitespaces
          ArrayList<String> splitted = new
          ArrayList<String>(Arrays.asList(line.trim().split("\\s+")));

          // Check if first element is a tag and save it
          if (splitted.get(0).endsWith(":")) {
            memory_.setTag(splitted.get(0).replace(":", ""), lineNumber);
            splitted.remove(0);
          }

          // Get instruction id
          InstructId id = InstructId.valueOf(splitted.get(0).toUpperCase());
          splitted.remove(0);

          // Get list of operands
          ArrayList<Operand<?>> op = new ArrayList<Operand<?>>();

          if (!splitted.isEmpty()) {
            for (var arg : splitted) {
              try {
                char firstChar = arg.charAt(0);

                // CONSTANT
                if (firstChar == '=') {
                  arg = arg.substring(1);
                  op.add(new ConstOperand<Integer>(Integer.parseInt(arg)));

                } else if (firstChar == '*') {
                  arg = arg.substring(1);
                  op.add(new IndirectDirOperand<Integer>(Integer.parseInt(arg),
                                                         memory_.dataMemory_));
                }

                else {
                  op.add(new DirectDirOperand<Integer>(Integer.parseInt(arg),
                                                       memory_.dataMemory_));
                }

                // If can't be saved as an int is a label (string)
              } catch (NumberFormatException e) {
                op.add(new ConstOperand<String>(arg));
              }
            }
          }

          // Save instruction in program memory
          memory_.putInRegister(lineNumber, new Opcode(id, op));
          ++lineNumber;
        }
      }

      System.out.println(lineNumber);
      System.out.println("Program loaded!\n" + memory_.programMemory_.toString());
      System.out.println("Tags:\n" + memory_.tags_);

    } catch (IOException e) { // Handle exceptions better
      e.printStackTrace();
    }
  }
}
