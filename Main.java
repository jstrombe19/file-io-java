/**********************************************************************************************************************
 * CLASS: Main.java
 *
 * DESCRIPTION:
 * This is the Main class for this project. It performs a simple analysis of a provided input file (in this case,
 * p1-in.txt) to determine how many "runs" are present. A "run" is defined as a sequence of integers on any given
 * line(s) of the input file within the range [0, 32767] which are monotonically increasing or decreasing. This means
 * that each subsequent integer of that line is either greater than or less than the integer immediately preceding it.
 * The length of a given run is determined by the number of integers which follow the monotonic increase or decrease
 * (provided the trend remains constant).
 *
 * For example, given the sequence 2 8 3, the total number of runs present is 2. {2,8} and {8,3} are monotonically
 * increasing and monotonically decreasing, respectively. {2,8,3} is not a run because the trend shifts from increasing
 * to decreasing.
 *
 * In addition to simple, two-digit runs, runs may span up to n-1 integers in length, where n is the number of integers
 * in the input file. Integers provided are considered across multiple rows to form spans. The total number of spans of
 * each length are written to an output file - p1-runs.txt, sorted in increasing length.
 *
 * For example, given the file p1-in.txt:
 * 2 8 3
 * 2 9
 * 8
 * 6
 * 3 4 6 1 9
 *
 * The output in p1-out.txt would be:
 * runs_total: 7
 * runs_1: 4
 * runs_2: 2
 * runs_3: 1
 * runs_4: 0
 * runs_5: 0
 * runs_6: 0
 * runs_7: 0
 * runs_8: 0
 * runs_9: 0
 * runs_10: 0
 * runs_11: 0
 *
 * COURSE AND PROJECT INFORMATION:
 * CSE205 Object-Oriented Programming and Data Structures, Spring 2022
 *
 * PROJECT NUMBER: 1
 *
 * AUTHOR: Jared Stromberg, 1207603783, Jared.Stromberg@asu.edu
 *********************************************************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static int RUNS_UP = 1;
    static int RUNS_DOWN = -1;

    public static void main(String[] pArgs) {
        Main mainObject = new Main();
        mainObject.run();
    }
    private void run() {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            list = readInputFile("p1-in.txt");
        } catch (FileNotFoundException pException) {
            System.out.println("Oops, could not open 'p1-in.txt' for reading. The program is ending.");
            System.exit(-100);
        }
        ArrayList<Integer> listRunsUpCount = findRuns(list, RUNS_UP);
        ArrayList<Integer> listRunsDownCount = findRuns(list, RUNS_DOWN);
        ArrayList<Integer> listRunsCount = mergeLists(listRunsUpCount, listRunsDownCount);
        try {
            writeOutputFile("p1-runs.txt", listRunsCount);
        } catch (FileNotFoundException pException) {
            System.out.println("Oops, could not open 'p1-out.txt' for writing. The program is ending.");
            System.exit(-200);
        }
    }

    /**
     * This method identifies runs in the input sequence; it also determines which type of run it is as well as its
     * length, inserts it into the applicable ArrayList entry for that run length, and increments the total run count.
     * Because the arguments are mutated by the method, there is no need for an explicit return.
     * @param pList ArrayList<Integer>
     * @param pDir int
     */
    private ArrayList<Integer> findRuns(ArrayList<Integer> pList, int pDir) {
        ArrayList<Integer> listRunsCount = new ArrayList<>();
        listRunsCount = arrayListCreate(pList.size(), 0);
        int i = 0;
        int k = 0;
        while (i < pList.size() - 1) {
            if (pDir == RUNS_UP & (int) pList.get(i) <= (int) pList.get(i + 1)) {
                ++k;
            } else if (pDir == RUNS_DOWN & (int) pList.get(i) >= (int) pList.get(i + 1)) {
                ++k;
            } else {
              if (k != 0) {
                      listRunsCount.set(k, listRunsCount.get(k) + 1);
                      k = 0;
                  }
            }
            ++i;
        }
        if (k != 0) {
            listRunsCount.set(k, listRunsCount.get(k) + 1);
        }
        return listRunsCount;
    }

    /**
     * This method merges two integer ArrayLists into a single integer ArrayList.
     * @param pListRunsUpCount ArrayList<Integer>
     * @param pListRunsDownCount ArrayList<Integer>
     * @return ArrayList<Integer> merged integer ArrayList
     */
    private ArrayList<Integer> mergeLists(ArrayList<Integer> pListRunsUpCount, ArrayList<Integer> pListRunsDownCount) {
        ArrayList<Integer> listRunsCount = arrayListCreate(pListRunsUpCount.size(), 0);
        for (int i = 0; i < pListRunsUpCount.size(); ++i) {
            listRunsCount.set(i, (pListRunsUpCount.get(i) + pListRunsDownCount.get(i)));
        }
        return listRunsCount;
    }

    /**
    * This method iterates over a given integer ArrayList and provides the sum of all values contained therein.
    * @arrayList ArrayList<Integer>
    * @return int summed values of the integer ArrayList parameter
    */
    private int sumArrayListValues(ArrayList<Integer> arrayList) {
      int sum = 0;
      for (int i = 0; i < arrayList.size(); ++i) {
        sum += (int) arrayList.get(i);
      }
      return sum;
    }
                                  
    /**
     * This method attempts to print the output generated by the main process to 'p1-runs.txt'
     * @param pFileName String
     * @param pListRuns ArrayList<Integer>
     * @throws FileNotFoundException in the event the file cannot be opened or written to.
     */
    private void writeOutputFile(String pFileName, ArrayList<Integer> pListRuns) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(pFileName);
        out.println("runs_total: " + sumArrayListValues(pListRuns));
        for (int k = 1; k < pListRuns.size(); ++k) {
            out.println("runs_" + k + ": " + pListRuns.get(k));
        }
        out.close();
    }

    /**
     * This method generates a pre-populated integer ArrayList that is the same length as that needed for the final
     * output as described above. This is what will be written to the output file.
     * @param pSize int - total size of the ArrayList
     * @param pInitValue int - the default initial value to set each placeholder to
     * @return ArrayList<Integer> of pre-filled default value the size required for 'p1-runs.txt'
     */
    private ArrayList<Integer> arrayListCreate(int pSize, int pInitValue) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < pSize; i++) {
            list.add(pInitValue);
        }
        return list;
    }

    /**
     * This method opens an input file at the path provided as an argument, then generates an ArrayList from the
     * integer values contained in that file. If a value is not an integer, the method will print a message to the
     * terminal and continue on, skipping over that value.
     * @param pFileName: String
     * @return ArrayList<Integer>
     * @throws FileNotFoundException in the event the input file provided cannot be found.
     */
    private ArrayList<Integer> readInputFile(String pFileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(pFileName));
        ArrayList<Integer> input = new ArrayList<>();
        while (in.hasNext()) {
          try {
                int nextInt = in.nextInt();
                input.add(nextInt);
            } catch (InputMismatchException pException) {
                System.out.println("Wrong data type encountered; moving on.");
            }
        }
        in.close();
        return input;
    }
}

