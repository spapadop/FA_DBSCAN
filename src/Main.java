import java.util.Timer;

public class Main {

    private static long startTime;
    private static long endTime;

    public static void main(String[] args) {

//        startTime = System.nanoTime();
//        FADBScan startFA = new FADBScan();
//        endTime = System.nanoTime();
//
//        System.out.println(endTime -startTime);

        startTime = System.nanoTime();
        DBScan start = new DBScan();
        endTime = System.nanoTime();

        System.out.println(endTime -startTime);
    }
}
// 27878739

// 25671774

//