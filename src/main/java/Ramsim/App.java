package Ramsim;

import Ramsim.Ramsim;

/**
  * Main class for loading RamSim programs
  *
  * @author David Afonso Dorta
  * @since 2019-02-25
  * e-mail: alu0101015255@ull.edu.es
  */
public class App {
  public static void main(String[] args) {
    if (args.length != 4) { // This 4 arguments MUST be provided
      System.out.println(
        "Use: Ramsim ram_program.ram input_tape.in output_tape.out debug[0|1]");
      return;
    }

    String ramProgram = args[0];
    String inputTape = args[1];
    String outputTape = args[2];
    boolean debug = args[3].equals("1");

    Ramsim rms = new Ramsim(ramProgram, inputTape, outputTape, debug);

    try {
      rms.execute();

    } catch (Exception e) { // Most error printing is done in lower levels
      System.out.println("RAMSIM::EXECUTE()::Exception:\n" + e);
    }
  }
}
