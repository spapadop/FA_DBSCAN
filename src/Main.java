public class Main {

    private static Scan scan;
    private static long startTime;
    private static long endTime;

    private static final int RUN = 2; //1=DBSCAN, 2=FADBSCAN
    private static final double EPS = 0.5; //set the value of eps
    private static final int MINPOINTS = 5; //set the value of MinPoints


    public static void main(String[] args) {

        startTime = System.nanoTime();
        switch (RUN){
            case 1: scan = new DBScan(EPS,MINPOINTS);
                break;
            case 2: scan = new FADBScan(EPS,MINPOINTS);
                break;
        }
        endTime = System.nanoTime();
        System.out.println("Time: " + (endTime - startTime));

    }

}