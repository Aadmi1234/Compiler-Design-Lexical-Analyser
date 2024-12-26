public class Driver {

    public static void main(String[] args) {

        if (3 != args.length) {
            System.out.println("Usage : java Driver <inputFile> <outputFile> <numThreads>");
            return;
        }

        String ipfile = args[0];
        String opfile = args[1];
        int numThreads;

        try {
            numThreads = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Number of threads must be a valid integer.");
            return;
        }

        long startTime = System.currentTimeMillis();

        if (1 == numThreads) {
            SingleThreadProcessor singleThreadProcessor = new SingleThreadProcessor();
            singleThreadProcessor.processFile(ipfile, opfile);
        } else if (1 < numThreads) {
            MultiThreadProcessor multiThreadProcessor = new MultiThreadProcessor(numThreads);
            multiThreadProcessor.processFile(ipfile, opfile);
        } else {
            System.out.println("Invalid number of threads specified.");
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Processing time for " + numThreads + " threads : " + (endTime - startTime) + " ms");
        System.out.println("Processing completed. Check the output files: " + opfile);
    }
}
