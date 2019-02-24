package Ramsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import Ramsim.Ramsim;

public class RamsimTest {

  static final String inputFilepath_ = "src/test/java/Ramsim/testinput.txt";
  static final String outputFilepath_ = "src/test/java/Ramsim/testoutput.txt";

  public static ArrayList<Integer> outputToArrayList() {
    ArrayList<Integer> outputArray = new ArrayList<Integer>();

    try(Scanner fileReader = new Scanner(new File(outputFilepath_))) {
      while (fileReader.hasNextInt())
        outputArray.add(fileReader.nextInt());

    } catch (IOException e) {
      e.printStackTrace();
    }


    return outputArray;
  }

  @Test
  public void testProgram1() {
    Ramsim rsm = new Ramsim("tests_ram/test1.ram",
                            inputFilepath_,
                            outputFilepath_,
                            false);
    rsm.execute();

    assertArrayEquals(new Integer[] {1, 2, 2}, outputToArrayList().toArray());
  }

  @Test
  public void testProgram2() {
    Ramsim rsm = new Ramsim("tests_ram/test2.ram",
                            inputFilepath_,
                            outputFilepath_,
                            false);
    rsm.execute();

    assertArrayEquals(new Integer[] {0}, outputToArrayList().toArray());
  }

  @Test
  public void testProgram3() {
    Ramsim rsm = new Ramsim("tests_ram/test3.ram",
                            inputFilepath_,
                            outputFilepath_,
                            false);
    assertThrows(IllegalArgumentException.class, () -> {
      rsm.execute();
    });
  }

  @Test
  public void testProgram4() {
    Ramsim rsm = new Ramsim("tests_ram/test4.ram",
                            inputFilepath_,
                            outputFilepath_,
                            false);
    rsm.execute();

    assertArrayEquals(new Integer[] {2, 4, 4}, outputToArrayList().toArray());
  }

  @Test
  public void testProgram5() {
    Ramsim rsm = new Ramsim("tests_ram/test5.ram",
                            inputFilepath_,
                            outputFilepath_,
                            false);
    rsm.execute();

    assertArrayEquals(new Integer[] {5}, outputToArrayList().toArray());
  }

  @Test
  public void testProgram6() {
    Ramsim rsm = new Ramsim("tests_ram/test6.ram",
                            inputFilepath_,
                            outputFilepath_,
                            false);
    assertThrows(IllegalArgumentException.class, () -> {
      rsm.execute();
    });
  }

  @Test
  public void testProgram7() {
    Ramsim rsm = new Ramsim("tests_ram/test7.ram",
                            inputFilepath_,
                            outputFilepath_,
                            false);
    rsm.execute();

    assertArrayEquals(new Integer[] {3, 6, 6}, outputToArrayList().toArray());
  }
}
