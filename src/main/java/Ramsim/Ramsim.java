package Ramsim;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import Ramsim.Memory.Memory;
import Ramsim.Io.InputUnit;
import Ramsim.Io.OutputUnit;
import Ramsim.Instruction.Operand.*;
import Ramsim.Instruction.Opcode;
import Ramsim.Alu.Alu;
import Ramsim.Instruction.InstructId;

public class Ramsim {
  // Ram Simulator components
  private Memory<Integer> dataMemory_;
  private ArrayList<Opcode> programMemory_;
  private HashMap<String, Integer> tags_;
  private InputUnit input_;
  private OutputUnit output_;
  private Alu alu_;

  // Flags
  private boolean debug_;

  // Constructor

  public Ramsim(String programFilePath,
                String inputFilePath,
                String outputFilePath,
                boolean debug) {

    dataMemory_ = new Memory<Integer>();
    programMemory_ = new ArrayList<Opcode>();
    tags_ = new HashMap<String, Integer>();
    input_ = new InputUnit(inputFilePath);
    output_ = new OutputUnit(outputFilePath);
    alu_ = new Alu(dataMemory_, programMemory_, tags_, input_, output_, debug);

    debug_ = debug;

    loadProgram(programFilePath);
  }

  public void execute() {
    try {
      while (!alu_.isHalted())
        alu_.cycle();

    } catch (RuntimeException e) {
      System.out.println("-> Runtime Exception:");
      if (debug_)
        e.printStackTrace();
      else
        System.out.println(e);

      System.out.println("\n   ALU STATE INFORMATION:");
      alu_.printDebugState();

      alu_.halt();
      throw e;
    }

    System.out.println("Output:\n" + output_);
  }

  private void loadProgram(String programFilePath) {
    try(Scanner fileReader = new Scanner(new File(programFilePath))) {

      int lineNum = 0;
      while (fileReader.hasNextLine()) {
        ++lineNum;
        String line = fileReader.nextLine();

        line = line.replaceAll("#[^\n]*", ""); // Remove # comments

        if (!line.isEmpty()) {
          // Split line by whitespaces
          ArrayList<String> splitted = new
          ArrayList<String>(Arrays.asList(line.trim().split("\\s+")));

          // Check if first element is a tag and store tag
          if (splitted.get(0).endsWith(":")) {
            tags_.put(splitted.get(0).replace(":", ""), programMemory_.size());
            splitted.remove(0);
          }

          // Get instruction id
          InstructId id = InstructId.valueOf(splitted.get(0).toUpperCase());
          splitted.remove(0);

          // Get list of operands
          ArrayList<Operand<?>> operand = new ArrayList<Operand<?>>();

          if (!splitted.isEmpty()) {
            for (var arg : splitted) {
              try {
                char firstChar = arg.charAt(0);

                // CONSTANT
                if (firstChar == '=') {
                  arg = arg.substring(1); // Remove first character
                  operand.add(new ConstOperand<>(Integer.parseInt(arg)));

                  // INDIRECT
                } else if (firstChar == '*') {
                  arg = arg.substring(1); // Remove first character
                  operand.add(new IndirectDirOperand<>(Integer.parseInt(arg),
                                                       dataMemory_));
                }

                // DIRECT
                else {
                  operand.add(new DirectDirOperand<>(Integer.parseInt(arg),
                                                     dataMemory_));
                }

                // If can't be saved as an int must be a label (string)
              } catch (NumberFormatException e) {
                operand.add(new ConstOperand<String>(arg));
              }
            }
          }

          // Save instruction in program memory
          programMemory_.add(new Opcode(id, operand, lineNum));
        }
      }

      if (debug_) {
        System.out.println(String.format("Program loaded!\n%d instructions:\n%s",
                                         programMemory_.size(), programMemory_));
        System.out.println("Tags:\n" + tags_ + "\n");
      }

    } catch (IOException e) { // Handle exceptions better
      e.printStackTrace();
    }
  }
}
