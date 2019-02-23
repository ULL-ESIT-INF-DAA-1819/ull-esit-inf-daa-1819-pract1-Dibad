package Ramsim;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
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

      } catch (Exception e) { // Handle exceptions better
        System.out.println("Exception occured during cycle()");
        e.printStackTrace();
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
            for (var s : splitted) {
              if (s.startsWith("=")) { // DIRECT DIR
                int index = Integer.parseInt(s.substring(1));
                op.add(new DirectDirOperand<Integer>(index, memory_.dataMemory_));

              } else if (s.startsWith("*")) { // INDIRECT DIR
                int index = Integer.parseInt(s.substring(1));
                op.add(new IndirectDirOperand<Integer>(index,
                                                       memory_.dataMemory_));

              } else { // CONSTANT
                try {
                  op.add(new
                         ConstOperand<Integer>(Integer.parseInt(s)));

                } catch (NumberFormatException e) {
                  op.add(new ConstOperand<String>(s));
                }
              }
            }
          }

          memory_.putInRegister(lineNumber, new Opcode(id, op));

          ++lineNumber;
        }
      }

      System.out.println(memory_.programMemory_.toString());

    } catch (IOException e) { // Handle exceptions better
      e.printStackTrace();
    }
  }
}
