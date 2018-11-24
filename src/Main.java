/**
 * Université Libre de Bruxelles (ULB)
 * Master Program in Big Data Management & Analytics (BDMA)
 *
 * Course: Data mining
 * Assignment 2: DBSCAN & FADBSCAN implementation.
 *
 * Authors:
 * Carlos Martinez Lorenzo (000477671)
 * Haftamu (000472133)
 * Ioannis Prapas (000473813)
 * Sokratis Papadopoulos (000476296)
 *
 * Date: 28 November 2018
 *
 * Program accepts four (4)  arguments:
 * 	1. the algorithm that you want to run (1=DBSCAN, 2=FADBSCAN)
 * 	2. the value of EPS
 * 	3. the value of MinPoints
 * 	4. the dataset you want to use (input.txt , input-5000.txt , input-100000.txt)
 *
 * 	Example execute command: java -jar DBSCAN.jar 2 20 5 input-100000.txt
 *
 */

public class Main {
    private static Scan scan;
    private static long startTime;
    private static long endTime;

    private static final int RUN = 2; //1=DBSCAN, 2=FADBSCAN
    private static final double EPS = 20; //set the value of eps
    private static final int MINPOINTS = 5; //set the value of MinPoints
    private static final String DIRECTORY = ".\\input\\";
    private static final String FILENAME = DIRECTORY + "input-100000.txt"; // set the filename of dataset
    /* different input files
    aggregation.txt  input-100000.txt   input-5000.txt  spiral.txt  twodiamonds.txt
    input.txt        input-1000000.txt  lsun.txt        target.txt  wingnut.txt
    */

    public static void main(String[] args) {

        startTime = System.nanoTime();

        switch (RUN){
            case 1: scan = new DBScan(EPS,MINPOINTS, FILENAME);
                break;
            case 2: scan = new FADBScan(EPS,MINPOINTS, FILENAME);
                break;
        }

        endTime = System.nanoTime();
        System.out.print("Time: " + (endTime - startTime)/1000000 + "ms");

        scan.print();

    }

}